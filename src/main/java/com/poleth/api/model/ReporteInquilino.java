// ReporteInquilino.java
package com.poleth.api.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Reportes_Inquilinos")
public class ReporteInquilino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Integer idReporte;

    @Column(name = "id_inquilino", nullable = false)
    private Integer idInquilino;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "tipo", length = 50)
    private String tipo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "id_cuarto", nullable = false)
    private Integer idCuarto;

    @Column(name = "estado_reporte", length = 50)
    private String estadoReporte = "abierto";

    @Column(name = "fecha_cierre")
    private LocalDate fechaCierre;

    @Column(name = "acciones_tomadas", columnDefinition = "TEXT")
    private String accionesTomadas;

    // Constructor por defecto
    public ReporteInquilino() {
    }

    // Constructor con parámetros básicos
    public ReporteInquilino(Integer idInquilino, String nombre, String tipo, String descripcion,
                            LocalDate fecha, Integer idCuarto) {
        this.idInquilino = idInquilino;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.idCuarto = idCuarto;
    }

    // Constructor completo
    public ReporteInquilino(Integer idInquilino, String nombre, String tipo, String descripcion,
                            LocalDate fecha, Integer idCuarto, String estadoReporte,
                            LocalDate fechaCierre, String accionesTomadas) {
        this.idInquilino = idInquilino;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.idCuarto = idCuarto;
        this.estadoReporte = estadoReporte;
        this.fechaCierre = fechaCierre;
        this.accionesTomadas = accionesTomadas;
    }

    // Getters y Setters
    public Integer getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(Integer idReporte) {
        this.idReporte = idReporte;
    }

    public Integer getIdInquilino() {
        return idInquilino;
    }

    public void setIdInquilino(Integer idInquilino) {
        this.idInquilino = idInquilino;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Integer getIdCuarto() {
        return idCuarto;
    }

    public void setIdCuarto(Integer idCuarto) {
        this.idCuarto = idCuarto;
    }

    public String getEstadoReporte() {
        return estadoReporte;
    }

    public void setEstadoReporte(String estadoReporte) {
        this.estadoReporte = estadoReporte;
    }

    public LocalDate getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDate fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public String getAccionesTomadas() {
        return accionesTomadas;
    }

    public void setAccionesTomadas(String accionesTomadas) {
        this.accionesTomadas = accionesTomadas;
    }

    // Métodos utilitarios
    public boolean estaAbierto() {
        return "abierto".equalsIgnoreCase(estadoReporte);
    }

    public boolean estaCerrado() {
        return "cerrado".equalsIgnoreCase(estadoReporte) ||
                "resuelto".equalsIgnoreCase(estadoReporte) ||
                "completado".equalsIgnoreCase(estadoReporte);
    }

    public boolean tieneFechaCierre() {
        return fechaCierre != null;
    }

    public boolean tieneAccionesTomadas() {
        return accionesTomadas != null && !accionesTomadas.trim().isEmpty();
    }

    public boolean esReporteUrgente() {
        if (descripcion == null) return false;
        String descripcionLower = descripcion.toLowerCase();
        return descripcionLower.contains("urgente") ||
                descripcionLower.contains("emergencia") ||
                descripcionLower.contains("inmediato");
    }

    public void cerrarReporte(String acciones) {
        this.estadoReporte = "cerrado";
        this.fechaCierre = LocalDate.now();
        if (acciones != null && !acciones.trim().isEmpty()) {
            this.accionesTomadas = acciones;
        }
    }

    // toString para debugging
    @Override
    public String toString() {
        return "ReporteInquilino{" +
                "idReporte=" + idReporte +
                ", idInquilino=" + idInquilino +
                ", nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", descripcion='" + (descripcion != null ?
                descripcion.substring(0, Math.min(50, descripcion.length())) : "null") + "'" +
                ", fecha=" + fecha +
                ", idCuarto=" + idCuarto +
                ", estadoReporte='" + estadoReporte + '\'' +
                ", fechaCierre=" + fechaCierre +
                ", accionesTomadas='" + (accionesTomadas != null ?
                accionesTomadas.substring(0, Math.min(50, accionesTomadas.length())) : "null") + "'" +
                '}';
    }
}