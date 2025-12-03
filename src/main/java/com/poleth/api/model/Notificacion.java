// Notificacion.java
package com.poleth.api.model;

import jakarta.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Integer idNotificacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_inquilino", referencedColumnName = "id_usuario", nullable = false)
    private Usuario inquilino; // Cambiado de Inquilino a Usuario

    @Column(name = "id_contrato", nullable = false)
    private Integer idContrato;

    @Column(name = "fecha_utilizacion")
    private Date fechaUtilizacion;

    @Column(name = "tipo_notificacion", length = 50)
    private String tipoNotificacion;

    @Column(name = "detalles", columnDefinition = "TEXT")
    private String detalles;

    @Column(name = "estado_notificacion", length = 20)
    private String estadoNotificacion = "no_leido";

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    // Constructor por defecto
    public Notificacion() {
        this.createdAt = Timestamp.valueOf(LocalDateTime.now());
    }

    // Constructor con parámetros
    public Notificacion(Usuario inquilino, Integer idContrato, Date fechaUtilizacion,
                        String tipoNotificacion, String detalles) {
        this();
        this.inquilino = inquilino;
        this.idContrato = idContrato;
        this.fechaUtilizacion = fechaUtilizacion;
        this.tipoNotificacion = tipoNotificacion;
        this.detalles = detalles;
    }

    // Constructor completo
    public Notificacion(Usuario inquilino, Integer idContrato, Date fechaUtilizacion,
                        String tipoNotificacion, String detalles, String estadoNotificacion) {
        this(inquilino, idContrato, fechaUtilizacion, tipoNotificacion, detalles);
        this.estadoNotificacion = estadoNotificacion != null ? estadoNotificacion : "no_leido";
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Timestamp.valueOf(LocalDateTime.now());
        }
        if (estadoNotificacion == null) {
            estadoNotificacion = "no_leido";
        }
    }

    // Getters y Setters
    public Integer getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(Integer idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public Usuario getInquilino() {
        return inquilino;
    }

    public void setInquilino(Usuario inquilino) {
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

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public String getEstadoNotificacion() {
        return estadoNotificacion;
    }

    public void setEstadoNotificacion(String estadoNotificacion) {
        this.estadoNotificacion = estadoNotificacion;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // Métodos utilitarios
    public boolean tieneInquilino() {
        return inquilino != null;
    }

    public boolean tieneContrato() {
        return idContrato != null;
    }

    public boolean esEstadoLeido() {
        return "leido".equalsIgnoreCase(estadoNotificacion);
    }

    public boolean esEstadoNoLeido() {
        return "no_leido".equalsIgnoreCase(estadoNotificacion);
    }

    public void marcarComoLeido() {
        this.estadoNotificacion = "leido";
    }

    public void marcarComoNoLeido() {
        this.estadoNotificacion = "no_leido";
    }

    public boolean esFechaUtilizacionPasada() {
        if (fechaUtilizacion == null) {
            return false;
        }
        LocalDate hoy = LocalDate.now();
        return fechaUtilizacion.toLocalDate().isBefore(hoy);
    }

    public boolean esFechaUtilizacionFutura() {
        if (fechaUtilizacion == null) {
            return false;
        }
        LocalDate hoy = LocalDate.now();
        return fechaUtilizacion.toLocalDate().isAfter(hoy);
    }

    public boolean esFechaUtilizacionHoy() {
        if (fechaUtilizacion == null) {
            return false;
        }
        LocalDate hoy = LocalDate.now();
        return fechaUtilizacion.toLocalDate().isEqual(hoy);
    }

    // Método para obtener el ID del inquilino de forma segura
    public Integer getIdInquilino() {
        return tieneInquilino() ? inquilino.getIdUsuario() : null;
    }

    // toString para debugging
    @Override
    public String toString() {
        return "Notificacion{" +
                "idNotificacion=" + idNotificacion +
                ", inquilino=" + (tieneInquilino() ? inquilino.getIdUsuario() : "null") +
                ", idContrato=" + idContrato +
                ", fechaUtilizacion=" + fechaUtilizacion +
                ", tipoNotificacion='" + tipoNotificacion + '\'' +
                ", estadoNotificacion='" + estadoNotificacion + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}