package com.proyecto_nelumbo.pruebatecnica.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "registros")
public class Registro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Placa del vehículo (se almacena como texto directamente)
    @Column(nullable = false)
    private String placa;

    // Relación ManyToOne con Parqueadero (un parqueadero puede tener muchos registros)
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "parqueadero_id", nullable = false)
    private Parqueadero parqueadero;

    // Fecha y hora en la cual se realizó el ingreso
    @Column(name = "fecha_hora_ingreso", nullable = false)
    private LocalDateTime fechaHoraIngreso;

    public Registro() {
    }

    public Registro(String placa, Parqueadero parqueadero, LocalDateTime fechaHoraIngreso) {
        this.placa = placa;
        this.parqueadero = parqueadero;
        this.fechaHoraIngreso = fechaHoraIngreso;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Parqueadero getParqueadero() {
        return parqueadero;
    }

    public void setParqueadero(Parqueadero parqueadero) {
        this.parqueadero = parqueadero;
    }

    public LocalDateTime getFechaHoraIngreso() {
        return fechaHoraIngreso;
    }

    public void setFechaHoraIngreso(LocalDateTime fechaHoraIngreso) {
        this.fechaHoraIngreso = fechaHoraIngreso;
    }
}