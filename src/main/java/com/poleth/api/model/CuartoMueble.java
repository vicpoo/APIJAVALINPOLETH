// CuartoMueble.java
package com.poleth.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Cuarto_Mueble")
public class CuartoMueble {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuarto_mueble")
    private Integer idCuartoMueble;

    @Column(name = "id_cuarto", nullable = false)
    private Integer idCuarto;

    @Column(name = "id_catalogo_mueble", nullable = false)
    private Integer idCatalogoMueble;

    @Column(name = "cantidad")
    private Integer cantidad = 0;

    @Column(name = "estado", length = 50)
    private String estado = "bueno";

    // Constructor por defecto
    public CuartoMueble() {
    }

    // Constructor con parámetros básicos
    public CuartoMueble(Integer idCuarto, Integer idCatalogoMueble, Integer cantidad, String estado) {
        this.idCuarto = idCuarto;
        this.idCatalogoMueble = idCatalogoMueble;
        this.cantidad = cantidad;
        this.estado = estado;
    }

    // Constructor sin estado
    public CuartoMueble(Integer idCuarto, Integer idCatalogoMueble, Integer cantidad) {
        this.idCuarto = idCuarto;
        this.idCatalogoMueble = idCatalogoMueble;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public Integer getIdCuartoMueble() {
        return idCuartoMueble;
    }

    public void setIdCuartoMueble(Integer idCuartoMueble) {
        this.idCuartoMueble = idCuartoMueble;
    }

    public Integer getIdCuarto() {
        return idCuarto;
    }

    public void setIdCuarto(Integer idCuarto) {
        this.idCuarto = idCuarto;
    }

    public Integer getIdCatalogoMueble() {
        return idCatalogoMueble;
    }

    public void setIdCatalogoMueble(Integer idCatalogoMueble) {
        this.idCatalogoMueble = idCatalogoMueble;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Métodos utilitarios
    public void incrementarCantidad(int incremento) {
        this.cantidad += incremento;
    }

    public void decrementarCantidad(int decremento) {
        this.cantidad = Math.max(0, this.cantidad - decremento);
    }

    public boolean tieneStock() {
        return cantidad != null && cantidad > 0;
    }

    public boolean necesitaReposicion() {
        return cantidad != null && cantidad == 0;
    }

    public boolean estaEnBuenEstado() {
        return "bueno".equalsIgnoreCase(estado);
    }

    // toString para debugging
    @Override
    public String toString() {
        return "CuartoMueble{" +
                "idCuartoMueble=" + idCuartoMueble +
                ", idCuarto=" + idCuarto +
                ", idCatalogoMueble=" + idCatalogoMueble +
                ", cantidad=" + cantidad +
                ", estado='" + estado + '\'' +
                '}';
    }
}