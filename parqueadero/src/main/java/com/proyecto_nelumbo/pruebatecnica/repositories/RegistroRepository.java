package com.proyecto_nelumbo.pruebatecnica.repositories;

import com.proyecto_nelumbo.pruebatecnica.entities.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroRepository extends JpaRepository<Registro, Long> {

    boolean existsByPlaca(String placa);

    List<Registro> findByParqueaderoId(Long parqueaderoId);

    Optional<Registro> findByPlaca(String placa);
}