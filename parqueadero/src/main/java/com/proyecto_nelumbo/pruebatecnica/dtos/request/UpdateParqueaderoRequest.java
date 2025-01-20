package com.proyecto_nelumbo.pruebatecnica.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record UpdateParqueaderoRequest(
        @NotBlank(message = "El nombre no puede estar vacío")
        String nombre,

        @NotBlank(message = "La dirección no puede estar vacía")
        String direccion,

        @Min(value = 0, message = "El costo de la tarifa por hora debe ser mayor o igual a 0")
        BigDecimal costoTarifaHora,

        @Min(value = 1, message = "La capacidad debe ser un valor positivo")
        Integer capacidad
) {}