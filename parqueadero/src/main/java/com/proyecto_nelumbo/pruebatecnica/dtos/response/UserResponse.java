package com.proyecto_nelumbo.pruebatecnica.dtos.response;

public record UserResponse(
        Long id,
        String email,
        String nombre,
        String role
) {}