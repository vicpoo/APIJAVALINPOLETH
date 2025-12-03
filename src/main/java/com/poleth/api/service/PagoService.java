// PagoService.java
package com.poleth.api.service;

import com.poleth.api.model.Pago;
import com.poleth.api.repository.PagoRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
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

        if (pago.getIdInquilino() == null) {
            throw new IllegalArgumentException("El ID del inquilino es requerido");
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

        // Validar longitud de campos
        if (pago.getConcepto() != null && pago.getConcepto().length() > 100) {
            throw new IllegalArgumentException("El concepto no puede exceder 100 caracteres");
        }

        if (pago.getMetodoPago() != null && pago.getMetodoPago().length() > 50) {
            throw new IllegalArgumentException("El método de pago no puede exceder 50 caracteres");
        }

        if (pago.getEstadoPago() != null && pago.getEstadoPago().length() > 20) {
            throw new IllegalArgumentException("El estado del pago no puede exceder 20 caracteres");
        }

        // Validar estado si se proporciona
        if (pago.getEstadoPago() != null) {
            String estado = pago.getEstadoPago().toLowerCase();
            if (!estado.equals("completado") && !estado.equals("pendiente") &&
                    !estado.equals("cancelado") && !estado.equals("rechazado")) {
                throw new IllegalArgumentException("Estado inválido. Valores permitidos: 'completado', 'pendiente', 'cancelado', 'rechazado'");
            }
        } else {
            // Establecer estado por defecto
            pago.setEstadoPago("completado");
        }

        // Establecer monto por defecto si es null
        if (pago.getMontoPagado() == null) {
            pago.setMontoPagado(BigDecimal.ZERO);
        }

        // Validar que la fecha no sea futura (opcional, puedes quitarlo si no aplica)
        LocalDate hoy = LocalDate.now();
        if (pago.getFechaPago().isAfter(hoy)) {
            throw new IllegalArgumentException("La fecha de pago no puede ser futura");
        }

        // Guardar el pago
        Pago pagoGuardado = pagoRepository.save(pago);

        // Recargar con relaciones para evitar LazyInitializationException
        return pagoRepository.findById(pagoGuardado.getIdPago())
                .orElseThrow(() -> new RuntimeException("Error al recuperar el pago creado"));
    }

    // Método para actualizar un pago existente
    public Pago updatePago(Integer id, Pago pagoActualizado) {
        // Validaciones básicas
        if (pagoActualizado.getMontoPagado() != null &&
                pagoActualizado.getMontoPagado().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto pagado no puede ser negativo");
        }

        // Validar longitud de campos
        if (pagoActualizado.getConcepto() != null &&
                pagoActualizado.getConcepto().length() > 100) {
            throw new IllegalArgumentException("El concepto no puede exceder 100 caracteres");
        }

        if (pagoActualizado.getMetodoPago() != null &&
                pagoActualizado.getMetodoPago().length() > 50) {
            throw new IllegalArgumentException("El método de pago no puede exceder 50 caracteres");
        }

        if (pagoActualizado.getEstadoPago() != null &&
                pagoActualizado.getEstadoPago().length() > 20) {
            throw new IllegalArgumentException("El estado del pago no puede exceder 20 caracteres");
        }

        // Validar estado si se proporciona
        if (pagoActualizado.getEstadoPago() != null) {
            String estado = pagoActualizado.getEstadoPago().toLowerCase();
            if (!estado.equals("completado") && !estado.equals("pendiente") &&
                    !estado.equals("cancelado") && !estado.equals("rechazado")) {
                throw new IllegalArgumentException("Estado inválido. Valores permitidos: 'completado', 'pendiente', 'cancelado', 'rechazado'");
            }
        }

        // Buscar el pago existente
        Optional<Pago> pagoExistenteOpt = pagoRepository.findById(id);
        if (pagoExistenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Pago no encontrado con ID: " + id);
        }

        Pago pagoExistente = pagoExistenteOpt.get();

        // NO se pueden cambiar contrato ni inquilino (son FK)
        // Actualizar campos permitidos
        if (pagoActualizado.getFechaPago() != null) {
            pagoExistente.setFechaPago(pagoActualizado.getFechaPago());
        }
        if (pagoActualizado.getConcepto() != null) {
            pagoExistente.setConcepto(pagoActualizado.getConcepto());
        }
        if (pagoActualizado.getMontoPagado() != null) {
            pagoExistente.setMontoPagado(pagoActualizado.getMontoPagado());
        }
        if (pagoActualizado.getMetodoPago() != null) {
            pagoExistente.setMetodoPago(pagoActualizado.getMetodoPago());
        }
        if (pagoActualizado.getEstadoPago() != null) {
            pagoExistente.setEstadoPago(pagoActualizado.getEstadoPago());
        }

        // Guardar los cambios
        pagoRepository.save(pagoExistente);

        // Recargar con relaciones para evitar LazyInitializationException
        return pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error al recuperar el pago actualizado"));
    }

    // Método para cambiar estado del pago
    public Pago cambiarEstadoPago(Integer idPago, String nuevoEstado) {
        Optional<Pago> pagoOpt = pagoRepository.findById(idPago);
        if (pagoOpt.isEmpty()) {
            throw new IllegalArgumentException("Pago no encontrado con ID: " + idPago);
        }

        // Validar estado
        String estado = nuevoEstado.toLowerCase();
        if (!estado.equals("completado") && !estado.equals("pendiente") &&
                !estado.equals("cancelado") && !estado.equals("rechazado")) {
            throw new IllegalArgumentException("Estado inválido. Valores permitidos: 'completado', 'pendiente', 'cancelado', 'rechazado'");
        }

        // Validar longitud
        if (nuevoEstado.length() > 20) {
            throw new IllegalArgumentException("El estado del pago no puede exceder 20 caracteres");
        }

        Pago pago = pagoOpt.get();
        pago.setEstadoPago(nuevoEstado);

        // Guardar y luego recuperar con la relación cargada
        pagoRepository.save(pago);
        return pagoRepository.findById(idPago)
                .orElseThrow(() -> new RuntimeException("Error al recuperar el pago actualizado"));
    }

    // Métodos de búsqueda
    public List<Pago> getPagosByContrato(Integer idContrato) {
        return pagoRepository.findByContrato(idContrato);
    }

    public List<Pago> getPagosByInquilino(Integer idInquilino) {
        return pagoRepository.findByInquilino(idInquilino);
    }

    public List<Pago> getPagosByFecha(LocalDate fechaPago) {
        return pagoRepository.findByFecha(fechaPago);
    }

    public List<Pago> getPagosByEstado(String estadoPago) {
        return pagoRepository.findByEstado(estadoPago);
    }

    public List<Pago> getPagosByMetodoPago(String metodoPago) {
        return pagoRepository.findByMetodoPago(metodoPago);
    }

    public List<Pago> getPagosByMontoMayorIgual(BigDecimal monto) {
        if (monto == null) {
            throw new IllegalArgumentException("El monto es requerido");
        }
        return pagoRepository.findByMontoMayorIgual(monto);
    }

    public List<Pago> getPagosByMontoMenorIgual(BigDecimal monto) {
        if (monto == null) {
            throw new IllegalArgumentException("El monto es requerido");
        }
        return pagoRepository.findByMontoMenorIgual(monto);
    }

    public Optional<Pago> getPagoMasRecienteByContrato(Integer idContrato) {
        return pagoRepository.findLatestByContrato(idContrato);
    }

    public Optional<Pago> getPagoMasAntiguoByContrato(Integer idContrato) {
        return pagoRepository.findOldestByContrato(idContrato);
    }

    // Método para verificar si un pago existe por ID
    public boolean existsById(Integer id) {
        return pagoRepository.existsById(id);
    }

    // Métodos de resumen
    public BigDecimal getTotalPagadoPorContrato(Integer idContrato) {
        return pagoRepository.sumMontoByContrato(idContrato);
    }

    public BigDecimal getTotalPagadoPorInquilino(Integer idInquilino) {
        return pagoRepository.sumMontoByInquilino(idInquilino);
    }

    public BigDecimal getTotalPagadoPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return pagoRepository.sumMontoByRangoFechas(fechaInicio, fechaFin);
    }

    // Método para obtener pagos por rango de fechas
    public List<Pago> getPagosPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return pagoRepository.findByRangoFechas(fechaInicio, fechaFin);
    }

    // Método para obtener pagos por rango de montos
    public List<Pago> getPagosPorRangoMontos(BigDecimal montoMin, BigDecimal montoMax) {
        return pagoRepository.findByRangoMontos(montoMin, montoMax);
    }

    // Método para obtener pagos por concepto
    public List<Pago> getPagosPorConcepto(String concepto) {
        return pagoRepository.findByConcepto(concepto);
    }
}