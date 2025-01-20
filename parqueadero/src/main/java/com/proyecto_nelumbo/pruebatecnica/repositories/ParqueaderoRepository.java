package com.proyecto_nelumbo.pruebatecnica.repositories;

import com.proyecto_nelumbo.pruebatecnica.entities.Parqueadero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParqueaderoRepository extends JpaRepository<Parqueadero, Long> {

    List<Parqueadero> findBySocioId(Long socioId);

    @Query("SELECT p FROM Parqueadero p WHERE p.habilitado = true")
    List<Parqueadero> findAllHabilitados();

    @Query("SELECT p FROM Parqueadero p WHERE p.id = :id AND p.habilitado = true")
    Optional<Parqueadero> findByIdHabilitado(@Param("id") Long id);
}