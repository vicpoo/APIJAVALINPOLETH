// ReporteInquilinoService.java
package com.poleth.api.service;

import com.poleth.api.model.ReporteInquilino;
import com.poleth.api.repository.ReporteInquilinoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ReporteInquilinoService {
    private final ReporteInquilinoRepository reporteInquilinoRepository;

    public ReporteInquilinoService(ReporteInquilinoRepository reporteInquilinoRepository) {
        this.reporteInquilinoRepository = reporteInquilinoRepository;
    }

    // Métodos CRUD básicos
    public ReporteInquilino saveReporteInquilino(ReporteInquilino reporteInquilino) {
        return reporteInquilinoRepository.save(reporteInquilino);
    }

    public List<ReporteInquilino> getAllReportesInquilinos() {
        return reporteInquilinoRepository.findAll();
    }

    public Optional<ReporteInquilino> getReporteInquilinoById(Integer id) {
        return reporteInquilinoRepository.findById(id);
    }

    public void deleteReporteInquilino(Integer id) {
        reporteInquilinoRepository.delete(id);
    }

    // Método para crear un nuevo reporte con validaciones
    public ReporteInquilino createReporteInquilino(ReporteInquilino reporteInquilino) {
        // Validaciones básicas
        if (reporteInquilino.getIdInquilino() == null) {
            throw new IllegalArgumentException("El ID del inquilino es requerido");
        }

        if (reporteInquilino.getNombre() == null || reporteInquilino.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del reporte es requerido");
        }

        if (reporteInquilino.getDescripcion() == null || reporteInquilino.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del reporte es requerida");
        }

        if (reporteInquilino.getFecha() == null) {
            throw new IllegalArgumentException("La fecha del reporte es requerida");
        }

        // Validar que la fecha no sea en el futuro
        if (reporteInquilino.getFecha().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha del reporte no puede ser en el futuro");
        }

        // Guardar el reporte
        return reporteInquilinoRepository.save(reporteInquilino);
    }

    // Método para actualizar un reporte existente
    public ReporteInquilino updateReporteInquilino(Integer id, ReporteInquilino reporteActualizado) {
        // Validaciones básicas
        if (reporteActualizado.getNombre() == null || reporteActualizado.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del reporte es requerido");
        }

        if (reporteActualizado.getDescripcion() == null || reporteActualizado.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del reporte es requerida");
        }

        if (reporteActualizado.getFecha() == null) {
            throw new IllegalArgumentException("La fecha del reporte es requerida");
        }

        // Buscar el reporte existente
        Optional<ReporteInquilino> reporteExistenteOpt = reporteInquilinoRepository.findById(id);
        if (reporteExistenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Reporte no encontrado con ID: " + id);
        }

        ReporteInquilino reporteExistente = reporteExistenteOpt.get();

        // Actualizar los campos
        reporteExistente.setIdInquilino(reporteActualizado.getIdInquilino());
        reporteExistente.setNombre(reporteActualizado.getNombre());
        reporteExistente.setTipo(reporteActualizado.getTipo());
        reporteExistente.setDescripcion(reporteActualizado.getDescripcion());
        reporteExistente.setFecha(reporteActualizado.getFecha());
        reporteExistente.setIdCuarto(reporteActualizado.getIdCuarto());
        reporteExistente.setEstadoReporte(reporteActualizado.getEstadoReporte());

        // Guardar los cambios
        return reporteInquilinoRepository.save(reporteExistente);
    }

    // Métodos específicos para ReporteInquilino
    public List<ReporteInquilino> getReportesByInquilino(Integer idInquilino) {
        return reporteInquilinoRepository.findByIdInquilino(idInquilino);
    }

    public List<ReporteInquilino> getReportesByCuarto(Integer idCuarto) {
        return reporteInquilinoRepository.findByIdCuarto(idCuarto);
    }

    // NUEVO MÉTODO: Obtener estadísticas para gráfica de barras
    public Map<String, Integer> getEstadisticasTiposReportes() {
        List<Object[]> resultados = reporteInquilinoRepository.getEstadisticasTiposReportes();
        Map<String, Integer> estadisticas = new LinkedHashMap<>();

        // Inicializar con todos los tipos posibles en 0
        String[] tiposEsperados = {"Mantenimiento", "Reparacion", "Limpieza", "Seguridad", "Otro"};
        for (String tipo : tiposEsperados) {
            estadisticas.put(tipo, 0);
        }

        // Llenar con los datos reales de la base de datos
        for (Object[] resultado : resultados) {
            String tipo = (String) resultado[0];
            Long count = (Long) resultado[1];

            if (tipo != null) {
                // Normalizar el nombre del tipo
                String tipoNormalizado = normalizarTipo(tipo);
                estadisticas.put(tipoNormalizado, count.intValue());
            }
        }

        return estadisticas;
    }

    // Método auxiliar para normalizar tipos
    private String normalizarTipo(String tipo) {
        if (tipo == null) return "Otro";

        String tipoLower = tipo.toLowerCase().trim();
        switch (tipoLower) {
            case "mantenimiento":
            case "mantenimientos":
                return "Mantenimiento";
            case "reparacion":
            case "reparaciones":
                return "Reparacion";
            case "limpieza":
            case "limpiezas":
                return "Limpieza";
            case "seguridad":
                return "Seguridad";
            case "otro":
            case "otros":
                return "Otro";
            default:
                return "Otro";
        }
    }

    // Método alternativo para estadísticas más detalladas
    public Map<String, Object> getEstadisticasCompletas() {
        Map<String, Object> estadisticas = new HashMap<>();

        // Obtener estadísticas de tipos
        Map<String, Integer> tiposStats = getEstadisticasTiposReportes();
        estadisticas.put("tiposReportes", tiposStats);

        // Calcular total
        int total = tiposStats.values().stream().mapToInt(Integer::intValue).sum();
        estadisticas.put("totalReportes", total);

        // Encontrar el tipo más común
        String tipoMasComun = tiposStats.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Ninguno");
        estadisticas.put("tipoMasComun", tipoMasComun);

        return estadisticas;
    }
}