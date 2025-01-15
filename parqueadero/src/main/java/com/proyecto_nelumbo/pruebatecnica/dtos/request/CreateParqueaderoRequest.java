package com.proyecto_nelumbo.pruebatecnica.dtos.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateParqueaderoRequest(
        @NotBlank(message = "El nombre no puede estar vacío")
        String nombre,

        @NotBlank(message = "La dirección no puede estar vacía")
        String direccion,

        @NotNull(message = "El costoTarifaHora no puede ser nulo")
        @PositiveOrZero(message = "El costoTarifaHora debe ser >= 0")
        BigDecimal costoTarifaHora,

        @NotNull(message = "La capacidad no puede ser nula")
        @Positive(message = "La capacidad debe ser > 0")
        Integer capacidad,

        Long socioId
) {}