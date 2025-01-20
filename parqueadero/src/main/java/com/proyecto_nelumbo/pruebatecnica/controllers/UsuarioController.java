package com.proyecto_nelumbo.pruebatecnica.controllers;

import com.proyecto_nelumbo.pruebatecnica.dtos.request.CreateSocioRequest;
import com.proyecto_nelumbo.pruebatecnica.dtos.request.UpdateUsuarioRequest;
import com.proyecto_nelumbo.pruebatecnica.dtos.response.UserResponse;
import com.proyecto_nelumbo.pruebatecnica.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/socios")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> obtenerSocios() {
        return usuarioService.obtenerSocios();
    }

    @PutMapping("/{usuarioId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SOCIO')")
    public ResponseEntity<UserResponse> actualizarUsuario(
            @PathVariable Long usuarioId,
            @Valid @RequestBody UpdateUsuarioRequest request) {

        UserResponse response = usuarioService.actualizarUsuario(usuarioId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{socioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> eliminarSocio(@PathVariable Long socioId) {
        usuarioService.eliminarSocio(socioId);
        return ResponseEntity.noContent().build();
    }

}