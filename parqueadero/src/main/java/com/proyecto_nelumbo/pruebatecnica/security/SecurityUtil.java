package com.proyecto_nelumbo.pruebatecnica.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {
    public Long obtenerUserIdDesdeContext() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getUserId();
        }
        throw new IllegalStateException("No se pudo determinar la identidad del usuario. Por favor, asegúrate de haber iniciado sesión correctamente.");
    }
}