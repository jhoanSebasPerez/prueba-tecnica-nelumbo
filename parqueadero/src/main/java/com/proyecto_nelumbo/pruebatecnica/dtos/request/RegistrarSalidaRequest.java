package com.proyecto_nelumbo.pruebatecnica.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record RegistrarSalidaRequest(
        @NotBlank(message = "La placa no puede estar vacía")
        String placa
) {}