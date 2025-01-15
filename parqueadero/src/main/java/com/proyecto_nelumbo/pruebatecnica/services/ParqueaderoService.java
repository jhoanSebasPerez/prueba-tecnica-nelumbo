package com.proyecto_nelumbo.pruebatecnica.services;

import com.proyecto_nelumbo.pruebatecnica.dtos.request.CreateParqueaderoRequest;
import com.proyecto_nelumbo.pruebatecnica.dtos.response.ParqueaderoResponse;
import com.proyecto_nelumbo.pruebatecnica.entities.Parqueadero;
import com.proyecto_nelumbo.pruebatecnica.entities.Usuario;
import com.proyecto_nelumbo.pruebatecnica.mappers.ParqueaderoMapper;
import com.proyecto_nelumbo.pruebatecnica.repositories.HistorialRepository;
import com.proyecto_nelumbo.pruebatecnica.repositories.ParqueaderoRepository;
import com.proyecto_nelumbo.pruebatecnica.repositories.UsuarioRepository;
import com.proyecto_nelumbo.pruebatecnica.security.SecurityUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ParqueaderoService {

    private final ParqueaderoRepository parqueaderoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SecurityUtil securityUtil;

    public ParqueaderoService(
            ParqueaderoRepository parqueaderoRepository,
            UsuarioRepository usuarioRepository,
            HistorialRepository historialRepository,
            SecurityUtil securityUtil
    ) {
        this.parqueaderoRepository = parqueaderoRepository;
        this.usuarioRepository = usuarioRepository;
        this.securityUtil = securityUtil;
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

        parqueaderoRepository.save(parqueadero);

        return ParqueaderoMapper.toResponse(parqueadero);
    }

    public ParqueaderoResponse asociarSocioAParqueadero(Long parqueaderoId, Long socioId) {

        Parqueadero parqueadero = parqueaderoRepository.findById(parqueaderoId)
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
        Long socioId = securityUtil.obtenerUserIdDesdeContext();
        List<Parqueadero> parqueaderos = parqueaderoRepository.findBySocioId(socioId);

        return parqueaderos.stream()
                .map(ParqueaderoMapper::toResponse)
                .toList();
    }
}