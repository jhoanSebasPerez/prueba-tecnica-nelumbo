package com.proyecto_nelumbo.pruebatecnica.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String nombre;
    private String password;
    private String role;

    @OneToMany(mappedBy = "socio", fetch = FetchType.LAZY)
    private Set<Parqueadero> parqueaderos = new HashSet<>();


    public Usuario() {
    }

    public Usuario(String email, String nombre, String password, String role) {
        this.email = email;
        this.nombre = nombre;
        this.password = password;
        this.role = role;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Long getId() {
        return id;
    }

    public Set<Parqueadero> getParqueaderos() {
        return parqueaderos;
    }

    //setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
