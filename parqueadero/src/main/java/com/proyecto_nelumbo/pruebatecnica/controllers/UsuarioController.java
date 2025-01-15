package com.proyecto_nelumbo.pruebatecnica.controllers;

import com.proyecto_nelumbo.pruebatecnica.dtos.request.CreateSocioRequest;
import com.proyecto_nelumbo.pruebatecnica.dtos.response.UserResponse;
import com.proyecto_nelumbo.pruebatecnica.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/socios")
    public ResponseEntity<UserResponse> crearSocio(
            @Valid @RequestBody CreateSocioRequest request
    ) {
        UserResponse response = usuarioService.crearSocio(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}