package com.proyecto_nelumbo.pruebatecnica.services;

import com.proyecto_nelumbo.pruebatecnica.dtos.request.CreateParqueaderoRequest;
import com.proyecto_nelumbo.pruebatecnica.dtos.request.UpdateParqueaderoRequest;
import com.proyecto_nelumbo.pruebatecnica.dtos.response.ParqueaderoDetalleResponse;
import com.proyecto_nelumbo.pruebatecnica.dtos.response.ParqueaderoResponse;
import com.proyecto_nelumbo.pruebatecnica.entities.Parqueadero;
import com.proyecto_nelumbo.pruebatecnica.entities.Usuario;
import com.proyecto_nelumbo.pruebatecnica.mappers.ParqueaderoMapper;
import com.proyecto_nelumbo.pruebatecnica.repositories.HistorialRepository;
import com.proyecto_nelumbo.pruebatecnica.repositories.ParqueaderoRepository;
import com.proyecto_nelumbo.pruebatecnica.repositories.UsuarioRepository;
import com.proyecto_nelumbo.pruebatecnica.security.SecurityUtil;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ParqueaderoService {

    private final ParqueaderoRepository parqueaderoRepository;
    private final UsuarioRepository usuarioRepository;

    public ParqueaderoService(
            ParqueaderoRepository parqueaderoRepository,
            UsuarioRepository usuarioRepository,
            HistorialRepository historialRepository
    ) {
        this.parqueaderoRepository = parqueaderoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ParqueaderoResponse crearParqueadero(CreateParqueaderoRequest request) {

        Usuario socio = null;
        if (request.socioId() != null) {
            Optional<Usuario> socioOpt = usuarioRepository.findById(request.socioId());
            if (socioOpt.isEmpty()) {
                throw new IllegalArgumentException("No existe usuario con ID " + request.socioId());
            }
            Usuario userFound = socioOpt.get();

            if (!"SOCIO".equalsIgnoreCase(userFound.getRole())) {
                throw new AccessDeniedException("El usuario con ID " + userFound.getId() + " no es SOCIO.");
            }
            socio = userFound;
        }

        Parqueadero parqueadero = ParqueaderoMapper.toEntity(request, socio);
        parqueadero.setHabilitado(true);

        parqueaderoRepository.save(parqueadero);

        return ParqueaderoMapper.toResponse(parqueadero);
    }

    public ParqueaderoResponse asociarSocioAParqueadero(Long parqueaderoId, Long socioId) {

        Parqueadero parqueadero = parqueaderoRepository.findByIdHabilitado(parqueaderoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe parqueadero con ID " + parqueaderoId
                ));

        Usuario socio = usuarioRepository.findById(socioId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe usuario con ID " + socioId
                ));

        if (!"SOCIO".equalsIgnoreCase(socio.getRole())) {
            throw new AccessDeniedException(
                    "El usuario con ID " + socioId + " no es SOCIO."
            );
        }

        parqueadero.setSocio(socio);
        parqueaderoRepository.save(parqueadero);

        return ParqueaderoMapper.toResponse(parqueadero);
    }

    public List<ParqueaderoResponse> listarParqueaderosSocio() {
        Long socioId = SecurityUtil.obtenerUserIdDesdeContext();
        List<Parqueadero> parqueaderos = parqueaderoRepository.findAllHabilitados()
                .stream()
                .filter(p -> p.getSocio() != null && p.getSocio().getId().equals(socioId))
                .toList();

        return parqueaderos.stream()
                .map(ParqueaderoMapper::toResponse)
                .toList();
    }

    @Transactional
    public ParqueaderoResponse actualizarParqueadero(Long id, UpdateParqueaderoRequest request) {
        Parqueadero parqueadero = parqueaderoRepository.findByIdHabilitado(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe un parqueadero con ID " + id));

        Long usuarioId = SecurityUtil.obtenerUserIdDesdeContext();
        boolean isAdmin = SecurityUtil.usuarioEsAdmin();

        // Validar si el usuario es propietario del parqueadero o es un administrador
        if (!isAdmin && (parqueadero.getSocio() == null || !parqueadero.getSocio().getId().equals(usuarioId))) {
            throw new AccessDeniedException("No tienes permisos para actualizar este parqueadero.");
        }

        // Validación de capacidad
        int vehiculosRegistrados = parqueadero.getRegistros().size();
        if (request.capacidad() < vehiculosRegistrados) {
            throw new IllegalArgumentException("La nueva capacidad no puede ser menor que la cantidad de vehículos registrados actualmente: " + vehiculosRegistrados);
        }

        // Actualizar datos del parqueadero
        parqueadero.setNombre(request.nombre());
        parqueadero.setDireccion(request.direccion());
        parqueadero.setCostoTarifaHora(request.costoTarifaHora());
        parqueadero.setCapacidad(request.capacidad());

        parqueaderoRepository.save(parqueadero);

        return ParqueaderoMapper.toResponse(parqueadero);
    }

    public ParqueaderoDetalleResponse obtenerParqueadero(Long id) {
        Parqueadero parqueadero = parqueaderoRepository.findByIdHabilitado(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe un parqueadero con ID " + id));

        Long usuarioId = SecurityUtil.obtenerUserIdDesdeContext();
        boolean isAdmin = SecurityUtil.usuarioEsAdmin();

        // Validación: Solo el socio propietario o el admin pueden acceder
        if (!isAdmin && (parqueadero.getSocio() == null || !parqueadero.getSocio().getId().equals(usuarioId))) {
            throw new AccessDeniedException("No tienes permisos para ver la información de este parqueadero.");
        }

        return ParqueaderoMapper.toDetalleResponse(parqueadero);
    }

    @Transactional
    public void eliminarParqueadero(Long id) {
        Parqueadero parqueadero = parqueaderoRepository.findByIdHabilitado(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe un parqueadero con ID " + id));

        parqueadero.setHabilitado(false);
        parqueaderoRepository.save(parqueadero);
    }

    public List<ParqueaderoResponse> obtenerParqueaderos() {
        return parqueaderoRepository.findAllHabilitados()
                .stream()
                .map(ParqueaderoMapper::toResponse)
                .toList();
    }
}