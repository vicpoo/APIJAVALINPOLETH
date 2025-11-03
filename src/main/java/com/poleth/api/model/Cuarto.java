// Cuarto.java
package com.poleth.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Cuartos")
public class Cuarto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuarto")
    private Integer idCuarto;

    @Column(name = "id_propietario", nullable = false)
    private Integer idPropietario;

    @Column(name = "nombre_cuarto", nullable = false, length = 100)
    private String nombreCuarto;

    @Column(name = "precio_alquiler", precision = 10, scale = 2)
    private BigDecimal precioAlquiler;

    @Column(name = "estado_cuarto", length = 50)
    private String estadoCuarto;

    @Column(name = "descripcion_cuarto", columnDefinition = "TEXT")
    private String descripcionCuarto;

    // Relación Many-to-One con Propietario (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_propietario", insertable = false, updatable = false)
    private Propietario propietario;

    // Constructor por defecto
    public Cuarto() {
    }

    // Constructor con parámetros básicos
    public Cuarto(Integer idPropietario, String nombreCuarto) {
        this.idPropietario = idPropietario;
        this.nombreCuarto = nombreCuarto;
    }

    // Constructor completo
    public Cuarto(Integer idPropietario, String nombreCuarto, BigDecimal precioAlquiler, 
                  String estadoCuarto, String descripcionCuarto) {
        this.idPropietario = idPropietario;
        this.nombreCuarto = nombreCuarto;
        this.precioAlquiler = precioAlquiler;
        this.estadoCuarto = estadoCuarto;
        this.descripcionCuarto = descripcionCuarto;
    }

    // Getters y Setters
    public Integer getIdCuarto() {
        return idCuarto;
    }

    public void setIdCuarto(Integer idCuarto) {
        this.idCuarto = idCuarto;
    }

    public Integer getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(Integer idPropietario) {
        this.idPropietario = idPropietario;
    }

    public String getNombreCuarto() {
        return nombreCuarto;
    }

    public void setNombreCuarto(String nombreCuarto) {
        this.nombreCuarto = nombreCuarto;
    }

    public BigDecimal getPrecioAlquiler() {
        return precioAlquiler;
    }

    public void setPrecioAlquiler(BigDecimal precioAlquiler) {
        this.precioAlquiler = precioAlquiler;
    }

    public String getEstadoCuarto() {
        return estadoCuarto;
    }

    public void setEstadoCuarto(String estadoCuarto) {
        this.estadoCuarto = estadoCuarto;
    }

    public String getDescripcionCuarto() {
        return descripcionCuarto;
    }

    public void setDescripcionCuarto(String descripcionCuarto) {
        this.descripcionCuarto = descripcionCuarto;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    // toString para debugging
    @Override
    public String toString() {
        return "Cuarto{" +
                "idCuarto=" + idCuarto +
                ", idPropietario=" + idPropietario +
                ", nombreCuarto='" + nombreCuarto + '\'' +
                ", precioAlquiler=" + precioAlquiler +
                ", estadoCuarto='" + estadoCuarto + '\'' +
                ", descripcionCuarto='" + (descripcionCuarto != null ? descripcionCuarto.substring(0, Math.min(50, descripcionCuarto.length())) + "..." : "null") + '\'' +
                '}';
    }
}