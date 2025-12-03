package com.poleth.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Pagos")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer idPago;

    @Column(name = "id_contrato", nullable = false)
    private Integer idContrato;

    @Column(name = "id_inquilino", nullable = false)
    private Integer idInquilino;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;

    @Column(name = "concepto", length = 100)
    private String concepto;

    @Column(name = "monto_pagado", precision = 10, scale = 2)
    private BigDecimal montoPagado = BigDecimal.ZERO;

    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;

    @Column(name = "estado_pago", length = 20)
    private String estadoPago = "completado";

    // ✅ Relaciones con @JsonIgnore para evitar LazyInitializationException
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contrato", referencedColumnName = "id_contrato", insertable = false, updatable = false)
    private Contrato contrato;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_inquilino", referencedColumnName = "id_usuario", insertable = false, updatable = false)
    private Usuario inquilino;

    // Constructor por defecto
    public Pago() {
    }

    // Constructor con parámetros básicos
    public Pago(Integer idContrato, Integer idInquilino, LocalDate fechaPago, BigDecimal montoPagado) {
        this.idContrato = idContrato;
        this.idInquilino = idInquilino;
        this.fechaPago = fechaPago;
        this.montoPagado = montoPagado;
    }

    // Constructor completo
    public Pago(Integer idContrato, Integer idInquilino, LocalDate fechaPago, String concepto,
                BigDecimal montoPagado, String metodoPago, String estadoPago) {
        this.idContrato = idContrato;
        this.idInquilino = idInquilino;
        this.fechaPago = fechaPago;
        this.concepto = concepto;
        this.montoPagado = montoPagado;
        this.metodoPago = metodoPago;
        this.estadoPago = estadoPago;
    }

    // Getters y Setters
    public Integer getIdPago() {
        return idPago;
    }

    public void setIdPago(Integer idPago) {
        this.idPago = idPago;
    }

    public Integer getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(Integer idContrato) {
        this.idContrato = idContrato;
    }

    public Integer getIdInquilino() {
        return idInquilino;
    }

    public void setIdInquilino(Integer idInquilino) {
        this.idInquilino = idInquilino;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public BigDecimal getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(BigDecimal montoPagado) {
        this.montoPagado = montoPagado;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public Usuario getInquilino() {
        return inquilino;
    }

    public void setInquilino(Usuario inquilino) {
        this.inquilino = inquilino;
    }

    // Métodos utilitarios
    public boolean tieneConcepto() {
        return concepto != null && !concepto.trim().isEmpty();
    }

    public boolean esMontoPositivo() {
        return montoPagado != null && montoPagado.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean esMontoCero() {
        return montoPagado != null && montoPagado.compareTo(BigDecimal.ZERO) == 0;
    }

    public boolean estaCompletado() {
        return "completado".equalsIgnoreCase(estadoPago);
    }

    public boolean estaPendiente() {
        return "pendiente".equalsIgnoreCase(estadoPago);
    }

    public boolean estaCancelado() {
        return "cancelado".equalsIgnoreCase(estadoPago);
    }

    public boolean tieneMetodoPago() {
        return metodoPago != null && !metodoPago.trim().isEmpty();
    }

    // Método para validar campos requeridos
    public boolean esValido() {
        return idContrato != null && idInquilino != null &&
                fechaPago != null && montoPagado != null;
    }

    // toString para debugging
    @Override
    public String toString() {
        return "Pago{" +
                "idPago=" + idPago +
                ", idContrato=" + idContrato +
                ", idInquilino=" + idInquilino +
                ", fechaPago=" + fechaPago +
                ", concepto='" + concepto + '\'' +
                ", montoPagado=" + montoPagado +
                ", metodoPago='" + metodoPago + '\'' +
                ", estadoPago='" + estadoPago + '\'' +
                '}';
    }
}