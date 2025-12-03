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

    @Column(name = "nombre_mueble", nullable = false, length = 100)
    private String nombreMueble;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "estado_mueble", length = 20)
    private String estadoMueble;

    // Constructor por defecto
    public CatalogoMueble() {
        this.estadoMueble = "activo";
    }

    // Constructor con parámetros básicos
    public CatalogoMueble(String nombreMueble) {
        this();
        this.nombreMueble = nombreMueble;
    }

    // Constructor completo
    public CatalogoMueble(String nombreMueble, String descripcion, String estadoMueble) {
        this.nombreMueble = nombreMueble;
        this.descripcion = descripcion;
        this.estadoMueble = estadoMueble != null ? estadoMueble : "activo";
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

    public String getEstadoMueble() {
        return estadoMueble;
    }

    public void setEstadoMueble(String estadoMueble) {
        this.estadoMueble = estadoMueble;
    }

    // toString para debugging
    @Override
    public String toString() {
        return "CatalogoMueble{" +
                "idCatalogoMueble=" + idCatalogoMueble +
                ", nombreMueble='" + nombreMueble + '\'' +
                ", descripcion='" + (descripcion != null ? (descripcion.length() > 50 ? descripcion.substring(0, 50) + "..." : descripcion) : "null") + '\'' +
                ", estadoMueble='" + estadoMueble + '\'' +
                '}';
    }
}