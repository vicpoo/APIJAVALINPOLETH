// HistorialReporteRepository.java
package com.poleth.api.repository;

import com.poleth.api.config.DatabaseConfig;
import com.poleth.api.model.HistorialReporte;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
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
            return em.createQuery(
                            "SELECT hr FROM HistorialReporte hr ORDER BY hr.fechaRegistro DESC",
                            HistorialReporte.class)
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
                            "SELECT hr FROM HistorialReporte hr WHERE hr.idReporte = :idReporte ORDER BY hr.fechaRegistro DESC",
                            HistorialReporte.class)
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
                            "SELECT hr FROM HistorialReporte hr WHERE hr.tipoReporteHist = :tipoReporteHist ORDER BY hr.fechaRegistro DESC",
                            HistorialReporte.class)
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
                            "SELECT hr FROM HistorialReporte hr WHERE hr.usuarioRegistro = :usuarioRegistro ORDER BY hr.fechaRegistro DESC",
                            HistorialReporte.class)
                    .setParameter("usuarioRegistro", usuarioRegistro)
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
                            "SELECT hr FROM HistorialReporte hr WHERE hr.descripcionHist LIKE :texto ORDER BY hr.fechaRegistro DESC",
                            HistorialReporte.class)
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
                            "SELECT hr FROM HistorialReporte hr WHERE hr.nombreReporteHist LIKE :texto ORDER BY hr.fechaRegistro DESC",
                            HistorialReporte.class)
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
            TypedQuery<HistorialReporte> query = em.createQuery(
                    "SELECT hr FROM HistorialReporte hr WHERE hr.idReporte = :idReporte ORDER BY hr.fechaRegistro DESC",
                    HistorialReporte.class);
            query.setParameter("idReporte", idReporte);
            query.setMaxResults(1);

            try {
                HistorialReporte historial = query.getSingleResult();
                return Optional.of(historial);
            } catch (NoResultException e) {
                return Optional.empty();
            }
        } finally {
            em.close();
        }
    }

    // Método para obtener historiales ordenados por fecha (más recientes primero)
    public List<HistorialReporte> findAllOrderByFechaDesc() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT hr FROM HistorialReporte hr ORDER BY hr.fechaRegistro DESC",
                            HistorialReporte.class)
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
                            "SELECT hr FROM HistorialReporte hr WHERE hr.idReporte = :idReporte ORDER BY hr.fechaRegistro DESC",
                            HistorialReporte.class)
                    .setParameter("idReporte", idReporte)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para contar total de historiales
    public int countTotal() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(hr) FROM HistorialReporte hr", Long.class)
                    .getSingleResult();
            return count != null ? count.intValue() : 0;
        } finally {
            em.close();
        }
    }

    // Método para contar historiales por reporte
    public int countByReporte(Integer idReporte) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(hr) FROM HistorialReporte hr WHERE hr.idReporte = :idReporte",
                            Long.class)
                    .setParameter("idReporte", idReporte)
                    .getSingleResult();
            return count != null ? count.intValue() : 0;
        } finally {
            em.close();
        }
    }

    // Método para contar historiales por usuario
    public int countByUsuario(String usuarioRegistro) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(hr) FROM HistorialReporte hr WHERE hr.usuarioRegistro = :usuarioRegistro",
                            Long.class)
                    .setParameter("usuarioRegistro", usuarioRegistro)
                    .getSingleResult();
            return count != null ? count.intValue() : 0;
        } finally {
            em.close();
        }
    }

    // Método para contar historiales por tipo
    public int countByTipo(String tipoReporteHist) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(hr) FROM HistorialReporte hr WHERE hr.tipoReporteHist = :tipoReporteHist",
                            Long.class)
                    .setParameter("tipoReporteHist", tipoReporteHist)
                    .getSingleResult();
            return count != null ? count.intValue() : 0;
        } finally {
            em.close();
        }
    }

    // Método para buscar historiales en un rango de fechas
    public List<HistorialReporte> findByFechaBetween(java.time.LocalDateTime fechaInicio,
                                                     java.time.LocalDateTime fechaFin) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT hr FROM HistorialReporte hr WHERE hr.fechaRegistro BETWEEN :fechaInicio AND :fechaFin ORDER BY hr.fechaRegistro DESC",
                            HistorialReporte.class)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para obtener los N historiales más recientes
    public List<HistorialReporte> findRecientes(int limite) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT hr FROM HistorialReporte hr ORDER BY hr.fechaRegistro DESC",
                            HistorialReporte.class)
                    .setMaxResults(limite)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}