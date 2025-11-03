// Propietario.java
package com.poleth.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "propietario")
public class Propietario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_propietario")
    private Integer idPropietario;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "gmail", length = 100)
    private String gmail;

    // Constructor por defecto
    public Propietario() {
    }

    // Constructor con parámetros
    public Propietario(String nombre, String gmail) {
        this.nombre = nombre;
        this.gmail = gmail;
    }

    // Getters y Setters
    public Integer getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(Integer idPropietario) {
        this.idPropietario = idPropietario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    // toString para debugging
    @Override
    public String toString() {
        return "Propietario{" +
                "idPropietario=" + idPropietario +
                ", nombre='" + nombre + '\'' +
                ", gmail='" + gmail + '\'' +
                '}';
    }
}