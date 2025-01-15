package com.proyecto_nelumbo.pruebatecnica.mappers;

import com.proyecto_nelumbo.pruebatecnica.dtos.request.RegistrarVehiculoRequest;
import com.proyecto_nelumbo.pruebatecnica.dtos.response.RegistroResponse;
import com.proyecto_nelumbo.pruebatecnica.entities.Parqueadero;
import com.proyecto_nelumbo.pruebatecnica.entities.Registro;

import java.time.LocalDateTime;

public class RegistroMapper {

    public static Registro toEntity(RegistrarVehiculoRequest request,
                                    Parqueadero parqueadero,
                                    LocalDateTime fechaHoraIngreso) {
        return new Registro(
                request.placa(),
                parqueadero,
                fechaHoraIngreso
        );
    }


    public static RegistroResponse toResponse(Registro registro) {
        return new RegistroResponse(
                registro.getId(),
                registro.getPlaca(),
                registro.getParqueadero().getId(),
                registro.getFechaHoraIngreso()
        );
    }
}