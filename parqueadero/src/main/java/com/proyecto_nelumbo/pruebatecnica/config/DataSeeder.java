package com.proyecto_nelumbo.pruebatecnica.config;

import com.proyecto_nelumbo.pruebatecnica.entities.*;
import com.proyecto_nelumbo.pruebatecnica.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataSeeder {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private final PasswordEncoder passwordEncoder;

    public DataSeeder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner seedData(
            UsuarioRepository usuarioRepository,
            ParqueaderoRepository parqueaderoRepository,
            HistorialRepository historialRepository) {
        return args -> {
            // Crear usuario administrador
            Usuario admin = new Usuario("admin@mail.com", "Admin", passwordEncoder.encode("admin"), "ADMIN");
            usuarioRepository.save(admin);
            logger.info("Usuario administrador insertado.");

            // Crear usuarios (socios)
            Usuario socio1 = new Usuario("socio1@email.com", "Socio 1", passwordEncoder.encode("socio1"), "SOCIO");
            Usuario socio2 = new Usuario("socio2@email.com", "Socio 2", passwordEncoder.encode("socio2"), "SOCIO");
            Usuario socio3 = new Usuario("socio3@email.com", "Socio 3", passwordEncoder.encode("socio3"), "SOCIO");
            usuarioRepository.saveAll(List.of(socio1, socio2, socio3));

            logger.info("Usuarios (socios) insertados.");

            // Guardar parqueaderos uno por uno para asegurarse de que están gestionados por el contexto de persistencia
            Parqueadero parqueadero1 = parqueaderoRepository.save(new Parqueadero("Parqueadero Centro", "Calle 123", BigDecimal.valueOf(5.0), socio1, 50));
            Parqueadero parqueadero2 = parqueaderoRepository.save(new Parqueadero("Parqueadero Norte", "Avenida 45", BigDecimal.valueOf(4.5), socio1, 40));
            Parqueadero parqueadero3 = parqueaderoRepository.save(new Parqueadero("Parqueadero Sur", "Carrera 67", BigDecimal.valueOf(6.0), socio2, 60));
            Parqueadero parqueadero4 = parqueaderoRepository.save(new Parqueadero("Parqueadero Oeste", "Diagonal 89", BigDecimal.valueOf(4.0), socio3, 30));
            Parqueadero parqueadero5 = parqueaderoRepository.save(new Parqueadero("Parqueadero Este", "Calle 10", BigDecimal.valueOf(5.5), socio2, 35));
            Parqueadero parqueadero6 = parqueaderoRepository.save(new Parqueadero("Parqueadero Centro 2", "Avenida 20", BigDecimal.valueOf(4.8), socio3, 45));

            logger.info("Parqueaderos insertados.");

            // Crear historial de ingresos y salidas de vehículos
            List<Historial> historiales = List.of(
                    new Historial("ABC123", parqueadero1, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).plusHours(2), BigDecimal.valueOf(10)),
                    new Historial("DEF456", parqueadero1, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(2).plusHours(3), BigDecimal.valueOf(15)),
                    new Historial("GHI789", parqueadero2, LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(3).plusHours(4), BigDecimal.valueOf(20)),
                    new Historial("JKL012", parqueadero2, LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(5).plusHours(1), BigDecimal.valueOf(5)),
                    new Historial("MNO345", parqueadero3, LocalDateTime.now().minusDays(4), LocalDateTime.now().minusDays(4).plusHours(2), BigDecimal.valueOf(12)),
                    new Historial("PQR678", parqueadero3, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).plusHours(3), BigDecimal.valueOf(18)),
                    new Historial("STU901", parqueadero5, LocalDateTime.now().minusWeeks(1).minusDays(2), LocalDateTime.now().minusWeeks(1).minusDays(2).plusHours(2), BigDecimal.valueOf(11)),
                    new Historial("VWX234", parqueadero5, LocalDateTime.now().minusWeeks(1), LocalDateTime.now().minusWeeks(1).plusHours(4), BigDecimal.valueOf(22)),
                    new Historial("XYZ567", parqueadero4, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(2).plusHours(5), BigDecimal.valueOf(25)),
                    new Historial("LMN890", parqueadero6, LocalDateTime.now().minusDays(6), LocalDateTime.now().minusDays(6).plusHours(3), BigDecimal.valueOf(15)),
                    new Historial("OPQ123", parqueadero6, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).plusHours(2), BigDecimal.valueOf(9)),
                    new Historial("RST456", parqueadero4, LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(3).plusHours(4), BigDecimal.valueOf(20))
            );

            historialRepository.saveAll(historiales);
            logger.info("Historial de ingresos y salidas insertado.");

            logger.info("Datos semilla insertados correctamente.");
        };
    }
}