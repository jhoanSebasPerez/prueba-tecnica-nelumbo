package com.proyecto_nelumbo.pruebatecnica.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUsuarioRequest(

        @NotBlank(message = "El nombre no puede estar vacío")
        String nombre,

        @Email(message = "El email debe ser válido")
        @NotBlank(message = "El email no puede estar vacío")
        String email
) {}