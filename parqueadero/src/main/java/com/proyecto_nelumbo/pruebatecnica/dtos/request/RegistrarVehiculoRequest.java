package com.proyecto_nelumbo.pruebatecnica.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegistrarVehiculoRequest(
        @NotBlank(message = "La placa no puede estar vacía")
        @Pattern(
                regexp = "^[A-Za-z0-9&&[^ñÑ]]{6}$",
                message = "La placa debe contener exactamente 6 caracteres alfanuméricos sin caracteres especiales ni la letra ñ"
        )
        String placa
) {}