package com.proyecto_nelumbo.pruebatecnica.services;

import com.proyecto_nelumbo.pruebatecnica.dtos.request.CreateSocioRequest;
import com.proyecto_nelumbo.pruebatecnica.dtos.request.UpdateUsuarioRequest;
import com.proyecto_nelumbo.pruebatecnica.dtos.response.UserResponse;
import com.proyecto_nelumbo.pruebatecnica.entities.Parqueadero;
import com.proyecto_nelumbo.pruebatecnica.entities.Usuario;
import com.proyecto_nelumbo.pruebatecnica.mappers.UserMapper;
import com.proyecto_nelumbo.pruebatecnica.repositories.ParqueaderoRepository;
import com.proyecto_nelumbo.pruebatecnica.repositories.UsuarioRepository;
import com.proyecto_nelumbo.pruebatecnica.security.SecurityUtil;
import com.proyecto_nelumbo.servicioemail.dtos.EmailResponse;
import com.proyecto_nelumbo.servicioemail.dtos.RegistroSocioRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ParqueaderoRepository parqueaderoRepository;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          EmailService emailService,
                          ParqueaderoRepository parqueaderoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.parqueaderoRepository = parqueaderoRepository;
    }

    public List<UserResponse> obtenerSocios() {
        List<Usuario> socios = usuarioRepository.findByRole("SOCIO");
        return socios.stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse crearSocio(CreateSocioRequest request) {
        if (usuarioRepository.findByEmail(request.email()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un usuario con el email " + request.email()
            );
        }

        String passwordEncriptada = passwordEncoder.encode(request.password());

        Usuario socio = UserMapper.toEntity(request, passwordEncriptada, "SOCIO");
        usuarioRepository.save(socio);

        RegistroSocioRequest registroSocioRequest = new RegistroSocioRequest(
                socio.getEmail(),
                "Registro de socio exitoso"
        );

        EmailResponse response =  emailService.enviarEmailRegistroSocio(registroSocioRequest);

        if(response != null){
            logger.info("Correo de registro de socio enviado");
        }

        return UserMapper.toResponse(socio);
    }

    @Transactional
    public UserResponse actualizarUsuario(Long usuarioId, UpdateUsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("No existe un usuario con ID " + usuarioId));

        Long usuarioAutenticadoId = SecurityUtil.obtenerUserIdDesdeContext();
        boolean isAdmin = SecurityUtil.usuarioEsAdmin();

        if (!isAdmin && !usuario.getId().equals(usuarioAutenticadoId)) {
            throw new AccessDeniedException("No tienes permisos para actualizar este usuario.");
        }

        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());

        usuarioRepository.save(usuario);

        return UserMapper.toResponse(usuario);
    }

    @Transactional
    public void eliminarSocio(Long socioId) {
        Usuario socio = usuarioRepository.findById(socioId)
                .orElseThrow(() -> new IllegalArgumentException("No existe el socio con ID " + socioId));

        List<Parqueadero> parqueaderos = parqueaderoRepository.findBySocioId(socioId);

        boolean tieneVehiculosRegistrados = parqueaderos.stream()
                .anyMatch(parqueadero -> !parqueadero.getRegistros().isEmpty());

        if (tieneVehiculosRegistrados) {
            throw new IllegalStateException("No se puede eliminar el socio, ya que existen vehículos registrados en sus parqueaderos.");
        }

        parqueaderos.forEach(parqueadero -> parqueadero.setSocio(null));
        parqueaderoRepository.saveAll(parqueaderos);

        usuarioRepository.delete(socio);
    }
}