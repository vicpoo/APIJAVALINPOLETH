// NotificacionRepository.java
package com.poleth.api.repository;

import com.poleth.api.config.DatabaseConfig;
import com.poleth.api.model.Notificacion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class NotificacionRepository {

    // Método para guardar o actualizar una notificación
    public Notificacion save(Notificacion notificacion) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            if (notificacion.getIdNotificacion() == null) {
                em.persist(notificacion);
            } else {
                notificacion = em.merge(notificacion);
            }
            em.getTransaction().commit();
            return notificacion;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar la notificación: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    // Método para obtener todas las notificaciones
    public List<Notificacion> findAll() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT n FROM Notificacion n JOIN FETCH n.inquilino ORDER BY n.createdAt DESC", Notificacion.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar notificación por ID
    public Optional<Notificacion> findById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Notificacion notificacion = em.createQuery(
                            "SELECT n FROM Notificacion n JOIN FETCH n.inquilino WHERE n.idNotificacion = :id",
                            Notificacion.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return Optional.of(notificacion);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar notificación por ID: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    // Método para eliminar notificación
    public boolean delete(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            Notificacion notificacion = em.find(Notificacion.class, id);
            if (notificacion != null) {
                em.remove(notificacion);
                em.getTransaction().commit();
                return true;
            } else {
                em.getTransaction().rollback();
                return false;
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar la notificación: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    // Método para buscar notificaciones por inquilino
    public List<Notificacion> findByInquilino(Integer idInquilino) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT n FROM Notificacion n JOIN FETCH n.inquilino " +
                                    "WHERE n.inquilino.idUsuario = :idInquilino " +
                                    "ORDER BY n.createdAt DESC",
                            Notificacion.class)
                    .setParameter("idInquilino", idInquilino)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar notificaciones por contrato
    public List<Notificacion> findByContrato(Integer idContrato) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT n FROM Notificacion n JOIN FETCH n.inquilino " +
                                    "WHERE n.idContrato = :idContrato " +
                                    "ORDER BY n.createdAt DESC",
                            Notificacion.class)
                    .setParameter("idContrato", idContrato)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar notificaciones por fecha de utilización
    public List<Notificacion> findByFechaUtilizacion(Date fechaUtilizacion) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT n FROM Notificacion n JOIN FETCH n.inquilino " +
                                    "WHERE n.fechaUtilizacion = :fechaUtilizacion " +
                                    "ORDER BY n.createdAt DESC",
                            Notificacion.class)
                    .setParameter("fechaUtilizacion", fechaUtilizacion)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar notificaciones por inquilino y estado
    public List<Notificacion> findByInquilinoAndEstado(Integer idInquilino, String estado) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT n FROM Notificacion n JOIN FETCH n.inquilino " +
                                    "WHERE n.inquilino.idUsuario = :idInquilino " +
                                    "AND n.estadoNotificacion = :estado " +
                                    "ORDER BY n.createdAt DESC",
                            Notificacion.class)
                    .setParameter("idInquilino", idInquilino)
                    .setParameter("estado", estado)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para contar notificaciones por inquilino y estado
    public Long countByInquilinoAndEstado(Integer idInquilino, String estado) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(n) FROM Notificacion n " +
                                    "WHERE n.inquilino.idUsuario = :idInquilino " +
                                    "AND n.estadoNotificacion = :estado",
                            Long.class)
                    .setParameter("idInquilino", idInquilino)
                    .setParameter("estado", estado)
                    .getSingleResult();
        } catch (NoResultException e) {
            return 0L;
        } finally {
            em.close();
        }
    }

    // Método para buscar notificaciones por tipo
    public List<Notificacion> findByTipo(String tipoNotificacion) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT n FROM Notificacion n JOIN FETCH n.inquilino " +
                                    "WHERE n.tipoNotificacion = :tipo " +
                                    "ORDER BY n.createdAt DESC",
                            Notificacion.class)
                    .setParameter("tipo", tipoNotificacion)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar notificaciones por estado
    public List<Notificacion> findByEstado(String estado) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT n FROM Notificacion n JOIN FETCH n.inquilino " +
                                    "WHERE n.estadoNotificacion = :estado " +
                                    "ORDER BY n.createdAt DESC",
                            Notificacion.class)
                    .setParameter("estado", estado)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para actualizar estado de notificación
    public boolean updateEstadoNotificacion(Integer idNotificacion, String nuevoEstado) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            int updated = em.createQuery(
                            "UPDATE Notificacion n SET n.estadoNotificacion = :estado " +
                                    "WHERE n.idNotificacion = :id")
                    .setParameter("estado", nuevoEstado)
                    .setParameter("id", idNotificacion)
                    .executeUpdate();
            em.getTransaction().commit();
            return updated > 0;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar estado de notificación: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia
    public boolean existsById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(n) FROM Notificacion n WHERE n.idNotificacion = :id",
                            Long.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return count > 0;
        } catch (NoResultException e) {
            return false;
        } finally {
            em.close();
        }
    }

    // Método para buscar notificaciones recientes
    public List<Notificacion> findRecent(int limite) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT n FROM Notificacion n JOIN FETCH n.inquilino " +
                                    "ORDER BY n.createdAt DESC",
                            Notificacion.class)
                    .setMaxResults(limite)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar notificaciones recientes por inquilino
    public List<Notificacion> findRecentByInquilino(Integer idInquilino, int limite) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT n FROM Notificacion n JOIN FETCH n.inquilino " +
                                    "WHERE n.inquilino.idUsuario = :idInquilino " +
                                    "ORDER BY n.createdAt DESC",
                            Notificacion.class)
                    .setParameter("idInquilino", idInquilino)
                    .setMaxResults(limite)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para contar todas las notificaciones
    public Long count() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT COUNT(n) FROM Notificacion n", Long.class)
                    .getSingleResult();
        } catch (NoResultException e) {
            return 0L;
        } finally {
            em.close();
        }
    }
}