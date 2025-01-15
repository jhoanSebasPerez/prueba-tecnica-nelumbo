package com.proyecto_nelumbo.pruebatecnica.repositories;

import com.proyecto_nelumbo.pruebatecnica.dtos.response.TopVehiculoResponse;
import com.proyecto_nelumbo.pruebatecnica.entities.Historial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistorialRepository extends JpaRepository<Historial, Long> {

    @Query("SELECT new com.proyecto_nelumbo.pruebatecnica.dtos.response.TopVehiculoResponse(h.placa, COUNT(h)) " +
            "FROM Historial h " +
            "GROUP BY h.placa " +
            "ORDER BY COUNT(h) DESC")
    List<TopVehiculoResponse> findTop10Vehiculos();

    @Query("SELECT new com.proyecto_nelumbo.pruebatecnica.dtos.response.TopVehiculoResponse(h.placa, COUNT(h)) " +
            "FROM Historial h " +
            "WHERE h.parqueadero.id = :parqueaderoId " +
            "GROUP BY h.placa " +
            "ORDER BY COUNT(h) DESC")
    List<TopVehiculoResponse> findTop10VehiculosByParqueaderoId(Long parqueaderoId);

    @Query("SELECT COUNT(h) > 0 " +
            "FROM Historial h " +
            "WHERE h.parqueadero.id = :parqueaderoId AND h.placa = :placa")
    boolean existeRegistroPrevio(Long parqueaderoId, String placa);

    @Query(value = "SELECT SUM(h.valor_calculado) " +
            "FROM historiales h " +
            "WHERE h.parqueadero_id = :parqueaderoId AND h.fecha_hora_salida BETWEEN :fechaInicio AND :fechaFin",
            nativeQuery = true)
    Double obtenerGanancias(Long parqueaderoId, LocalDateTime fechaInicio, LocalDateTime fechaFin);

    @Query(value = "SELECT u.nombre AS socioNombre, COUNT(h.id) AS cantidadVehiculos " +
            "FROM historiales h " +
            "JOIN parqueaderos p ON h.parqueadero_id = p.id " +
            "JOIN usuarios u ON p.socio_id = u.id " +
            "WHERE DATE_TRUNC('week', h.fecha_hora_ingreso) = DATE_TRUNC('week', CURRENT_DATE) " +
            "GROUP BY u.nombre " +
            "ORDER BY COUNT(h.id) DESC " +
            "LIMIT 3",
            nativeQuery = true)
    List<Object[]> obtenerTop3SociosConMasIngresosSemana();

    @Query(value = "SELECT p.nombre AS parqueaderoNombre, SUM(h.valor_calculado) AS gananciaTotal " +
            "FROM historiales h " +
            "JOIN parqueaderos p ON h.parqueadero_id = p.id " +
            "WHERE DATE_TRUNC('week', h.fecha_hora_salida) = DATE_TRUNC('week', CURRENT_DATE) " +
            "GROUP BY p.nombre " +
            "ORDER BY SUM(h.valor_calculado) DESC " +
            "LIMIT 3",
            nativeQuery = true)
    List<Object[]> obtenerTop3ParqueaderosConMayorGananciaSemana();
}