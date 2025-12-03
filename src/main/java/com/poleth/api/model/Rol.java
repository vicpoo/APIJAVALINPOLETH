// Rol.java
package com.poleth.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_roles") // Cambiado para coincidir con la BD
    private Integer idRoles; // Cambiado el nombre del campo

    @Column(name = "titulo", nullable = false, length = 50) // Cambiado a "titulo"
    private String titulo; // Cambiado el nombre del campo

    // Constructor por defecto
    public Rol() {
    }

    // Constructor con parámetros
    public Rol(String titulo) {
        this.titulo = titulo;
    }

    // Getters y Setters
    public Integer getIdRoles() {
        return idRoles;
    }

    public void setIdRoles(Integer idRoles) {
        this.idRoles = idRoles;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    // toString para debugging
    @Override
    public String toString() {
        return "Rol{" +
                "idRoles=" + idRoles +
                ", titulo='" + titulo + '\'' +
                '}';
    }
}