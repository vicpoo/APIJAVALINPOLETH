// NotificacionService.java
package com.poleth.api.service;

import com.poleth.api.model.Notificacion;
import com.poleth.api.repository.NotificacionRepository;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class NotificacionService {
    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    // Métodos CRUD básicos
    public List<Notificacion> getAllNotificaciones() {
        return notificacionRepository.findAll();
    }

    public Optional<Notificacion> getNotificacionById(Integer id) {
        return notificacionRepository.findById(id);
    }

    public void deleteNotificacion(Integer id) {
        notificacionRepository.delete(id);
    }

    // Método para crear una nueva notificación con validaciones
    public Notificacion createNotificacion(Notificacion notificacion) {
        // Validaciones básicas
        if (notificacion.getTipoNotificacion() == null || notificacion.getTipoNotificacion().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de notificación es requerido");
        }

        // Validar longitud del tipo de notificación
        if (notificacion.getTipoNotificacion().length() > 50) {
            throw new IllegalArgumentException("El tipo de notificación no puede exceder 50 caracteres");
        }

        // Validar que tenga al menos un destinatario (inquilino o contrato)
        if (!notificacion.tieneInquilino() && !notificacion.tieneContrato()) {
            throw new IllegalArgumentException("La notificación debe estar asociada a un inquilino o contrato");
        }

        // Validar fecha de utilización
        if (notificacion.getFechaUtilizacion() == null) {
            throw new IllegalArgumentException("La fecha de utilización es requerida");
        }

        // Guardar la notificación
        return notificacionRepository.save(notificacion);
    }

    // Método para actualizar una notificación existente
    public Notificacion updateNotificacion(Integer id, Notificacion notificacionActualizada) {
        // Validaciones básicas
        if (notificacionActualizada.getTipoNotificacion() == null || notificacionActualizada.getTipoNotificacion().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de notificación es requerido");
        }

        // Validar longitud del tipo de notificación
        if (notificacionActualizada.getTipoNotificacion().length() > 50) {
            throw new IllegalArgumentException("El tipo de notificación no puede exceder 50 caracteres");
        }

        // Validar que tenga al menos un destinatario
        if (!notificacionActualizada.tieneInquilino() && !notificacionActualizada.tieneContrato()) {
            throw new IllegalArgumentException("La notificación debe estar asociada a un inquilino o contrato");
        }

        // Validar fecha de utilización
        if (notificacionActualizada.getFechaUtilizacion() == null) {
            throw new IllegalArgumentException("La fecha de utilización es requerida");
        }

        // Buscar la notificación existente
        Optional<Notificacion> notificacionExistenteOpt = notificacionRepository.findById(id);
        if (notificacionExistenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Notificación no encontrada con ID: " + id);
        }

        Notificacion notificacionExistente = notificacionExistenteOpt.get();

        // Actualizar campos permitidos
        notificacionExistente.setInquilino(notificacionActualizada.getInquilino());
        notificacionExistente.setIdContrato(notificacionActualizada.getIdContrato());
        notificacionExistente.setFechaUtilizacion(notificacionActualizada.getFechaUtilizacion());
        notificacionExistente.setTipoNotificacion(notificacionActualizada.getTipoNotificacion());

        // Guardar los cambios
        return notificacionRepository.save(notificacionExistente);
    }

    // Método para obtener notificaciones por inquilino
    public List<Notificacion> getNotificacionesByInquilino(Integer idInquilino) {
        return notificacionRepository.findByInquilino(idInquilino);
    }

    // Método para obtener notificaciones por fecha de utilización
    public List<Notificacion> getNotificacionesByFechaUtilizacion(Date fechaUtilizacion) {
        return notificacionRepository.findByFechaUtilizacion(fechaUtilizacion);
    }
}