// PagoService.java
package com.poleth.api.service;

import com.poleth.api.model.Pago;
import com.poleth.api.repository.PagoRepository;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class PagoService {
    private final PagoRepository pagoRepository;

    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    // Métodos CRUD básicos
    public List<Pago> getAllPagos() {
        return pagoRepository.findAll();
    }

    public Optional<Pago> getPagoById(Integer id) {
        return pagoRepository.findById(id);
    }

    public void deletePago(Integer id) {
        pagoRepository.delete(id);
    }

    // Método para crear un nuevo pago con validaciones
    public Pago createPago(Pago pago) {
        // Validaciones básicas
        if (pago.getIdContrato() == null) {
            throw new IllegalArgumentException("El ID del contrato es requerido");
        }

        if (pago.getFechaPago() == null) {
            throw new IllegalArgumentException("La fecha de pago es requerida");
        }

        if (pago.getMontoPagado() == null) {
            throw new IllegalArgumentException("El monto pagado es requerido");
        }

        // Validar monto
        if (pago.getMontoPagado().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto pagado no puede ser negativo");
        }

        // Validar longitud del concepto
        if (pago.getConcepto() != null && pago.getConcepto().length() > 100) {
            throw new IllegalArgumentException("El concepto no puede exceder 100 caracteres");
        }

        // Validar que la fecha no sea futura (opcional, puedes quitarlo si no aplica)
        Date hoy = new Date(System.currentTimeMillis());
        if (pago.getFechaPago().after(hoy)) {
            throw new IllegalArgumentException("La fecha de pago no puede ser futura");
        }

        // Guardar el pago
        return pagoRepository.save(pago);
    }

    // Método para obtener pagos por contrato
    public List<Pago> getPagosByContrato(Integer idContrato) {
        return pagoRepository.findByContrato(idContrato);
    }

    // Método para obtener pagos por fecha
    public List<Pago> getPagosByFecha(Date fechaPago) {
        return pagoRepository.findByFecha(fechaPago);
    }

    // Método para obtener pagos por monto mayor o igual
    public List<Pago> getPagosByMontoMayorIgual(BigDecimal monto) {
        if (monto == null) {
            throw new IllegalArgumentException("El monto es requerido");
        }
        return pagoRepository.findByMontoMayorIgual(monto);
    }

    // Método para obtener pagos por monto menor o igual
    public List<Pago> getPagosByMontoMenorIgual(BigDecimal monto) {
        if (monto == null) {
            throw new IllegalArgumentException("El monto es requerido");
        }
        return pagoRepository.findByMontoMenorIgual(monto);
    }

    // Método para obtener el pago más reciente por contrato
    public Optional<Pago> getPagoMasRecienteByContrato(Integer idContrato) {
        return pagoRepository.findLatestByContrato(idContrato);
    }

    // Método para obtener el pago más antiguo por contrato
    public Optional<Pago> getPagoMasAntiguoByContrato(Integer idContrato) {
        return pagoRepository.findOldestByContrato(idContrato);
    }
}