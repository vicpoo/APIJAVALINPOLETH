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
    public Notificacion saveNotificacion(Notificacion notificacion) {
        return notificacionRepository.save(notificacion);
    }

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

    // Método para obtener notificaciones por contrato
    public List<Notificacion> getNotificacionesByContrato(Integer idContrato) {
        return notificacionRepository.findByContrato(idContrato);
    }

    // Método para obtener notificaciones por tipo
    public List<Notificacion> getNotificacionesByTipo(String tipoNotificacion) {
        return notificacionRepository.findByTipo(tipoNotificacion);
    }

    // Método para obtener notificaciones por fecha de utilización
    public List<Notificacion> getNotificacionesByFechaUtilizacion(Date fechaUtilizacion) {
        return notificacionRepository.findByFechaUtilizacion(fechaUtilizacion);
    }

    // Método para obtener notificaciones por rango de fechas de utilización
    public List<Notificacion> getNotificacionesByRangoFechasUtilizacion(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son requeridas");
        }
        if (fechaInicio.after(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        return notificacionRepository.findByRangoFechasUtilizacion(fechaInicio, fechaFin);
    }

    // Método para obtener notificaciones con fecha de utilización pasada
    public List<Notificacion> getNotificacionesWithFechaUtilizacionPasada() {
        return notificacionRepository.findWithFechaUtilizacionPasada();
    }

    // Método para obtener notificaciones con fecha de utilización futura
    public List<Notificacion> getNotificacionesWithFechaUtilizacionFutura() {
        return notificacionRepository.findWithFechaUtilizacionFutura();
    }

    // Método para obtener notificaciones con fecha de utilización hoy
    public List<Notificacion> getNotificacionesWithFechaUtilizacionHoy() {
        return notificacionRepository.findWithFechaUtilizacionHoy();
    }

    // Método para obtener notificaciones sin inquilino
    public List<Notificacion> getNotificacionesWithoutInquilino() {
        return notificacionRepository.findWithoutInquilino();
    }

    // Método para obtener notificaciones sin contrato
    public List<Notificacion> getNotificacionesWithoutContrato() {
        return notificacionRepository.findWithoutContrato();
    }

    // Método para obtener notificaciones por inquilino y tipo
    public List<Notificacion> getNotificacionesByInquilinoAndTipo(Integer idInquilino, String tipoNotificacion) {
        return notificacionRepository.findByInquilinoAndTipo(idInquilino, tipoNotificacion);
    }

    // Método para obtener notificaciones por contrato y tipo
    public List<Notificacion> getNotificacionesByContratoAndTipo(Integer idContrato, String tipoNotificacion) {
        return notificacionRepository.findByContratoAndTipo(idContrato, tipoNotificacion);
    }

    // Método para verificar si una notificación existe por ID
    public boolean existsById(Integer id) {
        return notificacionRepository.findById(id).isPresent();
    }

    // Método para verificar si existen notificaciones por inquilino
    public boolean existsByInquilino(Integer idInquilino) {
        return notificacionRepository.existsByInquilino(idInquilino);
    }

    // Método para verificar si existen notificaciones por contrato
    public boolean existsByContrato(Integer idContrato) {
        return notificacionRepository.existsByContrato(idContrato);
    }

    // Método para verificar si existen notificaciones por tipo
    public boolean existsByTipo(String tipoNotificacion) {
        return notificacionRepository.existsByTipo(tipoNotificacion);
    }

    // Método para contar todas las notificaciones
    public Long countNotificaciones() {
        return notificacionRepository.count();
    }

    // Método para contar notificaciones por inquilino
    public Long countByInquilino(Integer idInquilino) {
        return notificacionRepository.countByInquilino(idInquilino);
    }

    // Método para contar notificaciones por contrato
    public Long countByContrato(Integer idContrato) {
        return notificacionRepository.countByContrato(idContrato);
    }

    // Método para contar notificaciones por tipo
    public Long countByTipo(String tipoNotificacion) {
        return notificacionRepository.countByTipo(tipoNotificacion);
    }

    // Método para obtener notificaciones con paginación
    public List<Notificacion> getNotificacionesPaginados(int pagina, int tamaño) {
        if (pagina < 1) pagina = 1;
        if (tamaño < 1 || tamaño > 100) tamaño = 20;
        
        int inicio = (pagina - 1) * tamaño;
        return notificacionRepository.findPaginados(inicio, tamaño);
    }

    // Método para obtener notificaciones por inquilino con paginación
    public List<Notificacion> getNotificacionesByInquilinoPaginados(Integer idInquilino, int pagina, int tamaño) {
        if (pagina < 1) pagina = 1;
        if (tamaño < 1 || tamaño > 100) tamaño = 20;
        
        int inicio = (pagina - 1) * tamaño;
        return notificacionRepository.findByInquilinoPaginados(idInquilino, inicio, tamaño);
    }

    // Método para eliminar todas las notificaciones de un inquilino
    public int deleteNotificacionesByInquilino(Integer idInquilino) {
        return notificacionRepository.deleteByInquilino(idInquilino);
    }

    // Método para eliminar todas las notificaciones de un contrato
    public int deleteNotificacionesByContrato(Integer idContrato) {
        return notificacionRepository.deleteByContrato(idContrato);
    }

    // Método para actualizar tipo de notificación
    public boolean updateTipoNotificacion(Integer idNotificacion, String nuevoTipo) {
        if (nuevoTipo == null || nuevoTipo.trim().isEmpty()) {
            throw new IllegalArgumentException("El nuevo tipo de notificación es requerido");
        }

        if (nuevoTipo.length() > 50) {
            throw new IllegalArgumentException("El tipo de notificación no puede exceder 50 caracteres");
        }

        // Verificar que la notificación exista
        Optional<Notificacion> notificacion = notificacionRepository.findById(idNotificacion);
        if (notificacion.isEmpty()) {
            throw new IllegalArgumentException("Notificación no encontrada con ID: " + idNotificacion);
        }

        int updated = notificacionRepository.updateTipoNotificacion(idNotificacion, nuevoTipo);
        return updated > 0;
    }

    // Método para obtener las notificaciones más recientes
    public List<Notificacion> getNotificacionesMasRecientes(int limite) {
        if (limite < 1 || limite > 100) limite = 10;
        return notificacionRepository.findMostRecent(limite);
    }

    // Método para crear notificación rápida para inquilino
    public Notificacion crearNotificacionParaInquilino(Integer idInquilino, String tipoNotificacion, Date fechaUtilizacion) {
        Notificacion notificacion = new Notificacion();
        // Aquí necesitarías obtener el objeto Inquilino completo
        // Por ahora usamos un enfoque temporal
        notificacion.setTipoNotificacion(tipoNotificacion);
        notificacion.setFechaUtilizacion(fechaUtilizacion);
        
        return createNotificacion(notificacion);
    }

    // Método para crear notificación rápida para contrato
    public Notificacion crearNotificacionParaContrato(Integer idContrato, String tipoNotificacion, Date fechaUtilizacion) {
        Notificacion notificacion = new Notificacion();
        notificacion.setIdContrato(idContrato);
        notificacion.setTipoNotificacion(tipoNotificacion);
        notificacion.setFechaUtilizacion(fechaUtilizacion);
        
        return createNotificacion(notificacion);
    }

    // Método para obtener estadísticas de notificaciones
    public NotificacionStats getStats() {
        Long totalNotificaciones = notificacionRepository.count();
        Long notificacionesConInquilino = totalNotificaciones - (long) notificacionRepository.findWithoutInquilino().size();
        Long notificacionesConContrato = totalNotificaciones - (long) notificacionRepository.findWithoutContrato().size();
        Long notificacionesPasadas = (long) notificacionRepository.findWithFechaUtilizacionPasada().size();
        Long notificacionesFuturas = (long) notificacionRepository.findWithFechaUtilizacionFutura().size();
        
        return new NotificacionStats(totalNotificaciones, notificacionesConInquilino, notificacionesConContrato, 
                                   notificacionesPasadas, notificacionesFuturas);
    }

    // Método para obtener estadísticas por inquilino
    public NotificacionStats getStatsByInquilino(Integer idInquilino) {
        Long totalPorInquilino = notificacionRepository.countByInquilino(idInquilino);
        List<Notificacion> notificaciones = notificacionRepository.findByInquilino(idInquilino);
        Long notificacionesPasadas = notificaciones.stream()
                .filter(Notificacion::esFechaUtilizacionPasada)
                .count();
        Long notificacionesFuturas = notificaciones.stream()
                .filter(Notificacion::esFechaUtilizacionFutura)
                .count();
        
        return new NotificacionStats(totalPorInquilino, totalPorInquilino, 0L, notificacionesPasadas, notificacionesFuturas);
    }

    // Clase interna para estadísticas
    public static class NotificacionStats {
        private final Long totalNotificaciones;
        private final Long notificacionesConInquilino;
        private final Long notificacionesConContrato;
        private final Long notificacionesPasadas;
        private final Long notificacionesFuturas;

        public NotificacionStats(Long totalNotificaciones, Long notificacionesConInquilino, 
                               Long notificacionesConContrato, Long notificacionesPasadas, 
                               Long notificacionesFuturas) {
            this.totalNotificaciones = totalNotificaciones;
            this.notificacionesConInquilino = notificacionesConInquilino;
            this.notificacionesConContrato = notificacionesConContrato;
            this.notificacionesPasadas = notificacionesPasadas;
            this.notificacionesFuturas = notificacionesFuturas;
        }

        public Long getTotalNotificaciones() {
            return totalNotificaciones;
        }

        public Long getNotificacionesConInquilino() {
            return notificacionesConInquilino;
        }

        public Long getNotificacionesConContrato() {
            return notificacionesConContrato;
        }

        public Long getNotificacionesPasadas() {
            return notificacionesPasadas;
        }

        public Long getNotificacionesFuturas() {
            return notificacionesFuturas;
        }

        public Long getNotificacionesHoy() {
            return totalNotificaciones - notificacionesPasadas - notificacionesFuturas;
        }
    }
}