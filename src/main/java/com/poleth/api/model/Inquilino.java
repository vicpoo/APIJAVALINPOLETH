// Inquilino.java
package com.poleth.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "inquilinos")
public class Inquilino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inquilino")
    private Integer idInquilino;

    @Column(name = "nombre_inquilino", nullable = false, length = 100)
    private String nombreInquilino;

    @Column(name = "telefono_inquilino", length = 20)
    private String telefonoInquilino;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "INE", nullable = false, unique = true, length = 50)
    private String ine;

    // Constructor por defecto
    public Inquilino() {
    }

    // Constructor con parámetros
    public Inquilino(String nombreInquilino, String telefonoInquilino, String email, String ine) {
        this.nombreInquilino = nombreInquilino;
        this.telefonoInquilino = telefonoInquilino;
        this.email = email;
        this.ine = ine;
    }

    // Getters y Setters
    public Integer getIdInquilino() {
        return idInquilino;
    }

    public void setIdInquilino(Integer idInquilino) {
        this.idInquilino = idInquilino;
    }

    public String getNombreInquilino() {
        return nombreInquilino;
    }

    public void setNombreInquilino(String nombreInquilino) {
        this.nombreInquilino = nombreInquilino;
    }

    public String getTelefonoInquilino() {
        return telefonoInquilino;
    }

    public void setTelefonoInquilino(String telefonoInquilino) {
        this.telefonoInquilino = telefonoInquilino;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIne() {
        return ine;
    }

    public void setIne(String ine) {
        this.ine = ine;
    }

    // toString para debugging
    @Override
    public String toString() {
        return "Inquilino{" +
                "idInquilino=" + idInquilino +
                ", nombreInquilino='" + nombreInquilino + '\'' +
                ", telefonoInquilino='" + telefonoInquilino + '\'' +
                ", email='" + email + '\'' +
                ", ine='" + ine + '\'' +
                '}';
    }
}