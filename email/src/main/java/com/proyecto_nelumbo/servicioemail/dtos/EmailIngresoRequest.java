package com.proyecto_nelumbo.servicioemail.dtos;

public record EmailIngresoRequest(
        String email,
        String placa,
        String mensaje,
        Long parqueaderoId
) {}