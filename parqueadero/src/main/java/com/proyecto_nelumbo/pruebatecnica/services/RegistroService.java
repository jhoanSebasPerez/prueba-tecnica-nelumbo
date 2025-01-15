package com.proyecto_nelumbo.pruebatecnica.services;

import com.proyecto_nelumbo.pruebatecnica.dtos.request.RegistrarVehiculoRequest;
import com.proyecto_nelumbo.pruebatecnica.dtos.response.HistorialResponse;
import com.proyecto_nelumbo.pruebatecnica.dtos.response.RegistroResponse;
import com.proyecto_nelumbo.pruebatecnica.entities.Historial;
import com.proyecto_nelumbo.pruebatecnica.entities.Parqueadero;
import com.proyecto_nelumbo.pruebatecnica.entities.Registro;
import com.proyecto_nelumbo.pruebatecnica.mappers.HistorialMapper;
import com.proyecto_nelumbo.pruebatecnica.mappers.RegistroMapper;
import com.proyecto_nelumbo.pruebatecnica.repositories.HistorialRepository;
import com.proyecto_nelumbo.pruebatecnica.repositories.ParqueaderoRepository;
import com.proyecto_nelumbo.pruebatecnica.repositories.RegistroRepository;
import com.proyecto_nelumbo.pruebatecnica.security.SecurityUtil;
import com.proyecto_nelumbo.servicioemail.dtos.EmailIngresoRequest;
import com.proyecto_nelumbo.servicioemail.dtos.EmailResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistroService {

    private static final Logger logger = LoggerFactory.getLogger(RegistroService.class);

    private final RegistroRepository registroRepository;
    private final ParqueaderoRepository parqueaderoRepository;
    private final HistorialRepository historialRepository;
    private final SecurityUtil securityUtil;
    private final EmailService emailService;

    public RegistroService(RegistroRepository registroRepository,
                           ParqueaderoRepository parqueaderoRepository,
                            HistorialRepository historialRepository,
                           SecurityUtil securityUtil,
                           EmailService emailService) {
        this.registroRepository = registroRepository;
        this.parqueaderoRepository = parqueaderoRepository;
        this.historialRepository = historialRepository;
        this.securityUtil = securityUtil;
        this.emailService = emailService;
    }

    public RegistroResponse registrarVehiculo(Long parqueaderoId, RegistrarVehiculoRequest request) {
        Long socioId = securityUtil.obtenerUserIdDesdeContext();

        Parqueadero parqueadero = parqueaderoRepository
                .findById(parqueaderoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe parqueadero con ID " + parqueaderoId));

        if (parqueadero.getSocio() == null ||
                !parqueadero.getSocio().getId().equals(socioId)) {
            throw new AccessDeniedException(
                    "Solo el socio asignado a este parqueadero puede registrar un vehículo."
            );
        }

        int vehiculosActuales = parqueadero.getRegistros().size();
        if (vehiculosActuales >= parqueadero.getCapacidad()) {
            throw new IllegalStateException("El parqueadero ha alcanzado su capacidad máxima de " + parqueadero.getCapacidad() + " vehículos.");
        }

        if (registroRepository.existsByPlaca(request.placa())) {
            throw new IllegalArgumentException(
                    "No se puede Regisrar Ingreso, ya existe la placa en este u otro parqueadero");
        }

        Registro nuevoRegistro = RegistroMapper.toEntity(
                request,
                parqueadero,
                LocalDateTime.now()
        );
        registroRepository.save(nuevoRegistro);

        //Envio de la peticion al servicio de email (simulacion)
        EmailIngresoRequest emailIngresoRequest = new EmailIngresoRequest(
                "example@correo.com",
                request.placa(),
                "Vehículo registrado en parqueadero",
                parqueaderoId
        );

        EmailResponse response = emailService.enviarEmailIngreso(emailIngresoRequest);

        if(response != null){
            logger.info("Respuesta del servicio de email: {}", response.mensaje());
        }

        return RegistroMapper.toResponse(nuevoRegistro);
    }

    @Transactional
    public HistorialResponse registrarSalida(Long parqueaderoId, String placa) {
        Registro registro = registroRepository.findByPlaca(placa)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se puede Registrar Salida, la placa no se encuentra en el sistema"));

        if(!registro.getParqueadero().getId().equals(parqueaderoId)){
            throw new IllegalArgumentException(
                    "No se puede Registrar Salida, la placa no pertenece a este parqueadero");
        }

        Parqueadero parqueadero = registro.getParqueadero();
        LocalDateTime fechaHoraIngreso = registro.getFechaHoraIngreso();
        LocalDateTime fechaHoraSalida = LocalDateTime.now();

        Historial historial = new Historial();
        historial.setPlaca(registro.getPlaca());
        historial.setParqueadero(parqueadero);
        historial.setFechaHoraIngreso(fechaHoraIngreso);
        historial.setFechaHoraSalida(fechaHoraSalida);

        Duration duracion = Duration.between(fechaHoraIngreso, fechaHoraSalida);
        BigDecimal horas = BigDecimal.valueOf(duracion.toMinutes())
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        BigDecimal tarifa = parqueadero.getCostoTarifaHora();
        BigDecimal valorCalculado = tarifa.multiply(horas);
        historial.setValorCalculado(valorCalculado);

        Historial guardado = historialRepository.save(historial);

        registroRepository.delete(registro);

        return HistorialMapper.toResponse(guardado);
    }

    public List<RegistroResponse> listarRegistrosPorParqueadero(Long parqueaderoId) {

        Parqueadero parqueadero = parqueaderoRepository.findById(parqueaderoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe un parqueadero con ID " + parqueaderoId));

        Long socioId = securityUtil.obtenerUserIdDesdeContext();
        if (parqueadero.getSocio() == null || !parqueadero.getSocio().getId().equals(socioId)) {
            throw new AccessDeniedException("No tienes permiso para ver los registros de este parqueadero.");
        }

        List<Registro> registros = registroRepository.findByParqueaderoId(parqueaderoId);
        return registros.stream()
                .map(RegistroMapper::toResponse)
                .toList();
    }

}