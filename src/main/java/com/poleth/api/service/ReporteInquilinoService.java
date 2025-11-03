// ReporteInquilinoService.java
package com.poleth.api.service;

import com.poleth.api.model.ReporteInquilino;
import com.poleth.api.repository.ReporteInquilinoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    // Método para contar todos los reportes
    public Long countReportesInquilinos() {
        return reporteInquilinoRepository.count();
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

        // Validar longitud de campos
        if (reporteInquilino.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre del reporte no puede exceder 100 caracteres");
        }

        if (reporteInquilino.getTipo() != null && reporteInquilino.getTipo().length() > 50) {
            throw new IllegalArgumentException("El tipo de reporte no puede exceder 50 caracteres");
        }

        if (reporteInquilino.getEstadoReporte() != null && reporteInquilino.getEstadoReporte().length() > 50) {
            throw new IllegalArgumentException("El estado del reporte no puede exceder 50 caracteres");
        }

        // Validar fechas si ambas están presentes
        if (reporteInquilino.getFechaCierre() != null && reporteInquilino.getFecha() != null) {
            if (reporteInquilino.getFechaCierre().isBefore(reporteInquilino.getFecha())) {
                throw new IllegalArgumentException("La fecha de cierre no puede ser anterior a la fecha del reporte");
            }
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

        // Validar longitud de campos
        if (reporteActualizado.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre del reporte no puede exceder 100 caracteres");
        }

        if (reporteActualizado.getTipo() != null && reporteActualizado.getTipo().length() > 50) {
            throw new IllegalArgumentException("El tipo de reporte no puede exceder 50 caracteres");
        }

        if (reporteActualizado.getEstadoReporte() != null && reporteActualizado.getEstadoReporte().length() > 50) {
            throw new IllegalArgumentException("El estado del reporte no puede exceder 50 caracteres");
        }

        // Validar fechas si ambas están presentes
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

    // Método para verificar si un reporte existe por ID
    public boolean existsById(Integer id) {
        return reporteInquilinoRepository.findById(id).isPresent();
    }

    // Métodos específicos para ReporteInquilino
    public List<ReporteInquilino> getReportesByInquilino(Integer idInquilino) {
        return reporteInquilinoRepository.findByIdInquilino(idInquilino);
    }

    public List<ReporteInquilino> getReportesByCuarto(Integer idCuarto) {
        return reporteInquilinoRepository.findByIdCuarto(idCuarto);
    }

    public List<ReporteInquilino> getReportesByTipo(String tipo) {
        return reporteInquilinoRepository.findByTipo(tipo);
    }

    public List<ReporteInquilino> getReportesByEstado(String estadoReporte) {
        return reporteInquilinoRepository.findByEstadoReporte(estadoReporte);
    }

    public List<ReporteInquilino> getReportesAbiertos() {
        return reporteInquilinoRepository.findAbiertos();
    }

    public List<ReporteInquilino> getReportesCerrados() {
        return reporteInquilinoRepository.findCerrados();
    }

    public List<ReporteInquilino> getReportesByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        return reporteInquilinoRepository.findByFechaBetween(fechaInicio, fechaFin);
    }

    public List<ReporteInquilino> getReportesByFechaCierreBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        return reporteInquilinoRepository.findByFechaCierreBetween(fechaInicio, fechaFin);
    }

    public List<ReporteInquilino> getReportesByDescripcionContaining(String texto) {
        return reporteInquilinoRepository.findByDescripcionContaining(texto);
    }

    public List<ReporteInquilino> getReportesByAccionesTomadasContaining(String texto) {
        return reporteInquilinoRepository.findByAccionesTomadasContaining(texto);
    }

    public List<ReporteInquilino> getReportesByNombreContaining(String texto) {
        return reporteInquilinoRepository.findByNombreContaining(texto);
    }

    public List<ReporteInquilino> getReportesAbiertosByInquilino(Integer idInquilino) {
        return reporteInquilinoRepository.findAbiertosByInquilino(idInquilino);
    }

    public List<ReporteInquilino> getReportesAbiertosByCuarto(Integer idCuarto) {
        return reporteInquilinoRepository.findAbiertosByCuarto(idCuarto);
    }

    public List<ReporteInquilino> getReportesRecientes() {
        return reporteInquilinoRepository.findRecientes();
    }

    public List<ReporteInquilino> getReportesByTipoAndEstado(String tipo, String estado) {
        return reporteInquilinoRepository.findByTipoAndEstado(tipo, estado);
    }

    public List<ReporteInquilino> getReportesWithRelations() {
        return reporteInquilinoRepository.findAllWithRelations();
    }

    public Optional<ReporteInquilino> getReporteByIdWithRelations(Integer id) {
        return reporteInquilinoRepository.findByIdWithRelations(id);
    }

    // Métodos de verificación
    public boolean existsByInquilino(Integer idInquilino) {
        return reporteInquilinoRepository.existsByInquilino(idInquilino);
    }

    public boolean existsAbiertosByInquilino(Integer idInquilino) {
        return reporteInquilinoRepository.existsAbiertosByInquilino(idInquilino);
    }

    public boolean existsByCuarto(Integer idCuarto) {
        return reporteInquilinoRepository.existsByCuarto(idCuarto);
    }

    // Métodos de conteo
    public Long countReportesAbiertos() {
        return reporteInquilinoRepository.countAbiertos();
    }

    public Long countReportesCerrados() {
        return reporteInquilinoRepository.countCerrados();
    }

    public Long countReportesByEstado(String estadoReporte) {
        return reporteInquilinoRepository.countByEstado(estadoReporte);
    }

    public Long countReportesByTipo(String tipo) {
        return reporteInquilinoRepository.countByTipo(tipo);
    }

    // Métodos de gestión
    public ReporteInquilino cerrarReporte(Integer idReporte, LocalDate fechaCierre, String accionesTomadas, String estadoFinal) {
        Optional<ReporteInquilino> reporteOpt = reporteInquilinoRepository.findById(idReporte);
        if (reporteOpt.isEmpty()) {
            throw new IllegalArgumentException("Reporte no encontrado con ID: " + idReporte);
        }

        ReporteInquilino reporte = reporteOpt.get();

        // Validar que la fecha de cierre no sea anterior a la fecha del reporte
        if (fechaCierre.isBefore(reporte.getFecha())) {
            throw new IllegalArgumentException("La fecha de cierre no puede ser anterior a la fecha del reporte");
        }

        // Validar longitud del estado
        if (estadoFinal != null && estadoFinal.length() > 50) {
            throw new IllegalArgumentException("El estado del reporte no puede exceder 50 caracteres");
        }

        reporte.setFechaCierre(fechaCierre);
        reporte.setAccionesTomadas(accionesTomadas);
        reporte.setEstadoReporte(estadoFinal);

        return reporteInquilinoRepository.save(reporte);
    }

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

        return reporteInquilinoRepository.save(reporte);
    }

    public ReporteInquilino actualizarAccionesTomadas(Integer idReporte, String nuevasAcciones) {
        Optional<ReporteInquilino> reporteOpt = reporteInquilinoRepository.findById(idReporte);
        if (reporteOpt.isEmpty()) {
            throw new IllegalArgumentException("Reporte no encontrado con ID: " + idReporte);
        }

        ReporteInquilino reporte = reporteOpt.get();
        reporte.setAccionesTomadas(nuevasAcciones);

        return reporteInquilinoRepository.save(reporte);
    }

    // Método para obtener reportes con paginación (simplificado)
    public List<ReporteInquilino> getReportesPaginados(int pagina, int tamaño) {
        List<ReporteInquilino> todosReportes = reporteInquilinoRepository.findAll();
        int inicio = Math.max(0, (pagina - 1) * tamaño);
        int fin = Math.min(todosReportes.size(), inicio + tamaño);
        
        if (inicio >= todosReportes.size()) {
            return List.of();
        }
        
        return todosReportes.subList(inicio, fin);
    }

    // Método para obtener reportes abiertos paginados
    public List<ReporteInquilino> getReportesAbiertosPaginados(int pagina, int tamaño) {
        List<ReporteInquilino> reportesAbiertos = reporteInquilinoRepository.findAbiertos();
        int inicio = Math.max(0, (pagina - 1) * tamaño);
        int fin = Math.min(reportesAbiertos.size(), inicio + tamaño);
        
        if (inicio >= reportesAbiertos.size()) {
            return List.of();
        }
        
        return reportesAbiertos.subList(inicio, fin);
    }

    // Método para obtener reportes por inquilino paginados
    public List<ReporteInquilino> getReportesByInquilinoPaginados(Integer idInquilino, int pagina, int tamaño) {
        List<ReporteInquilino> reportesInquilino = reporteInquilinoRepository.findByIdInquilino(idInquilino);
        int inicio = Math.max(0, (pagina - 1) * tamaño);
        int fin = Math.min(reportesInquilino.size(), inicio + tamaño);
        
        if (inicio >= reportesInquilino.size()) {
            return List.of();
        }
        
        return reportesInquilino.subList(inicio, fin);
    }

    // Método para verificar si un reporte está abierto
    public boolean isReporteAbierto(Integer idReporte) {
        Optional<ReporteInquilino> reporteOpt = reporteInquilinoRepository.findById(idReporte);
        if (reporteOpt.isEmpty()) {
            return false;
        }

        ReporteInquilino reporte = reporteOpt.get();
        return reporte.getFechaCierre() == null;
    }

    // Método para obtener estadísticas de reportes
    public String obtenerEstadisticasReportes() {
        Long total = reporteInquilinoRepository.count();
        Long abiertos = reporteInquilinoRepository.countAbiertos();
        Long cerrados = reporteInquilinoRepository.countCerrados();
        
        double porcentajeCerrados = total > 0 ? (cerrados.doubleValue() / total.doubleValue()) * 100 : 0;
        
        return String.format("Total: %d, Abiertos: %d, Cerrados: %d (%.2f%%)", 
                           total, abiertos, cerrados, porcentajeCerrados);
    }

    // Método para obtener reportes del último mes
    public List<ReporteInquilino> getReportesUltimoMes() {
        LocalDate fechaInicio = LocalDate.now().minusDays(30);
        LocalDate fechaFin = LocalDate.now();
        return reporteInquilinoRepository.findByFechaBetween(fechaInicio, fechaFin);
    }
}