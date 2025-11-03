// MantenimientoRepository.java
package com.poleth.api.repository;

import com.poleth.api.config.DatabaseConfig;
import com.poleth.api.model.Mantenimiento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MantenimientoRepository {

    // Método para guardar o actualizar un mantenimiento
    public Mantenimiento save(Mantenimiento mantenimiento) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            if (mantenimiento.getIdMantenimiento() == null) {
                em.persist(mantenimiento);
            } else {
                mantenimiento = em.merge(mantenimiento);
            }
            em.getTransaction().commit();
            return mantenimiento;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar el mantenimiento", e);
        } finally {
            em.close();
        }
    }

    // Método para obtener todos los mantenimientos CON relaciones COMPLETAS - USANDO JOIN FETCH PROFUNDO
    public List<Mantenimiento> findAll() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT m FROM Mantenimiento m " +
                    "LEFT JOIN FETCH m.cuarto cuarto " +
                    "LEFT JOIN FETCH cuarto.propietario", 
                    Mantenimiento.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar mantenimiento por ID CON relaciones COMPLETAS - USANDO JOIN FETCH PROFUNDO
    public Optional<Mantenimiento> findById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            TypedQuery<Mantenimiento> query = em.createQuery(
                    "SELECT m FROM Mantenimiento m " +
                    "LEFT JOIN FETCH m.cuarto cuarto " +
                    "LEFT JOIN FETCH cuarto.propietario " +
                    "WHERE m.idMantenimiento = :id", 
                    Mantenimiento.class);
            query.setParameter("id", id);
            
            try {
                Mantenimiento mantenimiento = query.getSingleResult();
                return Optional.of(mantenimiento);
            } catch (NoResultException e) {
                return Optional.empty();
            }
        } finally {
            em.close();
        }
    }

    // Método para eliminar mantenimiento
    public void delete(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            Mantenimiento mantenimiento = em.find(Mantenimiento.class, id);
            if (mantenimiento != null) {
                em.remove(mantenimiento);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar el mantenimiento", e);
        } finally {
            em.close();
        }
    }

    // Método para buscar mantenimientos por cuarto CON relaciones COMPLETAS - USANDO JOIN FETCH PROFUNDO
    public List<Mantenimiento> findByIdCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT m FROM Mantenimiento m " +
                    "LEFT JOIN FETCH m.cuarto cuarto " +
                    "LEFT JOIN FETCH cuarto.propietario " +
                    "WHERE m.idCuarto = :idCuarto", 
                    Mantenimiento.class)
                    .setParameter("idCuarto", idCuarto)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar mantenimientos por estado CON relaciones COMPLETAS - USANDO JOIN FETCH PROFUNDO
    public List<Mantenimiento> findByEstadoMantenimiento(String estadoMantenimiento) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT m FROM Mantenimiento m " +
                    "LEFT JOIN FETCH m.cuarto cuarto " +
                    "LEFT JOIN FETCH cuarto.propietario " +
                    "WHERE m.estadoMantenimiento = :estadoMantenimiento", 
                    Mantenimiento.class)
                    .setParameter("estadoMantenimiento", estadoMantenimiento)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar mantenimientos pendientes CON relaciones COMPLETAS - USANDO JOIN FETCH PROFUNDO
    public List<Mantenimiento> findPendientes() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT m FROM Mantenimiento m " +
                    "LEFT JOIN FETCH m.cuarto cuarto " +
                    "LEFT JOIN FETCH cuarto.propietario " +
                    "WHERE m.fechaAtencion IS NULL", 
                    Mantenimiento.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar mantenimientos completados CON relaciones COMPLETAS - USANDO JOIN FETCH PROFUNDO
    public List<Mantenimiento> findCompletados() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT m FROM Mantenimiento m " +
                    "LEFT JOIN FETCH m.cuarto cuarto " +
                    "LEFT JOIN FETCH cuarto.propietario " +
                    "WHERE m.fechaAtencion IS NOT NULL", 
                    Mantenimiento.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar mantenimientos por rango de fechas de reporte CON relaciones COMPLETAS - USANDO JOIN FETCH PROFUNDO
    public List<Mantenimiento> findByFechaReporteBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT m FROM Mantenimiento m " +
                    "LEFT JOIN FETCH m.cuarto cuarto " +
                    "LEFT JOIN FETCH cuarto.propietario " +
                    "WHERE m.fechaReporte BETWEEN :fechaInicio AND :fechaFin", 
                    Mantenimiento.class)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar mantenimientos por rango de fechas de atención CON relaciones COMPLETAS - USANDO JOIN FETCH PROFUNDO
    public List<Mantenimiento> findByFechaAtencionBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT m FROM Mantenimiento m " +
                    "LEFT JOIN FETCH m.cuarto cuarto " +
                    "LEFT JOIN FETCH cuarto.propietario " +
                    "WHERE m.fechaAtencion BETWEEN :fechaInicio AND :fechaFin", 
                    Mantenimiento.class)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar mantenimientos que contengan texto en la descripción CON relaciones COMPLETAS - USANDO JOIN FETCH PROFUNDO
    public List<Mantenimiento> findByDescripcionContaining(String texto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT m FROM Mantenimiento m " +
                    "LEFT JOIN FETCH m.cuarto cuarto " +
                    "LEFT JOIN FETCH cuarto.propietario " +
                    "WHERE m.descripcionProblema LIKE :texto", 
                    Mantenimiento.class)
                    .setParameter("texto", "%" + texto + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar mantenimientos pendientes por cuarto CON relaciones COMPLETAS - USANDO JOIN FETCH PROFUNDO
    public List<Mantenimiento> findPendientesByCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT m FROM Mantenimiento m " +
                    "LEFT JOIN FETCH m.cuarto cuarto " +
                    "LEFT JOIN FETCH cuarto.propietario " +
                    "WHERE m.idCuarto = :idCuarto AND m.fechaAtencion IS NULL", 
                    Mantenimiento.class)
                    .setParameter("idCuarto", idCuarto)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar mantenimientos recientes CON relaciones COMPLETAS - USANDO JOIN FETCH PROFUNDO
    public List<Mantenimiento> findRecientes() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            LocalDate fechaLimite = LocalDate.now().minusDays(30);
            return em.createQuery(
                    "SELECT m FROM Mantenimiento m " +
                    "LEFT JOIN FETCH m.cuarto cuarto " +
                    "LEFT JOIN FETCH cuarto.propietario " +
                    "WHERE m.fechaReporte >= :fechaLimite", 
                    Mantenimiento.class)
                    .setParameter("fechaLimite", fechaLimite)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de mantenimientos pendientes por cuarto
    public boolean existsPendientesByCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(m) FROM Mantenimiento m WHERE m.idCuarto = :idCuarto AND m.fechaAtencion IS NULL", 
                    Long.class)
                    .setParameter("idCuarto", idCuarto)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de mantenimientos por cuarto
    public boolean existsByCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(m) FROM Mantenimiento m WHERE m.idCuarto = :idCuarto", 
                    Long.class)
                    .setParameter("idCuarto", idCuarto)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método adicional: contar todos los mantenimientos
    public Long count() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT COUNT(m) FROM Mantenimiento m", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para contar mantenimientos pendientes
    public Long countPendientes() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT COUNT(m) FROM Mantenimiento m WHERE m.fechaAtencion IS NULL", 
                    Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para contar mantenimientos completados
    public Long countCompletados() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT COUNT(m) FROM Mantenimiento m WHERE m.fechaAtencion IS NOT NULL", 
                    Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para contar mantenimientos por estado
    public Long countByEstado(String estadoMantenimiento) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT COUNT(m) FROM Mantenimiento m WHERE m.estadoMantenimiento = :estadoMantenimiento", 
                    Long.class)
                    .setParameter("estadoMantenimiento", estadoMantenimiento)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para obtener mantenimientos con relaciones (ya no es necesario, findAll ya lo hace)
    public List<Mantenimiento> findAllWithRelations() {
        return findAll(); // Ya implementado en findAll()
    }

    // Método para buscar mantenimiento por ID con relaciones (ya no es necesario, findById ya lo hace)
    public Optional<Mantenimiento> findByIdWithRelations(Integer id) {
        return findById(id); // Ya implementado en findById()
    }
}