// HistorialReporteService.java
package com.poleth.api.service;

import com.poleth.api.model.HistorialReporte;
import com.poleth.api.repository.HistorialReporteRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
}