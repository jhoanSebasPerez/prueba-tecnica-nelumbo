package com.proyecto_nelumbo.pruebatecnica.dtos.response;

import java.math.BigDecimal;

public record ParqueaderoDetalleResponse(
        Long id,
        String nombre,
        String direccion,
        BigDecimal costoTarifaHora,
        Integer capacidad,
        Long socioId,
        String socioNombre
) {}