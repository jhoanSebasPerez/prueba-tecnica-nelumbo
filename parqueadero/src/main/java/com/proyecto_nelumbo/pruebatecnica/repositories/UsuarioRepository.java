package com.proyecto_nelumbo.pruebatecnica.repositories;

import com.proyecto_nelumbo.pruebatecnica.entities.Usuario;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);
}
