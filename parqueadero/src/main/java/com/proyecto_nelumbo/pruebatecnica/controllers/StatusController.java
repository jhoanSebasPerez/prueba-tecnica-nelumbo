package com.proyecto_nelumbo.pruebatecnica.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/status")
public class StatusController {

    @GetMapping
    public Map<String, Object> getStatus() {
        return Map.of(
                "status", "OK",
                "message", "La aplicación está en ejecución :)",
                "timestamp", LocalDateTime.now()
        );
    }
}