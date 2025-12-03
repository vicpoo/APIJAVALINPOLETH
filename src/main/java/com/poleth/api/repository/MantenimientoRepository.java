// MantenimientoRepository.java
package com.poleth.api.repository;

import com.poleth.api.config.DatabaseConfig;
import com.poleth.api.model.Mantenimiento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
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

    // Método para obtener todos los mantenimientos
    public List<Mantenimiento> findAll() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT m FROM Mantenimiento m ORDER BY m.fechaReporte DESC",
                            Mantenimiento.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar mantenimiento por ID
    public Optional<Mantenimiento> findById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            TypedQuery<Mantenimiento> query = em.createQuery(
                    "SELECT m FROM Mantenimiento m WHERE m.idMantenimiento = :id",
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

    // Método para buscar mantenimientos por cuarto
    public List<Mantenimiento> findByIdCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT m FROM Mantenimiento m WHERE m.idCuarto = :idCuarto ORDER BY m.fechaReporte DESC",
                            Mantenimiento.class)
                    .setParameter("idCuarto", idCuarto)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar mantenimientos por estado
    public List<Mantenimiento> findByEstadoMantenimiento(String estadoMantenimiento) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT m FROM Mantenimiento m WHERE m.estadoMantenimiento = :estadoMantenimiento ORDER BY m.fechaReporte DESC",
                            Mantenimiento.class)
                    .setParameter("estadoMantenimiento", estadoMantenimiento)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar mantenimientos pendientes
    public List<Mantenimiento> findPendientes() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT m FROM Mantenimiento m WHERE m.fechaAtencion IS NULL ORDER BY m.fechaReporte DESC",
                            Mantenimiento.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar mantenimientos completados
    public List<Mantenimiento> findCompletados() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT m FROM Mantenimiento m WHERE m.fechaAtencion IS NOT NULL ORDER BY m.fechaAtencion DESC",
                            Mantenimiento.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar mantenimientos por rango de fechas de reporte
    public List<Mantenimiento> findByFechaReporteBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT m FROM Mantenimiento m WHERE m.fechaReporte BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fechaReporte DESC",
                            Mantenimiento.class)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar mantenimientos por rango de fechas de atención
    public List<Mantenimiento> findByFechaAtencionBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT m FROM Mantenimiento m WHERE m.fechaAtencion BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fechaAtencion DESC",
                            Mantenimiento.class)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar mantenimientos que contengan texto en la descripción
    public List<Mantenimiento> findByDescripcionContaining(String texto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT m FROM Mantenimiento m WHERE m.descripcionProblema LIKE :texto ORDER BY m.fechaReporte DESC",
                            Mantenimiento.class)
                    .setParameter("texto", "%" + texto + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar mantenimientos pendientes por cuarto
    public List<Mantenimiento> findPendientesByCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT m FROM Mantenimiento m WHERE m.idCuarto = :idCuarto AND m.fechaAtencion IS NULL ORDER BY m.fechaReporte DESC",
                            Mantenimiento.class)
                    .setParameter("idCuarto", idCuarto)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar mantenimientos recientes (últimos 30 días)
    public List<Mantenimiento> findRecientes() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            LocalDate fechaLimite = LocalDate.now().minusDays(30);
            return em.createQuery(
                            "SELECT m FROM Mantenimiento m WHERE m.fechaReporte >= :fechaLimite ORDER BY m.fechaReporte DESC",
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

    // Método para contar todos los mantenimientos
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

    // Método para contar mantenimientos por mes y año
    public Long countByMesYAnio(int mes, int anio) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            LocalDate fechaInicio = LocalDate.of(anio, mes, 1);
            LocalDate fechaFin = fechaInicio.withDayOfMonth(fechaInicio.lengthOfMonth());

            return em.createQuery(
                            "SELECT COUNT(m) FROM Mantenimiento m WHERE m.fechaReporte BETWEEN :fechaInicio AND :fechaFin",
                            Long.class)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para obtener el costo total de mantenimientos
    public BigDecimal sumCostoTotal() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT SUM(m.costoMantenimiento) FROM Mantenimiento m WHERE m.costoMantenimiento IS NOT NULL",
                            BigDecimal.class)
                    .getSingleResult();
        } catch (Exception e) {
            return BigDecimal.ZERO;
        } finally {
            em.close();
        }
    }

    // Método para obtener el costo total por cuarto
    public BigDecimal sumCostoByCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT SUM(m.costoMantenimiento) FROM Mantenimiento m WHERE m.idCuarto = :idCuarto AND m.costoMantenimiento IS NOT NULL",
                            BigDecimal.class)
                    .setParameter("idCuarto", idCuarto)
                    .getSingleResult();
        } catch (Exception e) {
            return BigDecimal.ZERO;
        } finally {
            em.close();
        }
    }

    // Método para obtener mantenimientos con costo mayor a un valor
    public List<Mantenimiento> findByCostoGreaterThan(BigDecimal costoMinimo) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT m FROM Mantenimiento m WHERE m.costoMantenimiento > :costoMinimo ORDER BY m.costoMantenimiento DESC",
                            Mantenimiento.class)
                    .setParameter("costoMinimo", costoMinimo)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar mantenimientos por prioridad (urgentes)
    public List<Mantenimiento> findUrgentes() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT m FROM Mantenimiento m WHERE m.descripcionProblema LIKE '%urgente%' OR m.descripcionProblema LIKE '%emergencia%' ORDER BY m.fechaReporte DESC",
                            Mantenimiento.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}