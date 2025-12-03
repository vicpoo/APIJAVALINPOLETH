// Mantenimiento.java
package com.poleth.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Mantenimientos")
public class Mantenimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mantenimiento")
    private Integer idMantenimiento;

    @Column(name = "id_cuarto", nullable = false)
    private Integer idCuarto;

    @Column(name = "fecha_reporte", nullable = false)
    private LocalDate fechaReporte;

    @Column(name = "descripcion_problema", columnDefinition = "TEXT")
    private String descripcionProblema;

    @Column(name = "estado_mantenimiento", length = 50)
    private String estadoMantenimiento = "pendiente";

    @Column(name = "fecha_atencion")
    private LocalDate fechaAtencion;

    @Column(name = "costo_mantenimiento", precision = 10, scale = 2)
    private BigDecimal costoMantenimiento = BigDecimal.ZERO;

    // Constructor por defecto
    public Mantenimiento() {
    }

    // Constructor con parámetros básicos
    public Mantenimiento(Integer idCuarto, LocalDate fechaReporte, String descripcionProblema) {
        this.idCuarto = idCuarto;
        this.fechaReporte = fechaReporte;
        this.descripcionProblema = descripcionProblema;
    }

    // Constructor completo
    public Mantenimiento(Integer idCuarto, LocalDate fechaReporte, String descripcionProblema,
                         String estadoMantenimiento, LocalDate fechaAtencion, BigDecimal costoMantenimiento) {
        this.idCuarto = idCuarto;
        this.fechaReporte = fechaReporte;
        this.descripcionProblema = descripcionProblema;
        this.estadoMantenimiento = estadoMantenimiento;
        this.fechaAtencion = fechaAtencion;
        this.costoMantenimiento = costoMantenimiento;
    }

    // Getters y Setters
    public Integer getIdMantenimiento() {
        return idMantenimiento;
    }

    public void setIdMantenimiento(Integer idMantenimiento) {
        this.idMantenimiento = idMantenimiento;
    }

    public Integer getIdCuarto() {
        return idCuarto;
    }

    public void setIdCuarto(Integer idCuarto) {
        this.idCuarto = idCuarto;
    }

    public LocalDate getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(LocalDate fechaReporte) {
        this.fechaReporte = fechaReporte;
    }

    public String getDescripcionProblema() {
        return descripcionProblema;
    }

    public void setDescripcionProblema(String descripcionProblema) {
        this.descripcionProblema = descripcionProblema;
    }

    public String getEstadoMantenimiento() {
        return estadoMantenimiento;
    }

    public void setEstadoMantenimiento(String estadoMantenimiento) {
        this.estadoMantenimiento = estadoMantenimiento;
    }

    public LocalDate getFechaAtencion() {
        return fechaAtencion;
    }

    public void setFechaAtencion(LocalDate fechaAtencion) {
        this.fechaAtencion = fechaAtencion;
    }

    public BigDecimal getCostoMantenimiento() {
        return costoMantenimiento;
    }

    public void setCostoMantenimiento(BigDecimal costoMantenimiento) {
        this.costoMantenimiento = costoMantenimiento;
    }

    // Métodos utilitarios
    public boolean estaPendiente() {
        return "pendiente".equalsIgnoreCase(estadoMantenimiento);
    }

    public boolean estaCompletado() {
        return "completado".equalsIgnoreCase(estadoMantenimiento) ||
                "resuelto".equalsIgnoreCase(estadoMantenimiento);
    }

    public boolean tieneFechaAtencion() {
        return fechaAtencion != null;
    }

    public boolean tieneCosto() {
        return costoMantenimiento != null && costoMantenimiento.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean esMantenimientoUrgente() {
        if (descripcionProblema == null) return false;
        String descripcion = descripcionProblema.toLowerCase();
        return descripcion.contains("urgente") ||
                descripcion.contains("emergencia") ||
                descripcion.contains("grave");
    }

    // toString para debugging
    @Override
    public String toString() {
        return "Mantenimiento{" +
                "idMantenimiento=" + idMantenimiento +
                ", idCuarto=" + idCuarto +
                ", fechaReporte=" + fechaReporte +
                ", descripcionProblema='" + (descripcionProblema != null ?
                descripcionProblema.substring(0, Math.min(50, descripcionProblema.length())) : "null") + "'" +
                ", estadoMantenimiento='" + estadoMantenimiento + '\'' +
                ", fechaAtencion=" + fechaAtencion +
                ", costoMantenimiento=" + costoMantenimiento +
                '}';
    }
}