// Notificacion.java
package com.poleth.api.model;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "notificaciones")
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Integer idNotificacion;

    @ManyToOne
    @JoinColumn(name = "id_inquilino")
    private Inquilino inquilino;

    @Column(name = "id_contrato")
    private Integer idContrato;

    @Column(name = "fecha_utilizacion")
    private Date fechaUtilizacion;

    @Column(name = "tipo_notificacion", length = 50)
    private String tipoNotificacion;

    // Constructor por defecto
    public Notificacion() {
    }

    // Constructor con parámetros básicos
    public Notificacion(Inquilino inquilino, Integer idContrato, Date fechaUtilizacion, String tipoNotificacion) {
        this.inquilino = inquilino;
        this.idContrato = idContrato;
        this.fechaUtilizacion = fechaUtilizacion;
        this.tipoNotificacion = tipoNotificacion;
    }

    // Constructor sin inquilino
    public Notificacion(Integer idContrato, Date fechaUtilizacion, String tipoNotificacion) {
        this.idContrato = idContrato;
        this.fechaUtilizacion = fechaUtilizacion;
        this.tipoNotificacion = tipoNotificacion;
    }

    // Constructor sin contrato
    public Notificacion(Inquilino inquilino, Date fechaUtilizacion, String tipoNotificacion) {
        this.inquilino = inquilino;
        this.fechaUtilizacion = fechaUtilizacion;
        this.tipoNotificacion = tipoNotificacion;
    }

    // Getters y Setters
    public Integer getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(Integer idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public Inquilino getInquilino() {
        return inquilino;
    }

    public void setInquilino(Inquilino inquilino) {
        this.inquilino = inquilino;
    }

    public Integer getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(Integer idContrato) {
        this.idContrato = idContrato;
    }

    public Date getFechaUtilizacion() {
        return fechaUtilizacion;
    }

    public void setFechaUtilizacion(Date fechaUtilizacion) {
        this.fechaUtilizacion = fechaUtilizacion;
    }

    public String getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(String tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    // Métodos utilitarios
    public boolean tieneInquilino() {
        return inquilino != null;
    }

    public boolean tieneContrato() {
        return idContrato != null;
    }

    public boolean esParaInquilino() {
        return tieneInquilino();
    }

    public boolean esParaContrato() {
        return tieneContrato();
    }

    public boolean esFechaUtilizacionPasada() {
        if (fechaUtilizacion == null) {
            return false;
        }
        Date hoy = new Date(System.currentTimeMillis());
        return fechaUtilizacion.before(hoy);
    }

    public boolean esFechaUtilizacionFutura() {
        if (fechaUtilizacion == null) {
            return false;
        }
        Date hoy = new Date(System.currentTimeMillis());
        return fechaUtilizacion.after(hoy);
    }

    public boolean esFechaUtilizacionHoy() {
        if (fechaUtilizacion == null) {
            return false;
        }
        Date hoy = new Date(System.currentTimeMillis());
        return fechaUtilizacion.equals(hoy);
    }

    // Método para obtener el ID del inquilino de forma segura
    public Integer getIdInquilino() {
        return tieneInquilino() ? inquilino.getIdInquilino() : null;
    }

    // toString para debugging
    @Override
    public String toString() {
        return "Notificacion{" +
                "idNotificacion=" + idNotificacion +
                ", inquilino=" + (tieneInquilino() ? inquilino.getIdInquilino() : "null") +
                ", idContrato=" + idContrato +
                ", fechaUtilizacion=" + fechaUtilizacion +
                ", tipoNotificacion='" + tipoNotificacion + '\'' +
                '}';
    }
}