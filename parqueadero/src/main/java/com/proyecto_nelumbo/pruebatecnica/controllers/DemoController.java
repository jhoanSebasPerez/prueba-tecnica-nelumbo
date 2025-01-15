package com.proyecto_nelumbo.pruebatecnica.controllers;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/demo")
public class DemoController {

    // Ruta pública (sin seguridad) -> si está configurada en SecurityConfig
    @GetMapping("/public")
    public String publico() {
        return "Endpoint público, sin autenticación";
    }

    // Ruta protegida: solo usuarios autenticados
    @GetMapping("/user")
    public String soloUsuariosAutenticados() {
        return "Endpoint para cualquier usuario autenticado";
    }

    // Ruta protegida: SOLO ADMIN
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String soloAdmin() {
        return "Endpoint solo para ADMIN";
    }

    // Ruta protegida: SOLO SOCIO
    @GetMapping("/socio")
    @PreAuthorize("hasRole('SOCIO')")
    public String soloSocio() {
        return "Endpoint solo para SOCIO";
    }
}