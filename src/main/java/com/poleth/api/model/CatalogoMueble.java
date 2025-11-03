// CatalogoMueble.java
package com.poleth.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Catalogo_Muebles")
public class CatalogoMueble {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catalogo_mueble")
    private Integer idCatalogoMueble;

    @Column(name = "nombre_mueble", nullable = false, unique = true, length = 100)
    private String nombreMueble;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    // Constructor por defecto
    public CatalogoMueble() {
    }

    // Constructor con parámetros básicos
    public CatalogoMueble(String nombreMueble) {
        this.nombreMueble = nombreMueble;
    }

    // Constructor completo
    public CatalogoMueble(String nombreMueble, String descripcion) {
        this.nombreMueble = nombreMueble;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public Integer getIdCatalogoMueble() {
        return idCatalogoMueble;
    }

    public void setIdCatalogoMueble(Integer idCatalogoMueble) {
        this.idCatalogoMueble = idCatalogoMueble;
    }

    public String getNombreMueble() {
        return nombreMueble;
    }

    public void setNombreMueble(String nombreMueble) {
        this.nombreMueble = nombreMueble;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // toString para debugging
    @Override
    public String toString() {
        return "CatalogoMueble{" +
                "idCatalogoMueble=" + idCatalogoMueble +
                ", nombreMueble='" + nombreMueble + '\'' +
                ", descripcion='" + (descripcion != null ? descripcion.substring(0, Math.min(50, descripcion.length())) + "..." : "null") + '\'' +
                '}';
    }
}