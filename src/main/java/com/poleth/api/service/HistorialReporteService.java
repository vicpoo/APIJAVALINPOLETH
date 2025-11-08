// HistorialReporteService.java
package com.poleth.api.service;

import com.poleth.api.model.HistorialReporte;
import com.poleth.api.model.ReporteInquilino;
import com.poleth.api.repository.HistorialReporteRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Collections;

public class HistorialReporteService {
    private final HistorialReporteRepository historialReporteRepository;

    public HistorialReporteService(HistorialReporteRepository historialReporteRepository) {
        this.historialReporteRepository = historialReporteRepository;
    }

    // Métodos CRUD básicos
    public HistorialReporte saveHistorialReporte(HistorialReporte historialReporte) {
        return historialReporteRepository.save(historialReporte);
    }

    public List<HistorialReporte> getAllHistorialReportes() {
        return historialReporteRepository.findAll();
    }

    public Optional<HistorialReporte> getHistorialReporteById(Integer id) {
        return historialReporteRepository.findById(id);
    }

    public void deleteHistorialReporte(Integer id) {
        historialReporteRepository.delete(id);
    }

    // Método para contar todos los historiales
    public Long countHistorialReportes() {
        return historialReporteRepository.count();
    }

    // Método para crear un nuevo historial con validaciones
    public HistorialReporte createHistorialReporte(HistorialReporte historialReporte) {
        // Validaciones básicas
        if (historialReporte.getIdReporte() == null) {
            throw new IllegalArgumentException("El ID del reporte es requerido");
        }

        if (historialReporte.getNombreReporteHist() == null || historialReporte.getNombreReporteHist().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del historial es requerido");
        }

        if (historialReporte.getDescripcionHist() == null || historialReporte.getDescripcionHist().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del historial es requerida");
        }

        // Validar longitud de campos
        if (historialReporte.getNombreReporteHist().length() > 100) {
            throw new IllegalArgumentException("El nombre del historial no puede exceder 100 caracteres");
        }

        if (historialReporte.getTipoReporteHist() != null && historialReporte.getTipoReporteHist().length() > 50) {
            throw new IllegalArgumentException("El tipo de historial no puede exceder 50 caracteres");
        }

        if (historialReporte.getUsuarioRegistro() != null && historialReporte.getUsuarioRegistro().length() > 50) {
            throw new IllegalArgumentException("El usuario de registro no puede exceder 50 caracteres");
        }

        // Si no se proporciona fecha de registro, establecer la actual
        if (historialReporte.getFechaRegistro() == null) {
            historialReporte.setFechaRegistro(LocalDateTime.now());
        }

        // Guardar el historial
        return historialReporteRepository.save(historialReporte);
    }

    // Método para actualizar un historial existente
    public HistorialReporte updateHistorialReporte(Integer id, HistorialReporte historialActualizado) {
        // Validaciones básicas
        if (historialActualizado.getNombreReporteHist() == null || historialActualizado.getNombreReporteHist().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del historial es requerido");
        }

        if (historialActualizado.getDescripcionHist() == null || historialActualizado.getDescripcionHist().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del historial es requerida");
        }

        // Validar longitud de campos
        if (historialActualizado.getNombreReporteHist().length() > 100) {
            throw new IllegalArgumentException("El nombre del historial no puede exceder 100 caracteres");
        }

        if (historialActualizado.getTipoReporteHist() != null && historialActualizado.getTipoReporteHist().length() > 50) {
            throw new IllegalArgumentException("El tipo de historial no puede exceder 50 caracteres");
        }

        if (historialActualizado.getUsuarioRegistro() != null && historialActualizado.getUsuarioRegistro().length() > 50) {
            throw new IllegalArgumentException("El usuario de registro no puede exceder 50 caracteres");
        }

        // Buscar el historial existente
        Optional<HistorialReporte> historialExistenteOpt = historialReporteRepository.findById(id);
        if (historialExistenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Historial no encontrado con ID: " + id);
        }

        HistorialReporte historialExistente = historialExistenteOpt.get();

        // Actualizar los campos (no actualizamos idReporte ni fechaRegistro para mantener integridad)
        historialExistente.setNombreReporteHist(historialActualizado.getNombreReporteHist());
        historialExistente.setTipoReporteHist(historialActualizado.getTipoReporteHist());
        historialExistente.setDescripcionHist(historialActualizado.getDescripcionHist());
        historialExistente.setUsuarioRegistro(historialActualizado.getUsuarioRegistro());

        // Guardar los cambios
        return historialReporteRepository.save(historialExistente);
    }

    // Método para verificar si un historial existe por ID
    public boolean existsById(Integer id) {
        return historialReporteRepository.findById(id).isPresent();
    }

