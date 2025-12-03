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

    @Column(name = "fecha_registro", insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "usuario_registro", length = 50)
    private String usuarioRegistro;

    // Constructor por defecto
    public HistorialReporte() {
    }

    // Constructor con parámetros básicos
    public HistorialReporte(Integer idReporte, String nombreReporteHist, String tipoReporteHist,
                            String descripcionHist, String usuarioRegistro) {
        this.idReporte = idReporte;
        this.nombreReporteHist = nombreReporteHist;
        this.tipoReporteHist = tipoReporteHist;
        this.descripcionHist = descripcionHist;
        this.usuarioRegistro = usuarioRegistro;
    }

    // Constructor completo
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

    // Métodos utilitarios
    public boolean tieneUsuarioRegistro() {
        return usuarioRegistro != null && !usuarioRegistro.trim().isEmpty();
    }

    public boolean esHistorialReciente() {
        if (fechaRegistro == null) return false;
        LocalDateTime unaSemanaAtras = LocalDateTime.now().minusDays(7);
        return fechaRegistro.isAfter(unaSemanaAtras);
    }

    public boolean esHistorialDelDia() {
        if (fechaRegistro == null) return false;
        LocalDateTime hoy = LocalDateTime.now();
        return fechaRegistro.toLocalDate().equals(hoy.toLocalDate());
    }

    public boolean contieneTextoEnDescripcion(String texto) {
        if (descripcionHist == null || texto == null) return false;
        return descripcionHist.toLowerCase().contains(texto.toLowerCase());
    }

    // toString para debugging
    @Override
    public String toString() {
        return "HistorialReporte{" +
                "idHistorial=" + idHistorial +
                ", idReporte=" + idReporte +
                ", nombreReporteHist='" + nombreReporteHist + '\'' +
                ", tipoReporteHist='" + tipoReporteHist + '\'' +
                ", descripcionHist='" + (descripcionHist != null ?
                descripcionHist.substring(0, Math.min(50, descripcionHist.length())) : "null") + "'" +
                ", fechaRegistro=" + fechaRegistro +
                ", usuarioRegistro='" + usuarioRegistro + '\'' +
                '}';
    }

    // Método estático para crear historial a partir de reporte
    public static HistorialReporte crearDesdeReporte(ReporteInquilino reporte, String usuario) {
        return new HistorialReporte(
                reporte.getIdReporte(),
                reporte.getNombre(),
                reporte.getTipo(),
                reporte.getDescripcion(),
                usuario
        );
    }

    // Método para crear historial con acción específica
    public static HistorialReporte crearConAccion(ReporteInquilino reporte, String accion,
                                                  String detalles, String usuario) {
        String descripcionCompleta = String.format("Acción: %s\nDetalles: %s\nReporte original: %s",
                accion,
                detalles != null ? detalles : "Sin detalles adicionales",
                reporte.getDescripcion() != null ? reporte.getDescripcion() : "Sin descripción"
        );

        return new HistorialReporte(
                reporte.getIdReporte(),
                reporte.getNombre(),
                reporte.getTipo(),
                descripcionCompleta,
                usuario
        );
    }

    // Método para crear historial de cierre
    public static HistorialReporte crearHistorialCierre(ReporteInquilino reporte,
                                                        String accionesTomadas, String usuario) {
        String descripcion = String.format("REPORTE CERRADO\nAcciones tomadas: %s\nEstado anterior: %s\nFecha de cierre: %s",
                accionesTomadas,
                reporte.getEstadoReporte(),
                reporte.getFechaCierre() != null ? reporte.getFechaCierre().toString() : "No especificada"
        );

        return new HistorialReporte(
                reporte.getIdReporte(),
                reporte.getNombre() + " (CERRADO)",
                reporte.getTipo(),
                descripcion,
                usuario
        );
    }
}