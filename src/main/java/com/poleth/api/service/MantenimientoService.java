// MantenimientoService.java
package com.poleth.api.service;

import com.poleth.api.model.Mantenimiento;
import com.poleth.api.repository.MantenimientoRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MantenimientoService {
    private final MantenimientoRepository mantenimientoRepository;

    public MantenimientoService(MantenimientoRepository mantenimientoRepository) {
        this.mantenimientoRepository = mantenimientoRepository;
    }

    // Métodos CRUD básicos
    public List<Mantenimiento> getAllMantenimientos() {
        return mantenimientoRepository.findAll();
    }

    public Optional<Mantenimiento> getMantenimientoById(Integer id) {
        return mantenimientoRepository.findById(id);
    }

    public void deleteMantenimiento(Integer id) {
        mantenimientoRepository.delete(id);
    }

    // Método para crear un nuevo mantenimiento con validaciones
    public Mantenimiento createMantenimiento(Mantenimiento mantenimiento) {
        // Validaciones básicas
        if (mantenimiento.getIdCuarto() == null) {
            throw new IllegalArgumentException("El ID del cuarto es requerido");
        }

        if (mantenimiento.getFechaReporte() == null) {
            throw new IllegalArgumentException("La fecha de reporte es requerida");
        }

        // Validar que la fecha de reporte no sea en el futuro
        if (mantenimiento.getFechaReporte().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de reporte no puede ser en el futuro");
        }

        // Validar descripción del problema
        if (mantenimiento.getDescripcionProblema() == null || mantenimiento.getDescripcionProblema().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del problema es requerida");
        }

        // Validar longitud del estado
        if (mantenimiento.getEstadoMantenimiento() != null && mantenimiento.getEstadoMantenimiento().length() > 50) {
            throw new IllegalArgumentException("El estado del mantenimiento no puede exceder 50 caracteres");
        }

        // Validar costo no negativo
        if (mantenimiento.getCostoMantenimiento() != null && mantenimiento.getCostoMantenimiento().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El costo de mantenimiento no puede ser negativo");
        }

        // Validar costo máximo (10,2 decimales)
        if (mantenimiento.getCostoMantenimiento() != null) {
            BigDecimal costoMaximo = new BigDecimal("99999999.99");
            if (mantenimiento.getCostoMantenimiento().compareTo(costoMaximo) > 0) {
                throw new IllegalArgumentException("El costo de mantenimiento no puede exceder 99,999,999.99");
            }
        }

        // Validar fechas si ambas están presentes
        if (mantenimiento.getFechaAtencion() != null && mantenimiento.getFechaReporte() != null) {
            if (mantenimiento.getFechaAtencion().isBefore(mantenimiento.getFechaReporte())) {
                throw new IllegalArgumentException("La fecha de atención no puede ser anterior a la fecha de reporte");
            }
        }

        // Establecer valores por defecto si no están presentes
        if (mantenimiento.getEstadoMantenimiento() == null || mantenimiento.getEstadoMantenimiento().trim().isEmpty()) {
            mantenimiento.setEstadoMantenimiento("pendiente");
        }

        if (mantenimiento.getCostoMantenimiento() == null) {
            mantenimiento.setCostoMantenimiento(BigDecimal.ZERO);
        }

        // Guardar el mantenimiento
        return mantenimientoRepository.save(mantenimiento);
    }

    // Método para actualizar un mantenimiento existente
    public Mantenimiento updateMantenimiento(Integer id, Mantenimiento mantenimientoActualizado) {
        // Validaciones básicas
        if (mantenimientoActualizado.getFechaReporte() == null) {
            throw new IllegalArgumentException("La fecha de reporte es requerida");
        }

        // Validar descripción del problema
        if (mantenimientoActualizado.getDescripcionProblema() == null ||
                mantenimientoActualizado.getDescripcionProblema().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del problema es requerida");
        }

        // Validar longitud del estado
        if (mantenimientoActualizado.getEstadoMantenimiento() != null &&
                mantenimientoActualizado.getEstadoMantenimiento().length() > 50) {
            throw new IllegalArgumentException("El estado del mantenimiento no puede exceder 50 caracteres");
        }

        // Validar costo no negativo
        if (mantenimientoActualizado.getCostoMantenimiento() != null &&
                mantenimientoActualizado.getCostoMantenimiento().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El costo de mantenimiento no puede ser negativo");
        }

        // Validar costo máximo
        if (mantenimientoActualizado.getCostoMantenimiento() != null) {
            BigDecimal costoMaximo = new BigDecimal("99999999.99");
            if (mantenimientoActualizado.getCostoMantenimiento().compareTo(costoMaximo) > 0) {
                throw new IllegalArgumentException("El costo de mantenimiento no puede exceder 99,999,999.99");
            }
        }

        // Validar fechas si ambas están presentes
        if (mantenimientoActualizado.getFechaAtencion() != null && mantenimientoActualizado.getFechaReporte() != null) {
            if (mantenimientoActualizado.getFechaAtencion().isBefore(mantenimientoActualizado.getFechaReporte())) {
                throw new IllegalArgumentException("La fecha de atención no puede ser anterior a la fecha de reporte");
            }
        }

        // Buscar el mantenimiento existente
        Optional<Mantenimiento> mantenimientoExistenteOpt = mantenimientoRepository.findById(id);
        if (mantenimientoExistenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Mantenimiento no encontrado con ID: " + id);
        }

        Mantenimiento mantenimientoExistente = mantenimientoExistenteOpt.get();

        // Actualizar los campos
        mantenimientoExistente.setIdCuarto(mantenimientoActualizado.getIdCuarto());
        mantenimientoExistente.setFechaReporte(mantenimientoActualizado.getFechaReporte());
        mantenimientoExistente.setDescripcionProblema(mantenimientoActualizado.getDescripcionProblema());
        mantenimientoExistente.setEstadoMantenimiento(mantenimientoActualizado.getEstadoMantenimiento());
        mantenimientoExistente.setFechaAtencion(mantenimientoActualizado.getFechaAtencion());
        mantenimientoExistente.setCostoMantenimiento(mantenimientoActualizado.getCostoMantenimiento());

        // Guardar los cambios
        return mantenimientoRepository.save(mantenimientoExistente);
    }

    // Método para verificar si un mantenimiento existe por ID
    public boolean existsById(Integer id) {
        return mantenimientoRepository.findById(id).isPresent();
    }

    // Método para atender un mantenimiento
    public Mantenimiento atenderMantenimiento(Integer idMantenimiento, LocalDate fechaAtencion,
                                              BigDecimal costo, String estado) {
        Optional<Mantenimiento> mantenimientoOpt = mantenimientoRepository.findById(idMantenimiento);
        if (mantenimientoOpt.isEmpty()) {
            throw new IllegalArgumentException("Mantenimiento no encontrado con ID: " + idMantenimiento);
        }

        Mantenimiento mantenimiento = mantenimientoOpt.get();

        // Validar que la fecha de atención no sea anterior a la fecha de reporte
        if (fechaAtencion.isBefore(mantenimiento.getFechaReporte())) {
            throw new IllegalArgumentException("La fecha de atención no puede ser anterior a la fecha de reporte");
        }

        // Validar costo no negativo
        if (costo != null && costo.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El costo de mantenimiento no puede ser negativo");
        }

        // Validar costo máximo
        if (costo != null) {
            BigDecimal costoMaximo = new BigDecimal("99999999.99");
            if (costo.compareTo(costoMaximo) > 0) {
                throw new IllegalArgumentException("El costo de mantenimiento no puede exceder 99,999,999.99");
            }
        }

        // Validar longitud del estado
        if (estado != null && estado.length() > 50) {
            throw new IllegalArgumentException("El estado del mantenimiento no puede exceder 50 caracteres");
        }

        mantenimiento.setFechaAtencion(fechaAtencion);

        if (costo != null) {
            mantenimiento.setCostoMantenimiento(costo);
        }

        if (estado != null) {
            mantenimiento.setEstadoMantenimiento(estado);
        }

        // Guardar
        return mantenimientoRepository.save(mantenimiento);
    }

    // Método para actualizar solo el estado del mantenimiento
    public Mantenimiento actualizarEstadoMantenimiento(Integer idMantenimiento, String nuevoEstado) {
        Optional<Mantenimiento> mantenimientoOpt = mantenimientoRepository.findById(idMantenimiento);
        if (mantenimientoOpt.isEmpty()) {
            throw new IllegalArgumentException("Mantenimiento no encontrado con ID: " + idMantenimiento);
        }

        // Validar longitud del estado
        if (nuevoEstado != null && nuevoEstado.length() > 50) {
            throw new IllegalArgumentException("El estado del mantenimiento no puede exceder 50 caracteres");
        }

        Mantenimiento mantenimiento = mantenimientoOpt.get();
        mantenimiento.setEstadoMantenimiento(nuevoEstado);

        // Guardar
        return mantenimientoRepository.save(mantenimiento);
    }

    // Métodos de consulta adicionales
    public List<Mantenimiento> getMantenimientosByCuarto(Integer idCuarto) {
        return mantenimientoRepository.findByIdCuarto(idCuarto);
    }

    public List<Mantenimiento> getMantenimientosByEstado(String estado) {
        return mantenimientoRepository.findByEstadoMantenimiento(estado);
    }

    public List<Mantenimiento> getMantenimientosPendientes() {
        return mantenimientoRepository.findPendientes();
    }

    public List<Mantenimiento> getMantenimientosCompletados() {
        return mantenimientoRepository.findCompletados();
    }

    public List<Mantenimiento> getMantenimientosByFechaReporteBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        return mantenimientoRepository.findByFechaReporteBetween(fechaInicio, fechaFin);
    }

    public List<Mantenimiento> getMantenimientosPendientesByCuarto(Integer idCuarto) {
        return mantenimientoRepository.findPendientesByCuarto(idCuarto);
    }

    // Métodos de estadísticas
    public Long contarTotalMantenimientos() {
        return mantenimientoRepository.count();
    }

    public Long contarMantenimientosPendientes() {
        return mantenimientoRepository.countPendientes();
    }

    public Long contarMantenimientosCompletados() {
        return mantenimientoRepository.countCompletados();
    }

    public BigDecimal calcularCostoTotalMantenimientos() {
        List<Mantenimiento> mantenimientos = mantenimientoRepository.findAll();
        BigDecimal total = BigDecimal.ZERO;

        for (Mantenimiento m : mantenimientos) {
            if (m.getCostoMantenimiento() != null) {
                total = total.add(m.getCostoMantenimiento());
            }
        }

        return total;
    }
}