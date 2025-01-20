package com.proyecto_nelumbo.pruebatecnica.services;

import com.proyecto_nelumbo.pruebatecnica.dtos.response.TopParqueaderoResponse;
import com.proyecto_nelumbo.pruebatecnica.dtos.response.TopSocioResponse;
import com.proyecto_nelumbo.pruebatecnica.dtos.response.TopVehiculoResponse;
import com.proyecto_nelumbo.pruebatecnica.dtos.response.VehiculoPrimeraVezResponse;
import com.proyecto_nelumbo.pruebatecnica.entities.Parqueadero;
import com.proyecto_nelumbo.pruebatecnica.entities.Registro;
import com.proyecto_nelumbo.pruebatecnica.repositories.HistorialRepository;
import com.proyecto_nelumbo.pruebatecnica.repositories.ParqueaderoRepository;
import com.proyecto_nelumbo.pruebatecnica.security.SecurityUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndicadorService {

    private final ParqueaderoRepository parqueaderoRepository;
    private final HistorialRepository historialRepository;

    public IndicadorService(ParqueaderoRepository parqueaderoRepository,
                            HistorialRepository historialRepository) {
        this.parqueaderoRepository = parqueaderoRepository;
        this.historialRepository = historialRepository;
    }

    public List<TopVehiculoResponse> obtenerTop10Vehiculos() {
        return historialRepository.findTop10Vehiculos();
    }

    public List<TopVehiculoResponse> obtenerTop10VehiculosPorParqueadero(Long parqueaderoId) {
        manejarPermisos(parqueaderoId);

        return historialRepository.findTop10VehiculosByParqueaderoId(parqueaderoId);
    }

    public List<VehiculoPrimeraVezResponse> obtenerVehiculosPrimeraVez(Long parqueaderoId) {
        manejarPermisos(parqueaderoId);

        List<Registro> registrosActuales = parqueaderoRepository.findById(parqueaderoId)
                .orElseThrow(() -> new IllegalArgumentException("No existe parqueadero con ID " + parqueaderoId))
                .getRegistros();

        List<VehiculoPrimeraVezResponse> vehiculosPrimeraVez = registrosActuales.stream()
                .filter(registro -> !historialRepository.existeRegistroPrevio(parqueaderoId, registro.getPlaca()))
                .map(registro -> new VehiculoPrimeraVezResponse(registro.getPlaca()))
                .collect(Collectors.toList());

        return vehiculosPrimeraVez;
    }

    private void manejarPermisos(Long parqueaderoId) {
        boolean isAdmin = SecurityUtil.usuarioEsAdmin();

        if (!isAdmin) {
            Long socioId = SecurityUtil.obtenerUserIdDesdeContext();
            Parqueadero parqueadero = parqueaderoRepository.findById(parqueaderoId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No existe parqueadero con ID " + parqueaderoId));

            if (parqueadero.getSocio() == null || !parqueadero.getSocio().getId().equals(socioId)) {
                throw new AccessDeniedException("No tienes permiso para ver este indicador.");
            }
        }
    }

    public Double obtenerGanancias(Long parqueaderoId, String periodo) {
        Long socioId = SecurityUtil.obtenerUserIdDesdeContext();
        var parqueadero = parqueaderoRepository.findById(parqueaderoId)
                .orElseThrow(() -> new IllegalArgumentException("No existe parqueadero con ID " + parqueaderoId));

        if (parqueadero.getSocio() == null || !parqueadero.getSocio().getId().equals(socioId)) {
            throw new AccessDeniedException("No tienes permiso para ver este indicador.");
        }

        LocalDateTime fechaInicio;
        LocalDateTime fechaFin = LocalDateTime.now();
        switch (periodo.toLowerCase()) {
            case "hoy" -> fechaInicio = fechaFin.truncatedTo(ChronoUnit.DAYS);
            case "semana" -> fechaInicio = fechaFin.minusWeeks(1).truncatedTo(ChronoUnit.DAYS);
            case "mes" -> fechaInicio = fechaFin.minusMonths(1).truncatedTo(ChronoUnit.DAYS);
            case "año" -> fechaInicio = fechaFin.minusYears(1).truncatedTo(ChronoUnit.DAYS);
            default -> throw new IllegalArgumentException("Periodo no válido. Los valores permitidos son: hoy, semana, mes, año.");
        }

        Double ganancias = historialRepository.obtenerGanancias(parqueaderoId, fechaInicio, fechaFin);
        return ganancias != null ? ganancias : 0.0;
    }

    public List<TopSocioResponse> obtenerTop3SociosConMasIngresosSemana() {
        List<Object[]> resultados = historialRepository.obtenerTop3SociosConMasIngresosSemana();

        return resultados.stream()
                .map(resultado -> new TopSocioResponse((String) resultado[0], ((Number) resultado[1]).intValue()))
                .collect(Collectors.toList());
    }

    public List<TopParqueaderoResponse> obtenerTop3ParqueaderosConMayorGananciaSemana() {
        List<Object[]> resultados = historialRepository.obtenerTop3ParqueaderosConMayorGananciaSemana();

        return resultados.stream()
                .map(resultado -> new TopParqueaderoResponse((String) resultado[0], ((Number) resultado[1]).doubleValue()))
                .collect(Collectors.toList());
    }

}
