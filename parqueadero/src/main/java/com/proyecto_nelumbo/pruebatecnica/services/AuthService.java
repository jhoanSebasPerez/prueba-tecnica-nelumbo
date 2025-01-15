package com.proyecto_nelumbo.pruebatecnica.services;

import com.proyecto_nelumbo.pruebatecnica.dtos.request.LoginRequest;
import com.proyecto_nelumbo.pruebatecnica.dtos.response.LoginResponse;
import com.proyecto_nelumbo.pruebatecnica.entities.Usuario;
import com.proyecto_nelumbo.pruebatecnica.mappers.AuthMapper;
import com.proyecto_nelumbo.pruebatecnica.repositories.UsuarioRepository;
import com.proyecto_nelumbo.pruebatecnica.security.CustomUserDetails;
import com.proyecto_nelumbo.pruebatecnica.security.JwtUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthService(
            AuthenticationManager authenticationManager,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtUtils jwtUtils
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public LoginResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                );

        Authentication auth = authenticationManager.authenticate(authToken);

        var principal = (CustomUserDetails) auth.getPrincipal();
        Long userId = principal.getUserId();

        String jwt = jwtUtils.generateToken(userId, request.email());

        return new LoginResponse(jwt);
    }
}