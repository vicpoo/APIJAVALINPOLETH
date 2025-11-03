// HistorialReporte.java
package com.poleth.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Historial_Reportes")
public class HistorialReporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Integer idHistorial;

    @Column(name = "id_reporte", nullable = false)
    private Integer idReporte;

    @Column(name = "nombre_reporte_hist", length = 100)
    private String nombreReporteHist;

    @Column(name = "tipo_reporte_hist", length = 50)
    private String tipoReporteHist;

    @Column(name = "descripcion_hist", columnDefinition = "TEXT")
    private String descripcionHist;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "usuario_registro", length = 50)
    private String usuarioRegistro;

    // Relación Many-to-One con ReporteInquilino - Cambiado a EAGER
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_reporte", insertable = false, updatable = false)
    private ReporteInquilino reporte;

    // Constructor por defecto
    public HistorialReporte() {
        this.fechaRegistro = LocalDateTime.now();
    }

    // Constructor con parámetros básicos
    public HistorialReporte(Integer idReporte, String nombreReporteHist, String tipoReporteHist, String descripcionHist) {
        this();
        this.idReporte = idReporte;
        this.nombreReporteHist = nombreReporteHist;
        this.tipoReporteHist = tipoReporteHist;
        this.descripcionHist = descripcionHist;
    }

    // Constructor completo
    public HistorialReporte(Integer idReporte, String nombreReporteHist, String tipoReporteHist, 
                           String descripcionHist, String usuarioRegistro) {
        this();
        this.idReporte = idReporte;
        this.nombreReporteHist = nombreReporteHist;
        this.tipoReporteHist = tipoReporteHist;
        this.descripcionHist = descripcionHist;
        this.usuarioRegistro = usuarioRegistro;
    }

    // Constructor con fecha personalizada
    public HistorialReporte(Integer idReporte, String nombreReporteHist, String tipoReporteHist, 
                           String descripcionHist, LocalDateTime fechaRegistro, String usuarioRegistro) {
        this.idReporte = idReporte;
        this.nombreReporteHist = nombreReporteHist;
        this.tipoReporteHist = tipoReporteHist;
        this.descripcionHist = descripcionHist;
        this.fechaRegistro = fechaRegistro;
        this.usuarioRegistro = usuarioRegistro;
    }

    // Getters y Setters
    public Integer getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(Integer idHistorial) {
        this.idHistorial = idHistorial;
    }

    public Integer getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(Integer idReporte) {
        this.idReporte = idReporte;
    }

    public String getNombreReporteHist() {
        return nombreReporteHist;
    }

    public void setNombreReporteHist(String nombreReporteHist) {
        this.nombreReporteHist = nombreReporteHist;
    }

    public String getTipoReporteHist() {
        return tipoReporteHist;
    }

    public void setTipoReporteHist(String tipoReporteHist) {
        this.tipoReporteHist = tipoReporteHist;
    }

    public String getDescripcionHist() {
        return descripcionHist;
    }

    public void setDescripcionHist(String descripcionHist) {
        this.descripcionHist = descripcionHist;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(String usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    public ReporteInquilino getReporte() {
        return reporte;
    }

    public void setReporte(ReporteInquilino reporte) {
        this.reporte = reporte;
    }

    // toString para debugging (sin relaciones para evitar problemas)
    @Override
    public String toString() {
        return "HistorialReporte{" +
                "idHistorial=" + idHistorial +
                ", idReporte=" + idReporte +
                ", nombreReporteHist='" + nombreReporteHist + '\'' +
                ", tipoReporteHist='" + tipoReporteHist + '\'' +
                ", descripcionHist='" + (descripcionHist != null ? descripcionHist.substring(0, Math.min(50, descripcionHist.length())) + "..." : "null") + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                ", usuarioRegistro='" + usuarioRegistro + '\'' +
                '}';
    }

    // Método para crear un historial a partir de un reporte
    public static HistorialReporte fromReporteInquilino(ReporteInquilino reporte, String usuarioRegistro) {
        return new HistorialReporte(
            reporte.getIdReporte(),
            reporte.getNombre(),
            reporte.getTipo(),
            reporte.getDescripcion(),
            usuarioRegistro
        );
    }

    // Método para crear un historial con acciones específicas
    public static HistorialReporte createWithAction(ReporteInquilino reporte, String accion, String usuarioRegistro) {
        String descripcion = String.format("Acción realizada: %s. Reporte: %s", 
            accion, 
            reporte.getDescripcion() != null ? reporte.getDescripcion() : "Sin descripción"
        );
        
        return new HistorialReporte(
            reporte.getIdReporte(),
            reporte.getNombre(),
            reporte.getTipo(),
            descripcion,
            usuarioRegistro
        );
    }
}