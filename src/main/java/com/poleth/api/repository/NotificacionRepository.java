// NotificacionRepository.java
package com.poleth.api.repository;

import com.poleth.api.config.DatabaseConfig;
import com.poleth.api.model.Notificacion;
import jakarta.persistence.EntityManager;
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
            throw new RuntimeException("Error al guardar la notificación", e);
        } finally {
            em.close();
        }
    }

    // Método para obtener todas las notificaciones
    public List<Notificacion> findAll() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT n FROM Notificacion n", Notificacion.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar notificación por ID
    public Optional<Notificacion> findById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Notificacion notificacion = em.find(Notificacion.class, id);
            return Optional.ofNullable(notificacion);
        } finally {
            em.close();
        }
    }

    // Método para eliminar notificación
    public void delete(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            Notificacion notificacion = em.find(Notificacion.class, id);
            if (notificacion != null) {
                em.remove(notificacion);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar la notificación", e);
        } finally {
            em.close();
        }
    }

    // Método para buscar notificaciones por inquilino
    public List<Notificacion> findByInquilino(Integer idInquilino) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT n FROM Notificacion n WHERE n.inquilino.idInquilino = :idInquilino", Notificacion.class)
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
                            "SELECT n FROM Notificacion n WHERE n.idContrato = :idContrato", Notificacion.class)
                    .setParameter("idContrato", idContrato)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar notificaciones por tipo
    public List<Notificacion> findByTipo(String tipoNotificacion) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT n FROM Notificacion n WHERE n.tipoNotificacion = :tipoNotificacion", Notificacion.class)
                    .setParameter("tipoNotificacion", tipoNotificacion)
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
                            "SELECT n FROM Notificacion n WHERE n.fechaUtilizacion = :fechaUtilizacion", Notificacion.class)
                    .setParameter("fechaUtilizacion", fechaUtilizacion)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar notificaciones por rango de fechas de utilización
    public List<Notificacion> findByRangoFechasUtilizacion(Date fechaInicio, Date fechaFin) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT n FROM Notificacion n WHERE n.fechaUtilizacion BETWEEN :fechaInicio AND :fechaFin", Notificacion.class)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar notificaciones con fecha de utilización pasada
    public List<Notificacion> findWithFechaUtilizacionPasada() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Date hoy = new Date(System.currentTimeMillis());
            return em.createQuery(
                            "SELECT n FROM Notificacion n WHERE n.fechaUtilizacion < :hoy", Notificacion.class)
                    .setParameter("hoy", hoy)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar notificaciones con fecha de utilización futura
    public List<Notificacion> findWithFechaUtilizacionFutura() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Date hoy = new Date(System.currentTimeMillis());
            return em.createQuery(
                            "SELECT n FROM Notificacion n WHERE n.fechaUtilizacion > :hoy", Notificacion.class)
                    .setParameter("hoy", hoy)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar notificaciones con fecha de utilización hoy
    public List<Notificacion> findWithFechaUtilizacionHoy() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Date hoy = new Date(System.currentTimeMillis());
            return em.createQuery(
                            "SELECT n FROM Notificacion n WHERE n.fechaUtilizacion = :hoy", Notificacion.class)
                    .setParameter("hoy", hoy)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar notificaciones sin inquilino
    public List<Notificacion> findWithoutInquilino() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT n FROM Notificacion n WHERE n.inquilino IS NULL", Notificacion.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar notificaciones sin contrato
    public List<Notificacion> findWithoutContrato() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT n FROM Notificacion n WHERE n.idContrato IS NULL", Notificacion.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar notificaciones por inquilino y tipo
    public List<Notificacion> findByInquilinoAndTipo(Integer idInquilino, String tipoNotificacion) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT n FROM Notificacion n WHERE n.inquilino.idInquilino = :idInquilino AND n.tipoNotificacion = :tipoNotificacion", Notificacion.class)
                    .setParameter("idInquilino", idInquilino)
                    .setParameter("tipoNotificacion", tipoNotificacion)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar notificaciones por contrato y tipo
    public List<Notificacion> findByContratoAndTipo(Integer idContrato, String tipoNotificacion) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT n FROM Notificacion n WHERE n.idContrato = :idContrato AND n.tipoNotificacion = :tipoNotificacion", Notificacion.class)
                    .setParameter("idContrato", idContrato)
                    .setParameter("tipoNotificacion", tipoNotificacion)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de notificación por ID
    public boolean existsById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(n) FROM Notificacion n WHERE n.idNotificacion = :id", Long.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de notificaciones por inquilino
    public boolean existsByInquilino(Integer idInquilino) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(n) FROM Notificacion n WHERE n.inquilino.idInquilino = :idInquilino", Long.class)
                    .setParameter("idInquilino", idInquilino)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de notificaciones por contrato
    public boolean existsByContrato(Integer idContrato) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(n) FROM Notificacion n WHERE n.idContrato = :idContrato", Long.class)
                    .setParameter("idContrato", idContrato)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de notificaciones por tipo
    public boolean existsByTipo(String tipoNotificacion) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(n) FROM Notificacion n WHERE n.tipoNotificacion = :tipoNotificacion", Long.class)
                    .setParameter("tipoNotificacion", tipoNotificacion)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método adicional: contar todas las notificaciones
    public Long count() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT COUNT(n) FROM Notificacion n", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para contar notificaciones por inquilino
    public Long countByInquilino(Integer idInquilino) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(n) FROM Notificacion n WHERE n.inquilino.idInquilino = :idInquilino", Long.class)
                    .setParameter("idInquilino", idInquilino)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para contar notificaciones por contrato
    public Long countByContrato(Integer idContrato) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(n) FROM Notificacion n WHERE n.idContrato = :idContrato", Long.class)
                    .setParameter("idContrato", idContrato)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para contar notificaciones por tipo
    public Long countByTipo(String tipoNotificacion) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(n) FROM Notificacion n WHERE n.tipoNotificacion = :tipoNotificacion", Long.class)
                    .setParameter("tipoNotificacion", tipoNotificacion)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para buscar notificaciones con paginación
    public List<Notificacion> findPaginados(int inicio, int tamaño) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT n FROM Notificacion n ORDER BY n.fechaUtilizacion DESC, n.idNotificacion DESC", Notificacion.class)
                    .setFirstResult(inicio)
                    .setMaxResults(tamaño)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar notificaciones por inquilino con paginación
    public List<Notificacion> findByInquilinoPaginados(Integer idInquilino, int inicio, int tamaño) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT n FROM Notificacion n WHERE n.inquilino.idInquilino = :idInquilino ORDER BY n.fechaUtilizacion DESC, n.idNotificacion DESC", Notificacion.class)
                    .setParameter("idInquilino", idInquilino)
                    .setFirstResult(inicio)
                    .setMaxResults(tamaño)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para eliminar todas las notificaciones de un inquilino
    public int deleteByInquilino(Integer idInquilino) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            int deleted = em.createQuery(
                            "DELETE FROM Notificacion n WHERE n.inquilino.idInquilino = :idInquilino")
                    .setParameter("idInquilino", idInquilino)
                    .executeUpdate();
            em.getTransaction().commit();
            return deleted;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar notificaciones del inquilino", e);
        } finally {
            em.close();
        }
    }

    // Método para eliminar todas las notificaciones de un contrato
    public int deleteByContrato(Integer idContrato) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            int deleted = em.createQuery(
                            "DELETE FROM Notificacion n WHERE n.idContrato = :idContrato")
                    .setParameter("idContrato", idContrato)
                    .executeUpdate();
            em.getTransaction().commit();
            return deleted;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar notificaciones del contrato", e);
        } finally {
            em.close();
        }
    }

    // Método para actualizar tipo de notificación
    public int updateTipoNotificacion(Integer idNotificacion, String nuevoTipo) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            int updated = em.createQuery(
                            "UPDATE Notificacion n SET n.tipoNotificacion = :tipo WHERE n.idNotificacion = :id")
                    .setParameter("tipo", nuevoTipo)
                    .setParameter("id", idNotificacion)
                    .executeUpdate();
            em.getTransaction().commit();
            return updated;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar tipo de notificación", e);
        } finally {
            em.close();
        }
    }

    // Método para obtener las notificaciones más recientes
    public List<Notificacion> findMostRecent(int limite) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT n FROM Notificacion n ORDER BY n.fechaUtilizacion DESC, n.idNotificacion DESC", Notificacion.class)
                    .setMaxResults(limite)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}