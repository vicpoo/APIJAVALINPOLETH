// PagoRepository.java
package com.poleth.api.repository;

import com.poleth.api.config.DatabaseConfig;
import com.poleth.api.model.Pago;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class PagoRepository {

    // Método para guardar o actualizar un pago
    public Pago save(Pago pago) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            if (pago.getIdPago() == null) {
                em.persist(pago);
            } else {
                pago = em.merge(pago);
            }
            em.getTransaction().commit();
            return pago;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar el pago", e);
        } finally {
            em.close();
        }
    }

    // Método para obtener todos los pagos
    public List<Pago> findAll() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Pago p", Pago.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar pago por ID
    public Optional<Pago> findById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Pago pago = em.find(Pago.class, id);
            return Optional.ofNullable(pago);
        } finally {
            em.close();
        }
    }

    // Método para eliminar pago
    public void delete(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            Pago pago = em.find(Pago.class, id);
            if (pago != null) {
                em.remove(pago);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar el pago", e);
        } finally {
            em.close();
        }
    }

    // Método para buscar pagos por contrato
    public List<Pago> findByContrato(Integer idContrato) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p WHERE p.idContrato = :idContrato", Pago.class)
                    .setParameter("idContrato", idContrato)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar pagos por fecha
    public List<Pago> findByFecha(Date fechaPago) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p WHERE p.fechaPago = :fechaPago", Pago.class)
                    .setParameter("fechaPago", fechaPago)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar pagos por rango de fechas
    public List<Pago> findByRangoFechas(Date fechaInicio, Date fechaFin) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p WHERE p.fechaPago BETWEEN :fechaInicio AND :fechaFin", Pago.class)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar pagos por concepto
    public List<Pago> findByConcepto(String concepto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p WHERE p.concepto LIKE :concepto", Pago.class)
                    .setParameter("concepto", "%" + concepto + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar pagos por monto mayor o igual
    public List<Pago> findByMontoMayorIgual(BigDecimal monto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p WHERE p.montoPagado >= :monto", Pago.class)
                    .setParameter("monto", monto)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar pagos por monto menor o igual
    public List<Pago> findByMontoMenorIgual(BigDecimal monto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p WHERE p.montoPagado <= :monto", Pago.class)
                    .setParameter("monto", monto)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar pagos por rango de montos
    public List<Pago> findByRangoMontos(BigDecimal montoMin, BigDecimal montoMax) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p WHERE p.montoPagado BETWEEN :montoMin AND :montoMax", Pago.class)
                    .setParameter("montoMin", montoMin)
                    .setParameter("montoMax", montoMax)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar pagos con concepto (no nulos)
    public List<Pago> findWithConcepto() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p WHERE p.concepto IS NOT NULL", Pago.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar pagos sin concepto (nulos)
    public List<Pago> findWithoutConcepto() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p WHERE p.concepto IS NULL", Pago.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de pago por ID
    public boolean existsById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(p) FROM Pago p WHERE p.idPago = :id", Long.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de pagos por contrato
    public boolean existsByContrato(Integer idContrato) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(p) FROM Pago p WHERE p.idContrato = :idContrato", Long.class)
                    .setParameter("idContrato", idContrato)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método adicional: contar todos los pagos
    public Long count() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT COUNT(p) FROM Pago p", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para contar pagos por contrato
    public Long countByContrato(Integer idContrato) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(p) FROM Pago p WHERE p.idContrato = :idContrato", Long.class)
                    .setParameter("idContrato", idContrato)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para sumar montos de pagos por contrato
    public BigDecimal sumMontoByContrato(Integer idContrato) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT SUM(p.montoPagado) FROM Pago p WHERE p.idContrato = :idContrato", BigDecimal.class)
                    .setParameter("idContrato", idContrato)
                    .getSingleResult();
        } catch (NoResultException e) {
            return BigDecimal.ZERO;
        } finally {
            em.close();
        }
    }

    // Método para sumar montos de pagos por rango de fechas
    public BigDecimal sumMontoByRangoFechas(Date fechaInicio, Date fechaFin) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT SUM(p.montoPagado) FROM Pago p WHERE p.fechaPago BETWEEN :fechaInicio AND :fechaFin", BigDecimal.class)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getSingleResult();
        } catch (NoResultException e) {
            return BigDecimal.ZERO;
        } finally {
            em.close();
        }
    }

    // Método para obtener el pago más reciente por contrato
    public Optional<Pago> findLatestByContrato(Integer idContrato) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p WHERE p.idContrato = :idContrato ORDER BY p.fechaPago DESC, p.idPago DESC", Pago.class)
                    .setParameter("idContrato", idContrato)
                    .setMaxResults(1)
                    .getResultStream()
                    .findFirst();
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    // Método para obtener el pago más antiguo por contrato
    public Optional<Pago> findOldestByContrato(Integer idContrato) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p WHERE p.idContrato = :idContrato ORDER BY p.fechaPago ASC, p.idPago ASC", Pago.class)
                    .setParameter("idContrato", idContrato)
                    .setMaxResults(1)
                    .getResultStream()
                    .findFirst();
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    // Método para buscar pagos con paginación
    public List<Pago> findPaginados(int inicio, int tamaño) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Pago p ORDER BY p.fechaPago DESC, p.idPago DESC", Pago.class)
                    .setFirstResult(inicio)
                    .setMaxResults(tamaño)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar pagos por contrato con paginación
    public List<Pago> findByContratoPaginados(Integer idContrato, int inicio, int tamaño) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p WHERE p.idContrato = :idContrato ORDER BY p.fechaPago DESC, p.idPago DESC", Pago.class)
                    .setParameter("idContrato", idContrato)
                    .setFirstResult(inicio)
                    .setMaxResults(tamaño)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para eliminar todos los pagos de un contrato
    public int deleteByContrato(Integer idContrato) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            int deleted = em.createQuery(
                            "DELETE FROM Pago p WHERE p.idContrato = :idContrato")
                    .setParameter("idContrato", idContrato)
                    .executeUpdate();
            em.getTransaction().commit();
            return deleted;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar pagos del contrato", e);
        } finally {
            em.close();
        }
    }

    // Método para actualizar monto de un pago
    public int updateMonto(Integer idPago, BigDecimal nuevoMonto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            int updated = em.createQuery(
                            "UPDATE Pago p SET p.montoPagado = :monto WHERE p.idPago = :id")
                    .setParameter("monto", nuevoMonto)
                    .setParameter("id", idPago)
                    .executeUpdate();
            em.getTransaction().commit();
            return updated;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar monto", e);
        } finally {
            em.close();
        }
    }

    // Método para actualizar concepto de un pago
    public int updateConcepto(Integer idPago, String nuevoConcepto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            int updated = em.createQuery(
                            "UPDATE Pago p SET p.concepto = :concepto WHERE p.idPago = :id")
                    .setParameter("concepto", nuevoConcepto)
                    .setParameter("id", idPago)
                    .executeUpdate();
            em.getTransaction().commit();
            return updated;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar concepto", e);
        } finally {
            em.close();
        }
    }
}