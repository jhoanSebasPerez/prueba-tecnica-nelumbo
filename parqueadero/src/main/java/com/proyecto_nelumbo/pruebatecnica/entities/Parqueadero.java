package com.proyecto_nelumbo.pruebatecnica.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "parqueaderos")
public class Parqueadero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String direccion;

    private BigDecimal costoTarifaHora;

    private Integer capacidad;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "socio_id", nullable = true)
    private Usuario socio;

    @OneToMany(mappedBy = "parqueadero", orphanRemoval = true)
    private List<Registro> registros;

    public Parqueadero() {
    }

    public Parqueadero(String nombre, String direccion, BigDecimal costoTarifaHora, Usuario socio, Integer capacidad) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.costoTarifaHora = costoTarifaHora;
        this.socio = socio;
        this.capacidad = capacidad;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public BigDecimal getCostoTarifaHora() {
        return costoTarifaHora;
    }

    public void setCostoTarifaHora(BigDecimal costoTarifaHora) {
        this.costoTarifaHora = costoTarifaHora;
    }

    public Usuario getSocio() {
        return socio;
    }

    public void setSocio(Usuario socio) {
        this.socio = socio;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public List<Registro> getRegistros() {
        return registros;
    }

    public void setRegistros(List<Registro> registros) {
        this.registros = registros;
    }
}