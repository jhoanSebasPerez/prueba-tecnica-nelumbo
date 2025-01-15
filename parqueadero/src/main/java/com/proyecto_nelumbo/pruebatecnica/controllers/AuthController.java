package com.proyecto_nelumbo.pruebatecnica.controllers;

import com.proyecto_nelumbo.pruebatecnica.dtos.request.LoginRequest;
import com.proyecto_nelumbo.pruebatecnica.dtos.response.LoginResponse;
import com.proyecto_nelumbo.pruebatecnica.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}