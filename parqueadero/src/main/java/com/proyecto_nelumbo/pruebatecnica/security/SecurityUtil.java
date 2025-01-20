package com.proyecto_nelumbo.pruebatecnica.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static Long obtenerUserIdDesdeContext() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getUserId();
        }
        throw new IllegalStateException("No se pudo determinar la identidad del usuario. Por favor, asegúrate de haber iniciado sesión correctamente.");
    }

    public static boolean usuarioEsAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }
}