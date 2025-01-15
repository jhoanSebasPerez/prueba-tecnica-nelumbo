package com.proyecto_nelumbo.pruebatecnica.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSocioRequest(
        @NotBlank(message = "El email no puede estar vacío")
        @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(min = 4, message = "El nombre debe tener mínimo 4 caracteres")
        String nombre,

        @NotBlank(message = "La contraseña no puede estar vacía")
        @Size(min = 4, message = "La contraseña debe tener mínimo 4 caracteres")
        String password
) {}