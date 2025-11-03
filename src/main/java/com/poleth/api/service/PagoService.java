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
    public Pago savePago(Pago pago) {
        return pagoRepository.save(pago);
    }

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

    // Método para actualizar un pago existente
    public Pago updatePago(Integer id, Pago pagoActualizado) {
        // Validaciones básicas
        if (pagoActualizado.getFechaPago() == null) {
            throw new IllegalArgumentException("La fecha de pago es requerida");
        }

        if (pagoActualizado.getMontoPagado() == null) {
            throw new IllegalArgumentException("El monto pagado es requerido");
        }

        // Validar monto
        if (pagoActualizado.getMontoPagado().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto pagado no puede ser negativo");
        }

        // Validar longitud del concepto
        if (pagoActualizado.getConcepto() != null && pagoActualizado.getConcepto().length() > 100) {
            throw new IllegalArgumentException("El concepto no puede exceder 100 caracteres");
        }

        // Validar que la fecha no sea futura
        Date hoy = new Date(System.currentTimeMillis());
        if (pagoActualizado.getFechaPago().after(hoy)) {
            throw new IllegalArgumentException("La fecha de pago no puede ser futura");
        }

        // Buscar el pago existente
        Optional<Pago> pagoExistenteOpt = pagoRepository.findById(id);
        if (pagoExistenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Pago no encontrado con ID: " + id);
        }

        Pago pagoExistente = pagoExistenteOpt.get();

        // Actualizar campos permitidos (no se puede cambiar el contrato)
        pagoExistente.setFechaPago(pagoActualizado.getFechaPago());
        pagoExistente.setConcepto(pagoActualizado.getConcepto());
        pagoExistente.setMontoPagado(pagoActualizado.getMontoPagado());

        // Guardar los cambios
        return pagoRepository.save(pagoExistente);
    }

    // Método para obtener pagos por contrato
    public List<Pago> getPagosByContrato(Integer idContrato) {
        return pagoRepository.findByContrato(idContrato);
    }

    // Método para obtener pagos por fecha
    public List<Pago> getPagosByFecha(Date fechaPago) {
        return pagoRepository.findByFecha(fechaPago);
    }

    // Método para obtener pagos por rango de fechas
    public List<Pago> getPagosByRangoFechas(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son requeridas");
        }
        if (fechaInicio.after(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        return pagoRepository.findByRangoFechas(fechaInicio, fechaFin);
    }

    // Método para obtener pagos por concepto
    public List<Pago> getPagosByConcepto(String concepto) {
        return pagoRepository.findByConcepto(concepto);
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

    // Método para obtener pagos por rango de montos
    public List<Pago> getPagosByRangoMontos(BigDecimal montoMin, BigDecimal montoMax) {
        if (montoMin == null || montoMax == null) {
            throw new IllegalArgumentException("Los montos mínimo y máximo son requeridos");
        }
        if (montoMin.compareTo(montoMax) > 0) {
            throw new IllegalArgumentException("El monto mínimo no puede ser mayor al monto máximo");
        }
        return pagoRepository.findByRangoMontos(montoMin, montoMax);
    }

    // Método para obtener pagos con concepto
    public List<Pago> getPagosWithConcepto() {
        return pagoRepository.findWithConcepto();
    }

    // Método para obtener pagos sin concepto
    public List<Pago> getPagosWithoutConcepto() {
        return pagoRepository.findWithoutConcepto();
    }

    // Método para obtener el pago más reciente por contrato
    public Optional<Pago> getPagoMasRecienteByContrato(Integer idContrato) {
        return pagoRepository.findLatestByContrato(idContrato);
    }

    // Método para obtener el pago más antiguo por contrato
    public Optional<Pago> getPagoMasAntiguoByContrato(Integer idContrato) {
        return pagoRepository.findOldestByContrato(idContrato);
    }

    // Método para verificar si un pago existe por ID
    public boolean existsById(Integer id) {
        return pagoRepository.findById(id).isPresent();
    }

    // Método para verificar si existen pagos por contrato
    public boolean existsByContrato(Integer idContrato) {
        return pagoRepository.existsByContrato(idContrato);
    }

    // Método para contar todos los pagos
    public Long countPagos() {
        return pagoRepository.count();
    }

    // Método para contar pagos por contrato
    public Long countByContrato(Integer idContrato) {
        return pagoRepository.countByContrato(idContrato);
    }

    // Método para sumar montos de pagos por contrato
    public BigDecimal sumMontoByContrato(Integer idContrato) {
        BigDecimal suma = pagoRepository.sumMontoByContrato(idContrato);
        return suma != null ? suma : BigDecimal.ZERO;
    }

    // Método para sumar montos de pagos por rango de fechas
    public BigDecimal sumMontoByRangoFechas(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son requeridas");
        }
        if (fechaInicio.after(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        BigDecimal suma = pagoRepository.sumMontoByRangoFechas(fechaInicio, fechaFin);
        return suma != null ? suma : BigDecimal.ZERO;
    }

    // Método para obtener pagos con paginación
    public List<Pago> getPagosPaginados(int pagina, int tamaño) {
        if (pagina < 1) pagina = 1;
        if (tamaño < 1 || tamaño > 100) tamaño = 20;
        
        int inicio = (pagina - 1) * tamaño;
        return pagoRepository.findPaginados(inicio, tamaño);
    }

    // Método para obtener pagos por contrato con paginación
    public List<Pago> getPagosByContratoPaginados(Integer idContrato, int pagina, int tamaño) {
        if (pagina < 1) pagina = 1;
        if (tamaño < 1 || tamaño > 100) tamaño = 20;
        
        int inicio = (pagina - 1) * tamaño;
        return pagoRepository.findByContratoPaginados(idContrato, inicio, tamaño);
    }

    // Método para eliminar todos los pagos de un contrato
    public int deletePagosByContrato(Integer idContrato) {
        return pagoRepository.deleteByContrato(idContrato);
    }

    // Método para actualizar monto de un pago
    public boolean updateMonto(Integer idPago, BigDecimal nuevoMonto) {
        if (nuevoMonto == null) {
            throw new IllegalArgumentException("El nuevo monto es requerido");
        }
        if (nuevoMonto.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }

        // Verificar que el pago exista
        Optional<Pago> pago = pagoRepository.findById(idPago);
        if (pago.isEmpty()) {
            throw new IllegalArgumentException("Pago no encontrado con ID: " + idPago);
        }

        int updated = pagoRepository.updateMonto(idPago, nuevoMonto);
        return updated > 0;
    }

    // Método para actualizar concepto de un pago
    public boolean updateConcepto(Integer idPago, String nuevoConcepto) {
        if (nuevoConcepto != null && nuevoConcepto.length() > 100) {
            throw new IllegalArgumentException("El concepto no puede exceder 100 caracteres");
        }

        // Verificar que el pago exista
        Optional<Pago> pago = pagoRepository.findById(idPago);
        if (pago.isEmpty()) {
            throw new IllegalArgumentException("Pago no encontrado con ID: " + idPago);
        }

        int updated = pagoRepository.updateConcepto(idPago, nuevoConcepto);
        return updated > 0;
    }

    // Método para registrar un pago rápido
    public Pago registrarPagoRapido(Integer idContrato, BigDecimal monto, String concepto) {
        Pago pago = new Pago();
        pago.setIdContrato(idContrato);
        pago.setFechaPago(new Date(System.currentTimeMillis()));
        pago.setMontoPagado(monto);
        pago.setConcepto(concepto);

        return createPago(pago);
    }

    // Método para obtener estadísticas de pagos
    public PagoStats getStats() {
        Long totalPagos = pagoRepository.count();
        Long pagosConConcepto = (long) pagoRepository.findWithConcepto().size();
        Long pagosSinConcepto = (long) pagoRepository.findWithoutConcepto().size();
        BigDecimal montoTotal = pagoRepository.sumMontoByRangoFechas(
            Date.valueOf("1900-01-01"), 
            Date.valueOf("2100-12-31")
        );
        
        return new PagoStats(totalPagos, pagosConConcepto, pagosSinConcepto, montoTotal);
    }

    // Método para obtener estadísticas por contrato
    public PagoStats getStatsByContrato(Integer idContrato) {
        Long totalPorContrato = pagoRepository.countByContrato(idContrato);
        List<Pago> pagosContrato = pagoRepository.findByContrato(idContrato);
        Long pagosConConcepto = pagosContrato.stream()
                .filter(Pago::tieneConcepto)
                .count();
        Long pagosSinConcepto = totalPorContrato - pagosConConcepto;
        BigDecimal montoTotal = pagoRepository.sumMontoByContrato(idContrato);
        
        return new PagoStats(totalPorContrato, pagosConConcepto, pagosSinConcepto, montoTotal);
    }

    // Método para obtener estadísticas por rango de fechas
    public PagoStats getStatsByRangoFechas(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son requeridas");
        }
        if (fechaInicio.after(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }

        List<Pago> pagos = pagoRepository.findByRangoFechas(fechaInicio, fechaFin);
        Long totalPagos = (long) pagos.size();
        Long pagosConConcepto = pagos.stream()
                .filter(Pago::tieneConcepto)
                .count();
        Long pagosSinConcepto = totalPagos - pagosConConcepto;
        BigDecimal montoTotal = pagoRepository.sumMontoByRangoFechas(fechaInicio, fechaFin);
        
        return new PagoStats(totalPagos, pagosConConcepto, pagosSinConcepto, montoTotal);
    }

    // Clase interna para estadísticas
    public static class PagoStats {
        private final Long totalPagos;
        private final Long pagosConConcepto;
        private final Long pagosSinConcepto;
        private final BigDecimal montoTotal;

        public PagoStats(Long totalPagos, Long pagosConConcepto, Long pagosSinConcepto, BigDecimal montoTotal) {
            this.totalPagos = totalPagos;
            this.pagosConConcepto = pagosConConcepto;
            this.pagosSinConcepto = pagosSinConcepto;
            this.montoTotal = montoTotal != null ? montoTotal : BigDecimal.ZERO;
        }

        public Long getTotalPagos() {
            return totalPagos;
        }

        public Long getPagosConConcepto() {
            return pagosConConcepto;
        }

        public Long getPagosSinConcepto() {
            return pagosSinConcepto;
        }

        public BigDecimal getMontoTotal() {
            return montoTotal;
        }

        public BigDecimal getMontoPromedio() {
            if (totalPagos == 0) {
                return BigDecimal.ZERO;
            }
            return montoTotal.divide(BigDecimal.valueOf(totalPagos), 2, BigDecimal.ROUND_HALF_UP);
        }
    }
}