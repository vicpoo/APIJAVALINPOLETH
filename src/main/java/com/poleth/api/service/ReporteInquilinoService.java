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
    public List<ReporteInquilino> getAllReportesInquilinos() {
        return reporteInquilinoRepository.findAll();
    }

    public Optional<ReporteInquilino> getReporteInquilinoById(Integer id) {
        return reporteInquilinoRepository.findById(id);
    }

    public void deleteReporteInquilino(Integer id) {
        reporteInquilinoRepository.delete(id);
    }

    // Método para verificar si un reporte existe por ID
    public boolean existsById(Integer id) {
        return reporteInquilinoRepository.findById(id).isPresent();
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

        // Validar longitud del nombre
        if (reporteInquilino.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
        }

        // Validar longitud del tipo
        if (reporteInquilino.getTipo() != null && reporteInquilino.getTipo().length() > 50) {
            throw new IllegalArgumentException("El tipo no puede exceder 50 caracteres");
        }

        // Validar descripción del reporte
        if (reporteInquilino.getDescripcion() == null || reporteInquilino.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del reporte es requerida");
        }

        // Validar ID del cuarto (requerido según la tabla)
        if (reporteInquilino.getIdCuarto() == null) {
            throw new IllegalArgumentException("El ID del cuarto es requerido");
        }

        // Validar estado del reporte
        if (reporteInquilino.getEstadoReporte() != null && reporteInquilino.getEstadoReporte().length() > 50) {
            throw new IllegalArgumentException("El estado del reporte no puede exceder 50 caracteres");
        }

        // Validar que la fecha no sea en el futuro
        if (reporteInquilino.getFecha() != null && reporteInquilino.getFecha().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha del reporte no puede ser en el futuro");
        }

        // Validar fecha de cierre (si se proporciona)
        if (reporteInquilino.getFechaCierre() != null) {
            if (reporteInquilino.getFecha() != null &&
                    reporteInquilino.getFechaCierre().isBefore(reporteInquilino.getFecha())) {
                throw new IllegalArgumentException("La fecha de cierre no puede ser anterior a la fecha del reporte");
            }
        }

        // Estado por defecto si no se especifica
        if (reporteInquilino.getEstadoReporte() == null ||
                reporteInquilino.getEstadoReporte().trim().isEmpty()) {
            reporteInquilino.setEstadoReporte("abierto");
        }

        // Fecha por defecto si no se especifica
        if (reporteInquilino.getFecha() == null) {
            reporteInquilino.setFecha(LocalDate.now());
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

        if (reporteActualizado.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
        }

        if (reporteActualizado.getTipo() != null && reporteActualizado.getTipo().length() > 50) {
            throw new IllegalArgumentException("El tipo no puede exceder 50 caracteres");
        }

        if (reporteActualizado.getDescripcion() == null || reporteActualizado.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del reporte es requerida");
        }

        if (reporteActualizado.getIdCuarto() == null) {
            throw new IllegalArgumentException("El ID del cuarto es requerido");
        }

        if (reporteActualizado.getEstadoReporte() != null && reporteActualizado.getEstadoReporte().length() > 50) {
            throw new IllegalArgumentException("El estado del reporte no puede exceder 50 caracteres");
        }

        // Validar fechas
        if (reporteActualizado.getFecha() != null && reporteActualizado.getFecha().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha del reporte no puede ser en el futuro");
        }

        if (reporteActualizado.getFechaCierre() != null && reporteActualizado.getFecha() != null) {
            if (reporteActualizado.getFechaCierre().isBefore(reporteActualizado.getFecha())) {
                throw new IllegalArgumentException("La fecha de cierre no puede ser anterior a la fecha del reporte");
            }
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
        reporteExistente.setFechaCierre(reporteActualizado.getFechaCierre());
        reporteExistente.setAccionesTomadas(reporteActualizado.getAccionesTomadas());

        // Guardar los cambios
        return reporteInquilinoRepository.save(reporteExistente);
    }

    // Método para cerrar un reporte
    public ReporteInquilino cerrarReporte(Integer idReporte, String accionesTomadas, String estado) {
        Optional<ReporteInquilino> reporteOpt = reporteInquilinoRepository.findById(idReporte);
        if (reporteOpt.isEmpty()) {
            throw new IllegalArgumentException("Reporte no encontrado con ID: " + idReporte);
        }

        ReporteInquilino reporte = reporteOpt.get();

        // Validar acciones tomadas
        if (accionesTomadas == null || accionesTomadas.trim().isEmpty()) {
            throw new IllegalArgumentException("Las acciones tomadas son requeridas para cerrar un reporte");
        }

        // Estado por defecto si no se especifica
        String estadoFinal = (estado != null && !estado.trim().isEmpty()) ? estado : "cerrado";

        // Validar longitud del estado
        if (estadoFinal.length() > 50) {
            throw new IllegalArgumentException("El estado del reporte no puede exceder 50 caracteres");
        }

        // Actualizar el reporte
        reporte.setEstadoReporte(estadoFinal);
        reporte.setFechaCierre(LocalDate.now());
        reporte.setAccionesTomadas(accionesTomadas);

        // Guardar
        return reporteInquilinoRepository.save(reporte);
    }

    // Método para actualizar solo el estado del reporte
    public ReporteInquilino actualizarEstadoReporte(Integer idReporte, String nuevoEstado) {
        Optional<ReporteInquilino> reporteOpt = reporteInquilinoRepository.findById(idReporte);
        if (reporteOpt.isEmpty()) {
            throw new IllegalArgumentException("Reporte no encontrado con ID: " + idReporte);
        }

        // Validar longitud del estado
        if (nuevoEstado != null && nuevoEstado.length() > 50) {
            throw new IllegalArgumentException("El estado del reporte no puede exceder 50 caracteres");
        }

        ReporteInquilino reporte = reporteOpt.get();
        reporte.setEstadoReporte(nuevoEstado);

        // Si se cierra el reporte, establecer fecha de cierre
        if ("cerrado".equalsIgnoreCase(nuevoEstado) ||
                "resuelto".equalsIgnoreCase(nuevoEstado) ||
                "completado".equalsIgnoreCase(nuevoEstado)) {
            if (reporte.getFechaCierre() == null) {
                reporte.setFechaCierre(LocalDate.now());
            }
        }

        // Guardar
        return reporteInquilinoRepository.save(reporte);
    }

    // Métodos de consulta
    public List<ReporteInquilino> getReportesByInquilino(Integer idInquilino) {
        return reporteInquilinoRepository.findByIdInquilino(idInquilino);
    }

    public List<ReporteInquilino> getReportesByCuarto(Integer idCuarto) {
        return reporteInquilinoRepository.findByIdCuarto(idCuarto);
    }

    public List<ReporteInquilino> getReportesByEstado(String estado) {
        return reporteInquilinoRepository.findByEstadoReporte(estado);
    }

    public List<ReporteInquilino> getReportesAbiertos() {
        return reporteInquilinoRepository.findReportesAbiertos();
    }

    public List<ReporteInquilino> getReportesCerrados() {
        return reporteInquilinoRepository.findReportesCerrados();
    }

    public List<ReporteInquilino> getReportesByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        return reporteInquilinoRepository.findByFechaBetween(fechaInicio, fechaFin);
    }

    public List<ReporteInquilino> buscarReportesPorTexto(String texto) {
        return reporteInquilinoRepository.findByTexto(texto);
    }

    // Método para obtener estadísticas de tipos de reportes
    public Map<String, Integer> getEstadisticasTiposReportes() {
        List<Object[]> resultados = reporteInquilinoRepository.getEstadisticasTiposReportes();
        Map<String, Integer> estadisticas = new LinkedHashMap<>();

        // Inicializar con tipos comunes
        String[] tiposComunes = {"Mantenimiento", "Reparacion", "Limpieza", "Seguridad", "Ruido", "Otro"};
        for (String tipo : tiposComunes) {
            estadisticas.put(tipo, 0);
        }

        // Llenar con datos reales
        for (Object[] resultado : resultados) {
            String tipo = (String) resultado[0];
            Long count = (Long) resultado[1];

            if (tipo != null) {
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
            case "ruido":
            case "ruidos":
                return "Ruido";
            case "otro":
            case "otros":
                return "Otro";
            default:
                return "Otro";
        }
    }

    // Método para estadísticas completas
    public Map<String, Object> getEstadisticasCompletas() {
        Map<String, Object> estadisticas = new HashMap<>();

        // Estadísticas de tipos
        Map<String, Integer> tiposStats = getEstadisticasTiposReportes();
        estadisticas.put("tiposReportes", tiposStats);

        // Totales
        int totalReportes = reporteInquilinoRepository.countTotal();
        int totalAbiertos = reporteInquilinoRepository.countByEstado("abierto");
        int totalCerrados = reporteInquilinoRepository.countByEstado("cerrado");

        estadisticas.put("totalReportes", totalReportes);
        estadisticas.put("totalAbiertos", totalAbiertos);
        estadisticas.put("totalCerrados", totalCerrados);
        estadisticas.put("porcentajeCerrados", totalReportes > 0 ?
                (totalCerrados * 100.0 / totalReportes) : 0);

        // Tipo más común
        String tipoMasComun = tiposStats.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Ninguno");
        estadisticas.put("tipoMasComun", tipoMasComun);

        // Reportes por mes (últimos 6 meses)
        Map<String, Integer> reportesPorMes = new LinkedHashMap<>();
        LocalDate ahora = LocalDate.now();
        for (int i = 5; i >= 0; i--) {
            LocalDate fechaMes = ahora.minusMonths(i);
            String mesKey = fechaMes.getYear() + "-" + fechaMes.getMonthValue();
            int count = reporteInquilinoRepository.countByMes(fechaMes.getYear(), fechaMes.getMonthValue());
            reportesPorMes.put(mesKey, count);
        }
        estadisticas.put("reportesPorMes", reportesPorMes);

        return estadisticas;
    }

    // Método para obtener reportes recientes
    public List<ReporteInquilino> getReportesRecientes(int limite) {
        return reporteInquilinoRepository.findRecientes(limite);
    }

    // Método para obtener reportes urgentes
    public List<ReporteInquilino> getReportesUrgentes() {
        return reporteInquilinoRepository.findUrgentes();
    }
}