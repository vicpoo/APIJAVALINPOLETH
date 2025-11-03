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
    private String estadoMantenimiento;

    @Column(name = "fecha_atencion")
    private LocalDate fechaAtencion;

    @Column(name = "costo_mantenimiento", precision = 10, scale = 2)
    private BigDecimal costoMantenimiento;

    // Relación Many-to-One con Cuarto (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cuarto", insertable = false, updatable = false)
    private Cuarto cuarto;

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

    public Cuarto getCuarto() {
        return cuarto;
    }

    public void setCuarto(Cuarto cuarto) {
        this.cuarto = cuarto;
    }

    // toString para debugging
    @Override
    public String toString() {
        return "Mantenimiento{" +
                "idMantenimiento=" + idMantenimiento +
                ", idCuarto=" + idCuarto +
                ", fechaReporte=" + fechaReporte +
                ", descripcionProblema='" + (descripcionProblema != null ? descripcionProblema.substring(0, Math.min(50, descripcionProblema.length())) + "..." : "null") + '\'' +
                ", estadoMantenimiento='" + estadoMantenimiento + '\'' +
                ", fechaAtencion=" + fechaAtencion +
                ", costoMantenimiento=" + costoMantenimiento +
                '}';
    }
}