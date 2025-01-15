package com.proyecto_nelumbo.pruebatecnica.mappers;

import com.proyecto_nelumbo.pruebatecnica.dtos.response.HistorialResponse;
import com.proyecto_nelumbo.pruebatecnica.entities.Historial;

public class HistorialMapper {

    public static HistorialResponse toResponse(Historial historial) {
        Long parqueaderoId = historial.getParqueadero() != null ? historial.getParqueadero().getId() : null;

        return new HistorialResponse(
                historial.getId(),
                historial.getPlaca(),
                parqueaderoId,
                historial.getFechaHoraIngreso(),
                historial.getFechaHoraSalida(),
                historial.getValorCalculado()
        );
    }
}