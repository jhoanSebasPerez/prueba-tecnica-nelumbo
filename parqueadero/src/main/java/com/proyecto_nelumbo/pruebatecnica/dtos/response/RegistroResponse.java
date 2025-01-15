package com.proyecto_nelumbo.pruebatecnica.dtos.response;

import java.time.LocalDateTime;

public record RegistroResponse(
        Long id,
        String placa,
        Long parqueaderoId,
        LocalDateTime fechaHoraIngreso
) {}