    // Métodos específicos para HistorialReporte
    public List<HistorialReporte> getHistorialesByReporte(Integer idReporte) {
        return historialReporteRepository.findByIdReporte(idReporte);
    }

    public List<HistorialReporte> getHistorialesByTipo(String tipoReporteHist) {
        return historialReporteRepository.findByTipoReporteHist(tipoReporteHist);
    }

    public List<HistorialReporte> getHistorialesByUsuario(String usuarioRegistro) {
        return historialReporteRepository.findByUsuarioRegistro(usuarioRegistro);
    }

    public List<HistorialReporte> getHistorialesByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return historialReporteRepository.findByFechaRegistroBetween(fechaInicio, fechaFin);
    }

    public List<HistorialReporte> getHistorialesRecientes(int dias) {
        return historialReporteRepository.findRecientes(dias);
    }

    public List<HistorialReporte> getHistorialesByDescripcionContaining(String texto) {
        return historialReporteRepository.findByDescripcionContaining(texto);
    }

    public List<HistorialReporte> getHistorialesByNombreContaining(String texto) {
        return historialReporteRepository.findByNombreContaining(texto);
    }

    public Optional<HistorialReporte> getUltimoHistorialByReporte(Integer idReporte) {
        return historialReporteRepository.findUltimoByReporte(idReporte);
    }

    public List<HistorialReporte> getAllHistorialesOrderByFechaDesc() {
        return historialReporteRepository.findAllOrderByFechaDesc();
    }

    public List<HistorialReporte> getHistorialesByReporteOrderByFechaDesc(Integer idReporte) {
        return historialReporteRepository.findByIdReporteOrderByFechaDesc(idReporte);
    }

    public List<HistorialReporte> getHistorialesWithRelations() {
        return historialReporteRepository.findAllWithRelations();
    }

    public Optional<HistorialReporte> getHistorialByIdWithRelations(Integer id) {
        return historialReporteRepository.findByIdWithRelations(id);
    }

    public List<HistorialReporte> getHistorialesByReporteWithRelations(Integer idReporte) {
        return historialReporteRepository.findByIdReporteWithRelations(idReporte);
    }

    public List<HistorialReporte> getHistorialesByUsuarioWithRelations(String usuarioRegistro) {
        return historialReporteRepository.findByUsuarioRegistroWithRelations(usuarioRegistro);
    }

    // Métodos de verificación
    public boolean existsByReporte(Integer idReporte) {
        return historialReporteRepository.existsByReporte(idReporte);
    }

    public boolean existsByUsuario(String usuarioRegistro) {
        return historialReporteRepository.existsByUsuario(usuarioRegistro);
    }

    // Métodos de conteo
    public Long countHistorialesByReporte(Integer idReporte) {
        return historialReporteRepository.countByReporte(idReporte);
    }

    public Long countHistorialesByUsuario(String usuarioRegistro) {
        return historialReporteRepository.countByUsuario(usuarioRegistro);
    }

    public Long countHistorialesByTipo(String tipoReporteHist) {
        return historialReporteRepository.countByTipo(tipoReporteHist);
    }

    // Métodos de gestión específicos para historial

    // Método para registrar automáticamente un historial a partir de un reporte
    public HistorialReporte registrarHistorialDesdeReporte(ReporteInquilino reporte, String usuarioRegistro) {
        HistorialReporte historial = HistorialReporte.fromReporteInquilino(reporte, usuarioRegistro);
        return historialReporteRepository.save(historial);
    }

    // Método para registrar un historial con acción específica
    public HistorialReporte registrarHistorialConAccion(ReporteInquilino reporte, String accion, String usuarioRegistro) {
        HistorialReporte historial = HistorialReporte.createWithAction(reporte, accion, usuarioRegistro);
        return historialReporteRepository.save(historial);
    }

    // Método para registrar cambio de estado en historial
    public HistorialReporte registrarCambioEstado(ReporteInquilino reporte, String estadoAnterior, String estadoNuevo, String usuarioRegistro) {
        String descripcion = String.format("Cambio de estado: %s → %s. Reporte: %s",
                estadoAnterior,
                estadoNuevo,
                reporte.getDescripcion() != null ? reporte.getDescripcion() : "Sin descripción"
        );

        HistorialReporte historial = new HistorialReporte(
                reporte.getIdReporte(),
                reporte.getNombre(),
                reporte.getTipo(),
                descripcion,
                usuarioRegistro
        );

        return historialReporteRepository.save(historial);
    }

    // Método para registrar cierre de reporte en historial
    public HistorialReporte registrarCierreReporte(ReporteInquilino reporte, String accionesTomadas, String usuarioRegistro) {
        String descripcion = String.format("Reporte cerrado. Acciones tomadas: %s. Reporte original: %s",
                accionesTomadas,
                reporte.getDescripcion() != null ? reporte.getDescripcion() : "Sin descripción"
        );

        HistorialReporte historial = new HistorialReporte(
                reporte.getIdReporte(),
                reporte.getNombre(),
                reporte.getTipo(),
                descripcion,
                usuarioRegistro
        );

        return historialReporteRepository.save(historial);
    }

    // Método para obtener historiales con paginación (simplificado)
    public List<HistorialReporte> getHistorialesPaginados(int pagina, int tamaño) {
        List<HistorialReporte> todosHistoriales = historialReporteRepository.findAll();
        int inicio = Math.max(0, (pagina - 1) * tamaño);
        int fin = Math.min(todosHistoriales.size(), inicio + tamaño);

        if (inicio >= todosHistoriales.size()) {
            return Collections.emptyList();
        }

        return todosHistoriales.subList(inicio, fin);
    }

    // Método para obtener historiales por reporte paginados
    public List<HistorialReporte> getHistorialesByReportePaginados(Integer idReporte, int pagina, int tamaño) {
        List<HistorialReporte> historialesReporte = historialReporteRepository.findByIdReporte(idReporte);
        int inicio = Math.max(0, (pagina - 1) * tamaño);
        int fin = Math.min(historialesReporte.size(), inicio + tamaño);

        if (inicio >= historialesReporte.size()) {
            return Collections.emptyList();
        }

        return historialesReporte.subList(inicio, fin);
    }

    // Método para obtener historiales recientes paginados
    public List<HistorialReporte> getHistorialesRecientesPaginados(int dias, int pagina, int tamaño) {
        List<HistorialReporte> historialesRecientes = historialReporteRepository.findRecientes(dias);
        int inicio = Math.max(0, (pagina - 1) * tamaño);
        int fin = Math.min(historialesRecientes.size(), inicio + tamaño);

        if (inicio >= historialesRecientes.size()) {
            return Collections.emptyList();
        }

        return historialesRecientes.subList(inicio, fin);
    }

    // Método para obtener línea de tiempo de un reporte
    public List<HistorialReporte> getLineaTiempoReporte(Integer idReporte) {
        return historialReporteRepository.findByIdReporteOrderByFechaDesc(idReporte);
    }

    // Método para obtener actividad reciente de un usuario - CORREGIDO
    public List<HistorialReporte> getActividadRecienteUsuario(String usuarioRegistro, int limite) {
        List<HistorialReporte> historiales = historialReporteRepository.findByUsuarioRegistro(usuarioRegistro);
        return historiales.stream()
                .sorted((h1, h2) -> h2.getFechaRegistro().compareTo(h1.getFechaRegistro()))
                .limit(limite)
                .collect(Collectors.toList());
    }

    // Método para obtener estadísticas de actividad
    public String obtenerEstadisticasActividad() {
        Long total = historialReporteRepository.count();
        List<Object[]> estadisticasUsuario = historialReporteRepository.getEstadisticasPorUsuario();
        List<Object[]> estadisticasFecha = historialReporteRepository.getEstadisticasPorFecha();

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Total de registros históricos: %d\n", total));
        sb.append("Actividad por usuario:\n");

        for (Object[] stats : estadisticasUsuario) {
            String usuario = (String) stats[0];
            Long count = (Long) stats[1];
            sb.append(String.format("  %s: %d registros\n", usuario, count));
        }

        sb.append("Actividad reciente por fecha:\n");
        int count = 0;
        for (Object[] stats : estadisticasFecha) {
            if (count >= 5) break; // Mostrar solo las últimas 5 fechas
            java.sql.Date fecha = (java.sql.Date) stats[0];
            Long registros = (Long) stats[1];
            sb.append(String.format("  %s: %d registros\n", fecha.toString(), registros));
            count++;
        }

        return sb.toString();
    }

    // Método para limpiar historiales antiguos (más de X días)
    public int limpiarHistorialesAntiguos(int dias) {
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(dias);
        List<HistorialReporte> historialesAntiguos = historialReporteRepository.findByFechaRegistroBetween(
                LocalDateTime.of(1970, 1, 1, 0, 0), // Fecha muy antigua
                fechaLimite
        );

        int eliminados = 0;
        for (HistorialReporte historial : historialesAntiguos) {
            historialReporteRepository.delete(historial.getIdHistorial());
            eliminados++;
        }

        return eliminados;
    }

    // Método para verificar si un reporte tiene historial reciente
    public boolean tieneHistorialReciente(Integer idReporte, int horas) {
        Optional<HistorialReporte> ultimoHistorial = historialReporteRepository.findUltimoByReporte(idReporte);
        if (ultimoHistorial.isEmpty()) {
            return false;
        }

        LocalDateTime fechaLimite = LocalDateTime.now().minusHours(horas);
        return ultimoHistorial.get().getFechaRegistro().isAfter(fechaLimite);
    }
}