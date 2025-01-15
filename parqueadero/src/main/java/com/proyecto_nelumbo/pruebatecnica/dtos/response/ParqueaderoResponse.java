package com.proyecto_nelumbo.pruebatecnica.dtos.response;

import java.math.BigDecimal;

public record ParqueaderoResponse(
        Long id,
        String nombre,
        String direccion,
        BigDecimal costoTarifaHora,
        Long socioId,
        Integer capacidad
) {}