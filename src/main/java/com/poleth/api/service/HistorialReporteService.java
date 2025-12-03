// HistorialReporteService.java
// HistorialReporteService.java
package com.poleth.api.service;

import com.poleth.api.model.HistorialReporte;
import com.poleth.api.repository.HistorialReporteRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class HistorialReporteService {
    private final HistorialReporteRepository historialReporteRepository;

    public HistorialReporteService(HistorialReporteRepository historialReporteRepository) {
        this.historialReporteRepository = historialReporteRepository;
    }

    // Métodos CRUD básicos
    public List<HistorialReporte> getAllHistorialReportes() {
        return historialReporteRepository.findAll();
    }

    public Optional<HistorialReporte> getHistorialReporteById(Integer id) {
        return historialReporteRepository.findById(id);
    }

    public void deleteHistorialReporte(Integer id) {
        historialReporteRepository.delete(id);
    }

    // Método para verificar si un historial existe por ID
    public boolean existsById(Integer id) {
        return historialReporteRepository.findById(id).isPresent();
    }

    // Método para crear un nuevo historial con validaciones
    public HistorialReporte createHistorialReporte(HistorialReporte historialReporte) {
        // Validaciones básicas
        if (historialReporte.getIdReporte() == null) {
            throw new IllegalArgumentException("El ID del reporte es requerido");
        }

        if (historialReporte.getNombreReporteHist() == null ||
                historialReporte.getNombreReporteHist().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del historial es requerido");
        }

        // Validar longitud del nombre
        if (historialReporte.getNombreReporteHist().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
        }

        // Validar longitud del tipo
        if (historialReporte.getTipoReporteHist() != null &&
                historialReporte.getTipoReporteHist().length() > 50) {
            throw new IllegalArgumentException("El tipo no puede exceder 50 caracteres");
        }

        // Validar descripción del historial
        if (historialReporte.getDescripcionHist() == null ||
                historialReporte.getDescripcionHist().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del historial es requerida");
        }

        // Validar longitud del usuario
        if (historialReporte.getUsuarioRegistro() != null &&
                historialReporte.getUsuarioRegistro().length() > 50) {
            throw new IllegalArgumentException("El usuario de registro no puede exceder 50 caracteres");
        }

        // Guardar el historial
        return historialReporteRepository.save(historialReporte);
    }

    // Método para actualizar un historial existente
    public HistorialReporte updateHistorialReporte(Integer id, HistorialReporte historialActualizado) {
        // Validaciones básicas
        if (historialActualizado.getNombreReporteHist() == null ||
                historialActualizado.getNombreReporteHist().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del historial es requerido");
        }

        if (historialActualizado.getNombreReporteHist().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
        }

        if (historialActualizado.getTipoReporteHist() != null &&
                historialActualizado.getTipoReporteHist().length() > 50) {
            throw new IllegalArgumentException("El tipo no puede exceder 50 caracteres");
        }

        if (historialActualizado.getDescripcionHist() == null ||
                historialActualizado.getDescripcionHist().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del historial es requerida");
        }

        if (historialActualizado.getUsuarioRegistro() != null &&
                historialActualizado.getUsuarioRegistro().length() > 50) {
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

    // Método para crear historial a partir de reporte
    public HistorialReporte crearHistorialDesdeReporte(Integer idReporte, String usuarioRegistro,
                                                       String accion, String detalles) {
        // Aquí deberías tener acceso a ReporteInquilinoService para obtener el reporte
        // Por ahora, asumimos que el reporte existe
        // En una implementación completa, obtendrías el reporte aquí

        HistorialReporte historial = new HistorialReporte();
        historial.setIdReporte(idReporte);
        historial.setUsuarioRegistro(usuarioRegistro);

        // Estos campos normalmente vendrían del reporte original
        // En una implementación completa, los obtendrías del reporte
        historial.setNombreReporteHist("Historial del reporte " + idReporte);
        historial.setTipoReporteHist("Historial");

        if (accion != null && !accion.trim().isEmpty()) {
            historial.setDescripcionHist("Acción: " + accion + "\n" +
                    "Detalles: " + (detalles != null ? detalles : "Sin detalles") + "\n" +
                    "Usuario: " + usuarioRegistro);
        } else {
            historial.setDescripcionHist("Registro histórico creado por: " + usuarioRegistro);
        }

        return createHistorialReporte(historial);
    }

    // Métodos de consulta
    public List<HistorialReporte> getHistorialesByReporte(Integer idReporte) {
        return historialReporteRepository.findByIdReporte(idReporte);
    }

    public List<HistorialReporte> getHistorialesByTipo(String tipoReporteHist) {
        return historialReporteRepository.findByTipoReporteHist(tipoReporteHist);
    }

    public List<HistorialReporte> getHistorialesByUsuario(String usuarioRegistro) {
        return historialReporteRepository.findByUsuarioRegistro(usuarioRegistro);
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

    public List<HistorialReporte> getHistorialesByDescripcionContaining(String texto) {
        return historialReporteRepository.findByDescripcionContaining(texto);
    }

    public List<HistorialReporte> getHistorialesByNombreContaining(String texto) {
        return historialReporteRepository.findByNombreContaining(texto);
    }

    // Método para obtener historiales recientes (últimos 7 días)
    public List<HistorialReporte> getHistorialesRecientes() {
        List<HistorialReporte> todosHistoriales = getAllHistorialesOrderByFechaDesc();
        LocalDateTime unaSemanaAtras = LocalDateTime.now().minusDays(7);

        return todosHistoriales.stream()
                .filter(h -> h.getFechaRegistro() != null && h.getFechaRegistro().isAfter(unaSemanaAtras))
                .collect(Collectors.toList()); // CORREGIDO: Cambiado toList() por collect(Collectors.toList())
    }

    // Método para obtener historiales del día actual
    public List<HistorialReporte> getHistorialesDelDia() {
        List<HistorialReporte> todosHistoriales = getAllHistorialesOrderByFechaDesc();
        LocalDate hoy = LocalDate.now();

        return todosHistoriales.stream()
                .filter(h -> h.getFechaRegistro() != null &&
                        h.getFechaRegistro().toLocalDate().equals(hoy))
                .collect(Collectors.toList()); // CORREGIDO: Cambiado toList() por collect(Collectors.toList())
    }

    // Método para obtener estadísticas de historiales
    public Map<String, Object> getEstadisticasHistoriales() {
        Map<String, Object> estadisticas = new LinkedHashMap<>();

        // Total de historiales
        int totalHistoriales = historialReporteRepository.countTotal();
        estadisticas.put("totalHistoriales", totalHistoriales);

        // Historiales por tipo
        Map<String, Integer> historialesPorTipo = new HashMap<>();
        List<HistorialReporte> todosHistoriales = getAllHistorialReportes();

        for (HistorialReporte historial : todosHistoriales) {
            String tipo = historial.getTipoReporteHist();
            if (tipo == null || tipo.trim().isEmpty()) {
                tipo = "Sin tipo";
            }
            historialesPorTipo.put(tipo, historialesPorTipo.getOrDefault(tipo, 0) + 1);
        }
        estadisticas.put("historialesPorTipo", historialesPorTipo);

        // Historiales por usuario
        Map<String, Integer> historialesPorUsuario = new HashMap<>();
        for (HistorialReporte historial : todosHistoriales) {
            String usuario = historial.getUsuarioRegistro();
            if (usuario == null || usuario.trim().isEmpty()) {
                usuario = "Usuario desconocido";
            }
            historialesPorUsuario.put(usuario, historialesPorUsuario.getOrDefault(usuario, 0) + 1);
        }
        estadisticas.put("historialesPorUsuario", historialesPorUsuario);

        // Historiales recientes (último mes)
        LocalDateTime unMesAtras = LocalDateTime.now().minusMonths(1);
        long historialesRecientes = todosHistoriales.stream()
                .filter(h -> h.getFechaRegistro() != null && h.getFechaRegistro().isAfter(unMesAtras))
                .count();
        estadisticas.put("historialesRecientesUltimoMes", historialesRecientes);

        // Usuario más activo
        String usuarioMasActivo = historialesPorUsuario.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Ninguno");
        estadisticas.put("usuarioMasActivo", usuarioMasActivo);

        // Tipo más común
        String tipoMasComun = historialesPorTipo.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Ninguno");
        estadisticas.put("tipoMasComun", tipoMasComun);

        return estadisticas;
    }

    // Método para contar historiales por reporte
    public int countHistorialesByReporte(Integer idReporte) {
        List<HistorialReporte> historiales = getHistorialesByReporte(idReporte);
        return historiales.size();
    }

    // Método para verificar si un reporte tiene historial
    public boolean tieneHistorial(Integer idReporte) {
        return !getHistorialesByReporte(idReporte).isEmpty();
    }
}