// HistorialReporteRepository.java
package com.poleth.api.repository;

import com.poleth.api.config.DatabaseConfig;
import com.poleth.api.model.HistorialReporte;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class HistorialReporteRepository {

    // Método para guardar o actualizar un historial de reporte
    public HistorialReporte save(HistorialReporte historialReporte) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            if (historialReporte.getIdHistorial() == null) {
                em.persist(historialReporte);
            } else {
                historialReporte = em.merge(historialReporte);
            }
            em.getTransaction().commit();
            return historialReporte;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar el historial del reporte", e);
        } finally {
            em.close();
        }
    }

    // Método para obtener todos los historiales de reportes
    public List<HistorialReporte> findAll() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT hr FROM HistorialReporte hr", HistorialReporte.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar historial por ID
    public Optional<HistorialReporte> findById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            HistorialReporte historialReporte = em.find(HistorialReporte.class, id);
            return Optional.ofNullable(historialReporte);
        } finally {
            em.close();
        }
    }

    // Método para eliminar historial
    public void delete(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            HistorialReporte historialReporte = em.find(HistorialReporte.class, id);
            if (historialReporte != null) {
                em.remove(historialReporte);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar el historial del reporte", e);
        } finally {
            em.close();
        }
    }

    // Método para buscar historiales por reporte
    public List<HistorialReporte> findByIdReporte(Integer idReporte) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT hr FROM HistorialReporte hr WHERE hr.idReporte = :idReporte", HistorialReporte.class)
                    .setParameter("idReporte", idReporte)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar historiales por tipo de reporte
    public List<HistorialReporte> findByTipoReporteHist(String tipoReporteHist) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT hr FROM HistorialReporte hr WHERE hr.tipoReporteHist = :tipoReporteHist", HistorialReporte.class)
                    .setParameter("tipoReporteHist", tipoReporteHist)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar historiales por usuario de registro
    public List<HistorialReporte> findByUsuarioRegistro(String usuarioRegistro) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT hr FROM HistorialReporte hr WHERE hr.usuarioRegistro = :usuarioRegistro", HistorialReporte.class)
                    .setParameter("usuarioRegistro", usuarioRegistro)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar historiales por rango de fechas
    public List<HistorialReporte> findByFechaRegistroBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT hr FROM HistorialReporte hr WHERE hr.fechaRegistro BETWEEN :fechaInicio AND :fechaFin", HistorialReporte.class)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar historiales recientes (últimos X días)
    public List<HistorialReporte> findRecientes(int dias) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            LocalDateTime fechaLimite = LocalDateTime.now().minusDays(dias);
            return em.createQuery(
                            "SELECT hr FROM HistorialReporte hr WHERE hr.fechaRegistro >= :fechaLimite", HistorialReporte.class)
                    .setParameter("fechaLimite", fechaLimite)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar historiales que contengan texto en la descripción
    public List<HistorialReporte> findByDescripcionContaining(String texto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT hr FROM HistorialReporte hr WHERE hr.descripcionHist LIKE :texto", HistorialReporte.class)
                    .setParameter("texto", "%" + texto + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar historiales que contengan texto en el nombre
    public List<HistorialReporte> findByNombreContaining(String texto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT hr FROM HistorialReporte hr WHERE hr.nombreReporteHist LIKE :texto", HistorialReporte.class)
                    .setParameter("texto", "%" + texto + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para obtener el historial más reciente de un reporte
    public Optional<HistorialReporte> findUltimoByReporte(Integer idReporte) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT hr FROM HistorialReporte hr WHERE hr.idReporte = :idReporte ORDER BY hr.fechaRegistro DESC", HistorialReporte.class)
                    .setParameter("idReporte", idReporte)
                    .setMaxResults(1)
                    .getResultStream()
                    .findFirst();
        } finally {
            em.close();
        }
    }

    // Método para obtener historiales ordenados por fecha (más recientes primero)
    public List<HistorialReporte> findAllOrderByFechaDesc() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT hr FROM HistorialReporte hr ORDER BY hr.fechaRegistro DESC", HistorialReporte.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para obtener historiales de un reporte ordenados por fecha
    public List<HistorialReporte> findByIdReporteOrderByFechaDesc(Integer idReporte) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT hr FROM HistorialReporte hr WHERE hr.idReporte = :idReporte ORDER BY hr.fechaRegistro DESC", HistorialReporte.class)
                    .setParameter("idReporte", idReporte)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de historiales por reporte
    public boolean existsByReporte(Integer idReporte) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(hr) FROM HistorialReporte hr WHERE hr.idReporte = :idReporte", Long.class)
                    .setParameter("idReporte", idReporte)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de historiales por usuario
    public boolean existsByUsuario(String usuarioRegistro) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(hr) FROM HistorialReporte hr WHERE hr.usuarioRegistro = :usuarioRegistro", Long.class)
                    .setParameter("usuarioRegistro", usuarioRegistro)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método adicional: contar todos los historiales
    public Long count() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT COUNT(hr) FROM HistorialReporte hr", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para contar historiales por reporte
    public Long countByReporte(Integer idReporte) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(hr) FROM HistorialReporte hr WHERE hr.idReporte = :idReporte", Long.class)
                    .setParameter("idReporte", idReporte)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para contar historiales por usuario
    public Long countByUsuario(String usuarioRegistro) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(hr) FROM HistorialReporte hr WHERE hr.usuarioRegistro = :usuarioRegistro", Long.class)
                    .setParameter("usuarioRegistro", usuarioRegistro)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para contar historiales por tipo
    public Long countByTipo(String tipoReporteHist) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(hr) FROM HistorialReporte hr WHERE hr.tipoReporteHist = :tipoReporteHist", Long.class)
                    .setParameter("tipoReporteHist", tipoReporteHist)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para obtener historiales con relaciones (reporte)
    public List<HistorialReporte> findAllWithRelations() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT hr FROM HistorialReporte hr LEFT JOIN FETCH hr.reporte", HistorialReporte.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar historial por ID con relaciones
    public Optional<HistorialReporte> findByIdWithRelations(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            HistorialReporte historialReporte = em.createQuery(
                            "SELECT hr FROM HistorialReporte hr LEFT JOIN FETCH hr.reporte WHERE hr.idHistorial = :id", HistorialReporte.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return Optional.ofNullable(historialReporte);
        } catch (Exception e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    // Método para obtener historiales de un reporte con relaciones
    public List<HistorialReporte> findByIdReporteWithRelations(Integer idReporte) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT hr FROM HistorialReporte hr LEFT JOIN FETCH hr.reporte WHERE hr.idReporte = :idReporte ORDER BY hr.fechaRegistro DESC", HistorialReporte.class)
                    .setParameter("idReporte", idReporte)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar historiales por usuario con relaciones
    public List<HistorialReporte> findByUsuarioRegistroWithRelations(String usuarioRegistro) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT hr FROM HistorialReporte hr LEFT JOIN FETCH hr.reporte WHERE hr.usuarioRegistro = :usuarioRegistro ORDER BY hr.fechaRegistro DESC", HistorialReporte.class)
                    .setParameter("usuarioRegistro", usuarioRegistro)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para obtener estadísticas de actividad por usuario
    public List<Object[]> getEstadisticasPorUsuario() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT hr.usuarioRegistro, COUNT(hr) FROM HistorialReporte hr GROUP BY hr.usuarioRegistro ORDER BY COUNT(hr) DESC", Object[].class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para obtener estadísticas de actividad por fecha
    public List<Object[]> getEstadisticasPorFecha() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT DATE(hr.fechaRegistro), COUNT(hr) FROM HistorialReporte hr GROUP BY DATE(hr.fechaRegistro) ORDER BY DATE(hr.fechaRegistro) DESC", Object[].class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}