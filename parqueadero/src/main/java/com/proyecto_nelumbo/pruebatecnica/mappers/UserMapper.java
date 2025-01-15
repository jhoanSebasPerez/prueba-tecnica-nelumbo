package com.proyecto_nelumbo.pruebatecnica.mappers;

import com.proyecto_nelumbo.pruebatecnica.dtos.request.CreateSocioRequest;
import com.proyecto_nelumbo.pruebatecnica.dtos.response.UserResponse;
import com.proyecto_nelumbo.pruebatecnica.entities.Usuario;

public class UserMapper {

    public static Usuario toEntity(CreateSocioRequest request, String encodedPassword, String role) {
        return new Usuario(
                request.email(),
                request.nombre(),
                encodedPassword,
                role
        );
    }

    public static UserResponse toResponse(Usuario usuario) {
        return new UserResponse(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getRole()
        );
    }
}