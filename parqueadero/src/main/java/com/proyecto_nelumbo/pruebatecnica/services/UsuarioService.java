package com.proyecto_nelumbo.pruebatecnica.services;

import com.proyecto_nelumbo.pruebatecnica.dtos.request.CreateSocioRequest;
import com.proyecto_nelumbo.pruebatecnica.dtos.response.UserResponse;
import com.proyecto_nelumbo.pruebatecnica.entities.Usuario;
import com.proyecto_nelumbo.pruebatecnica.mappers.UserMapper;
import com.proyecto_nelumbo.pruebatecnica.repositories.UsuarioRepository;
import com.proyecto_nelumbo.servicioemail.dtos.EmailResponse;
import com.proyecto_nelumbo.servicioemail.dtos.RegistroSocioRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
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
}