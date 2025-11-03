// Pago.java
package com.poleth.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "pagos")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer idPago;

    @Column(name = "id_contrato", nullable = false)
    private Integer idContrato;

    @Column(name = "fecha_pago", nullable = false)
    private Date fechaPago;

    @Column(name = "concepto", length = 100)
    private String concepto;

    @Column(name = "monto_pagado", precision = 10, scale = 2)
    private BigDecimal montoPagado = BigDecimal.ZERO;

    // Constructor por defecto
    public Pago() {
    }

    // Constructor con parámetros básicos
    public Pago(Integer idContrato, Date fechaPago, String concepto, BigDecimal montoPagado) {
        this.idContrato = idContrato;
        this.fechaPago = fechaPago;
        this.concepto = concepto;
        this.montoPagado = montoPagado;
    }

    // Constructor sin concepto
    public Pago(Integer idContrato, Date fechaPago, BigDecimal montoPagado) {
        this.idContrato = idContrato;
        this.fechaPago = fechaPago;
        this.montoPagado = montoPagado;
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

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
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

    // Método para formatear monto para display
    public String getMontoFormateado() {
        if (montoPagado == null) {
            return "0.00";
        }
        return montoPagado.toString();
    }

    // toString para debugging
    @Override
    public String toString() {
        return "Pago{" +
                "idPago=" + idPago +
                ", idContrato=" + idContrato +
                ", fechaPago=" + fechaPago +
                ", concepto='" + concepto + '\'' +
                ", montoPagado=" + montoPagado +
                '}';
    }
}