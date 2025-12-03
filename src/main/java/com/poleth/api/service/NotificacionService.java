// NotificacionService.java
package com.poleth.api.service;

import com.poleth.api.model.Notificacion;
import com.poleth.api.model.Usuario;
import com.poleth.api.repository.NotificacionRepository;
import com.poleth.api.repository.UsuarioRepository;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class NotificacionService {
    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;

    public NotificacionService(NotificacionRepository notificacionRepository,
                               UsuarioRepository usuarioRepository) {
        this.notificacionRepository = notificacionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Método para crear una nueva notificación
    public Notificacion createNotificacion(Notificacion notificacion) {
        // Validaciones
        if (notificacion.getInquilino() == null || notificacion.getInquilino().getIdUsuario() == null) {
            throw new IllegalArgumentException("El inquilino es requerido");
        }

        if (notificacion.getIdContrato() == null) {
            throw new IllegalArgumentException("El ID del contrato es requerido");
        }

        // Verificar que el inquilino exista
        Optional<Usuario> inquilinoOpt = usuarioRepository.findById(notificacion.getInquilino().getIdUsuario());
        if (inquilinoOpt.isEmpty()) {
            throw new IllegalArgumentException("El inquilino especificado no existe");
        }

        // Asignar el inquilino completo
        notificacion.setInquilino(inquilinoOpt.get());

        // Validar tipo de notificación
        if (notificacion.getTipoNotificacion() != null && notificacion.getTipoNotificacion().length() > 50) {
            throw new IllegalArgumentException("El tipo de notificación no puede exceder 50 caracteres");
        }

        // Validar detalles
        if (notificacion.getDetalles() != null && notificacion.getDetalles().length() > 1000) {
            throw new IllegalArgumentException("Los detalles no pueden exceder 1000 caracteres");
        }

        // Validar fecha de utilización
        if (notificacion.getFechaUtilizacion() != null) {
            LocalDate fecha = notificacion.getFechaUtilizacion().toLocalDate();
            LocalDate hoy = LocalDate.now();

            if (fecha.isBefore(hoy.minusYears(1))) {
                throw new IllegalArgumentException("La fecha de utilización no puede ser mayor a un año en el pasado");
            }
            if (fecha.isAfter(hoy.plusYears(1))) {
                throw new IllegalArgumentException("La fecha de utilización no puede ser mayor a un año en el futuro");
            }
        }

        return notificacionRepository.save(notificacion);
    }

    // Método para actualizar una notificación
    public Notificacion updateNotificacion(Integer id, Notificacion notificacionActualizada) {
        // Verificar que la notificación exista
        Optional<Notificacion> notificacionExistenteOpt = notificacionRepository.findById(id);
        if (notificacionExistenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Notificación no encontrada con ID: " + id);
        }

        Notificacion notificacionExistente = notificacionExistenteOpt.get();

        // Actualizar campos permitidos
        if (notificacionActualizada.getInquilino() != null &&
                notificacionActualizada.getInquilino().getIdUsuario() != null) {

            Optional<Usuario> inquilinoOpt = usuarioRepository.findById(
                    notificacionActualizada.getInquilino().getIdUsuario());

            if (inquilinoOpt.isEmpty()) {
                throw new IllegalArgumentException("El inquilino especificado no existe");
            }
            notificacionExistente.setInquilino(inquilinoOpt.get());
        }

        if (notificacionActualizada.getIdContrato() != null) {
            notificacionExistente.setIdContrato(notificacionActualizada.getIdContrato());
        }

        if (notificacionActualizada.getFechaUtilizacion() != null) {
            notificacionExistente.setFechaUtilizacion(notificacionActualizada.getFechaUtilizacion());
        }

        if (notificacionActualizada.getTipoNotificacion() != null) {
            if (notificacionActualizada.getTipoNotificacion().length() > 50) {
                throw new IllegalArgumentException("El tipo de notificación no puede exceder 50 caracteres");
            }
            notificacionExistente.setTipoNotificacion(notificacionActualizada.getTipoNotificacion());
        }

        if (notificacionActualizada.getDetalles() != null) {
            if (notificacionActualizada.getDetalles().length() > 1000) {
                throw new IllegalArgumentException("Los detalles no pueden exceder 1000 caracteres");
            }
            notificacionExistente.setDetalles(notificacionActualizada.getDetalles());
        }

        if (notificacionActualizada.getEstadoNotificacion() != null) {
            if (notificacionActualizada.getEstadoNotificacion().length() > 20) {
                throw new IllegalArgumentException("El estado de notificación no puede exceder 20 caracteres");
            }
            notificacionExistente.setEstadoNotificacion(notificacionActualizada.getEstadoNotificacion());
        }

        return notificacionRepository.save(notificacionExistente);
    }

    // Método para marcar notificación como leída
    public Notificacion marcarComoLeida(Integer id) {
        Optional<Notificacion> notificacionOpt = notificacionRepository.findById(id);
        if (notificacionOpt.isEmpty()) {
            throw new IllegalArgumentException("Notificación no encontrada con ID: " + id);
        }

        Notificacion notificacion = notificacionOpt.get();
        notificacion.marcarComoLeido();

        return notificacionRepository.save(notificacion);
    }

    // Método para obtener todas las notificaciones
    public List<Notificacion> getAllNotificaciones() {
        return notificacionRepository.findAll();
    }

    // Método para obtener notificación por ID
    public Optional<Notificacion> getNotificacionById(Integer id) {
        return notificacionRepository.findById(id);
    }

    // Método para eliminar notificación
    public boolean deleteNotificacion(Integer id) {
        return notificacionRepository.delete(id);
    }

    // Método para obtener notificaciones por inquilino
    public List<Notificacion> getNotificacionesByInquilino(Integer idInquilino) {
        return notificacionRepository.findByInquilino(idInquilino);
    }

    // Método para obtener notificaciones por contrato
    public List<Notificacion> getNotificacionesByContrato(Integer idContrato) {
        return notificacionRepository.findByContrato(idContrato);
    }

    // Método para obtener notificaciones por fecha de utilización
    public List<Notificacion> getNotificacionesByFechaUtilizacion(Date fechaUtilizacion) {
        return notificacionRepository.findByFechaUtilizacion(fechaUtilizacion);
    }

    // Método para obtener notificaciones no leídas por inquilino
    public List<Notificacion> getNotificacionesNoLeidasByInquilino(Integer idInquilino) {
        return notificacionRepository.findByInquilinoAndEstado(idInquilino, "no_leido");
    }

    // Método para obtener notificaciones leídas por inquilino
    public List<Notificacion> getNotificacionesLeidasByInquilino(Integer idInquilino) {
        return notificacionRepository.findByInquilinoAndEstado(idInquilino, "leido");
    }

    // Método para contar notificaciones no leídas por inquilino
    public Long contarNotificacionesNoLeidasByInquilino(Integer idInquilino) {
        return notificacionRepository.countByInquilinoAndEstado(idInquilino, "no_leido");
    }

    // Método para obtener notificaciones por tipo
    public List<Notificacion> getNotificacionesByTipo(String tipo) {
        return notificacionRepository.findByTipo(tipo);
    }

    // Método para obtener notificaciones por estado
    public List<Notificacion> getNotificacionesByEstado(String estado) {
        return notificacionRepository.findByEstado(estado);
    }

    // Método para obtener notificaciones recientes
    public List<Notificacion> getNotificacionesRecientes(int limite) {
        return notificacionRepository.findRecent(limite);
    }

    // Método para obtener notificaciones recientes por inquilino
    public List<Notificacion> getNotificacionesRecientesByInquilino(Integer idInquilino, int limite) {
        return notificacionRepository.findRecentByInquilino(idInquilino, limite);
    }

    // Método para verificar si existe notificación
    public boolean existeNotificacion(Integer id) {
        return notificacionRepository.existsById(id);
    }

    // Método para contar todas las notificaciones
    public Long contarNotificaciones() {
        return notificacionRepository.count();
    }
}