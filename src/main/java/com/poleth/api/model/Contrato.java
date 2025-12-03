// Contrato.java
package com.poleth.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Contratos")
public class Contrato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contrato")
    private Integer idContrato;

    @Column(name = "id_cuarto", nullable = false)
    private Integer idCuarto;

    @Column(name = "id_inquilino", nullable = false)
    private Integer idInquilino;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_finalizacion")
    private LocalDate fechaFinalizacion;

    @Column(name = "fecha_pago_establecida")
    private LocalDate fechaPagoEstablecida;

    @Column(name = "estado_contrato", length = 50)
    private String estadoContrato = "activo";

    @Column(name = "monto_renta_acordada", precision = 10, scale = 2)
    private BigDecimal montoRentaAcordada = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // CORRECCIÓN: Agregar JsonIgnoreProperties para evitar errores de lazy loading
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cuarto", referencedColumnName = "id_cuarto", insertable = false, updatable = false)
    @JsonIgnoreProperties({"propietario", "hibernateLazyInitializer", "handler"})
    private Cuarto cuarto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_inquilino", referencedColumnName = "id_usuario", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "password"})
    private Usuario inquilino;

    // Constructor por defecto
    public Contrato() {
        this.createdAt = LocalDateTime.now();
    }

    // Constructor con parámetros básicos
    public Contrato(Integer idCuarto, Integer idInquilino, LocalDate fechaInicio) {
        this();
        this.idCuarto = idCuarto;
        this.idInquilino = idInquilino;
        this.fechaInicio = fechaInicio;
    }

    // Constructor completo
    public Contrato(Integer idCuarto, Integer idInquilino, LocalDate fechaInicio,
                    LocalDate fechaFinalizacion, LocalDate fechaPagoEstablecida,
                    String estadoContrato, BigDecimal montoRentaAcordada) {
        this();
        this.idCuarto = idCuarto;
        this.idInquilino = idInquilino;
        this.fechaInicio = fechaInicio;
        this.fechaFinalizacion = fechaFinalizacion;
        this.fechaPagoEstablecida = fechaPagoEstablecida;
        this.estadoContrato = estadoContrato;
        this.montoRentaAcordada = montoRentaAcordada;
    }

    // Getters y Setters
    public Integer getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(Integer idContrato) {
        this.idContrato = idContrato;
    }

    public Integer getIdCuarto() {
        return idCuarto;
    }

    public void setIdCuarto(Integer idCuarto) {
        this.idCuarto = idCuarto;
    }

    public Integer getIdInquilino() {
        return idInquilino;
    }

    public void setIdInquilino(Integer idInquilino) {
        this.idInquilino = idInquilino;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(LocalDate fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public LocalDate getFechaPagoEstablecida() {
        return fechaPagoEstablecida;
    }

    public void setFechaPagoEstablecida(LocalDate fechaPagoEstablecida) {
        this.fechaPagoEstablecida = fechaPagoEstablecida;
    }

    public String getEstadoContrato() {
        return estadoContrato;
    }

    public void setEstadoContrato(String estadoContrato) {
        this.estadoContrato = estadoContrato;
    }

    public BigDecimal getMontoRentaAcordada() {
        return montoRentaAcordada;
    }

    public void setMontoRentaAcordada(BigDecimal montoRentaAcordada) {
        this.montoRentaAcordada = montoRentaAcordada;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Cuarto getCuarto() {
        return cuarto;
    }

    public void setCuarto(Cuarto cuarto) {
        this.cuarto = cuarto;
    }

    public Usuario getInquilino() {
        return inquilino;
    }

    public void setInquilino(Usuario inquilino) {
        this.inquilino = inquilino;
    }

    // Métodos utilitarios
    public boolean estaActivo() {
        return "activo".equalsIgnoreCase(estadoContrato);
    }

    public boolean estaFinalizado() {
        return "finalizado".equalsIgnoreCase(estadoContrato) ||
                "cancelado".equalsIgnoreCase(estadoContrato);
    }

    public boolean estaVigente() {
        LocalDate hoy = LocalDate.now();
        boolean inicioValido = !fechaInicio.isAfter(hoy);
        boolean finValido = fechaFinalizacion == null || !fechaFinalizacion.isBefore(hoy);
        return inicioValido && finValido;
    }

    public boolean estaPorVencer() {
        if (fechaFinalizacion == null) return false;
        LocalDate hoy = LocalDate.now();
        return fechaFinalizacion.isAfter(hoy) && fechaFinalizacion.isBefore(hoy.plusDays(30));
    }

    // Método para establecer el timestamp antes de persistir
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (estadoContrato == null || estadoContrato.isEmpty()) {
            estadoContrato = "activo";
        }
        if (montoRentaAcordada == null) {
            montoRentaAcordada = BigDecimal.ZERO;
        }
    }

    // toString para debugging
    @Override
    public String toString() {
        return "Contrato{" +
                "idContrato=" + idContrato +
                ", idCuarto=" + idCuarto +
                ", idInquilino=" + idInquilino +
                ", fechaInicio=" + fechaInicio +
                ", fechaFinalizacion=" + fechaFinalizacion +
                ", fechaPagoEstablecida=" + fechaPagoEstablecida +
                ", estadoContrato='" + estadoContrato + '\'' +
                ", montoRentaAcordada=" + montoRentaAcordada +
                ", createdAt=" + createdAt +
                '}';
    }
}