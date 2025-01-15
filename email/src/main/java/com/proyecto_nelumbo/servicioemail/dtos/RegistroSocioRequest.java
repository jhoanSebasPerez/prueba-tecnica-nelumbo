package com.proyecto_nelumbo.servicioemail.dtos;

public record RegistroSocioRequest(
        String email,
        String mensaje
) {}