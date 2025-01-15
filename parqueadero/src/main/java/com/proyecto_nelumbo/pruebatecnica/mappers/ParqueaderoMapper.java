package com.proyecto_nelumbo.pruebatecnica.mappers;

import com.proyecto_nelumbo.pruebatecnica.dtos.request.CreateParqueaderoRequest;
import com.proyecto_nelumbo.pruebatecnica.dtos.response.ParqueaderoResponse;
import com.proyecto_nelumbo.pruebatecnica.entities.Parqueadero;
import com.proyecto_nelumbo.pruebatecnica.entities.Usuario;

public class ParqueaderoMapper {

    public static Parqueadero toEntity(CreateParqueaderoRequest request, Usuario socio) {
        return new Parqueadero(
                request.nombre(),
                request.direccion(),
                request.costoTarifaHora(),
                socio,
                request.capacidad()
        );
    }

    public static ParqueaderoResponse toResponse(Parqueadero parqueadero) {
        Long socioId = parqueadero.getSocio() != null
                ? parqueadero.getSocio().getId()
                : null;

        return new ParqueaderoResponse(
                parqueadero.getId(),
                parqueadero.getNombre(),
                parqueadero.getDireccion(),
                parqueadero.getCostoTarifaHora(),
                socioId,
                parqueadero.getCapacidad()
        );
    }
}