package com.proyecto_nelumbo.pruebatecnica.repositories;

import com.proyecto_nelumbo.pruebatecnica.entities.Usuario;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);

    List<Usuario> findByRole(String role);
}
