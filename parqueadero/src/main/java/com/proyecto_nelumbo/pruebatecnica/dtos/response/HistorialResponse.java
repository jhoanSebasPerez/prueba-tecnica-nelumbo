package com.proyecto_nelumbo.pruebatecnica.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record HistorialResponse(
        Long id,
        String placa,
        Long parqueaderoId,
        LocalDateTime fechaHoraIngreso,
        LocalDateTime fechaHoraSalida,
        BigDecimal valorCalculado
) {}