// PagoRepository.java
package com.poleth.api.repository;

import com.poleth.api.config.DatabaseConfig;
import com.poleth.api.model.Pago;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.math.BigDecimal;
import java.time.LocalDate;
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

    // Método para obtener todos los pagos CON relaciones
    public List<Pago> findAll() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p " +
                                    "LEFT JOIN FETCH p.contrato " +
                                    "LEFT JOIN FETCH p.inquilino " +
                                    "ORDER BY p.fechaPago DESC",
                            Pago.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar pago por ID CON relaciones
    public Optional<Pago> findById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p " +
                                    "LEFT JOIN FETCH p.contrato " +
                                    "LEFT JOIN FETCH p.inquilino " +
                                    "WHERE p.idPago = :id",
                            Pago.class)
                    .setParameter("id", id)
                    .getResultStream()
                    .findFirst();
        } catch (NoResultException e) {
            return Optional.empty();
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

    // Método para buscar pagos por contrato CON relaciones
    public List<Pago> findByContrato(Integer idContrato) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p " +
                                    "LEFT JOIN FETCH p.contrato " +
                                    "LEFT JOIN FETCH p.inquilino " +
                                    "WHERE p.idContrato = :idContrato " +
                                    "ORDER BY p.fechaPago DESC",
                            Pago.class)
                    .setParameter("idContrato", idContrato)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar pagos por inquilino CON relaciones
    public List<Pago> findByInquilino(Integer idInquilino) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p " +
                                    "LEFT JOIN FETCH p.contrato " +
                                    "LEFT JOIN FETCH p.inquilino " +
                                    "WHERE p.idInquilino = :idInquilino " +
                                    "ORDER BY p.fechaPago DESC",
                            Pago.class)
                    .setParameter("idInquilino", idInquilino)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar pagos por fecha CON relaciones
    public List<Pago> findByFecha(LocalDate fechaPago) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p " +
                                    "LEFT JOIN FETCH p.contrato " +
                                    "LEFT JOIN FETCH p.inquilino " +
                                    "WHERE p.fechaPago = :fechaPago " +
                                    "ORDER BY p.idPago DESC",
                            Pago.class)
                    .setParameter("fechaPago", fechaPago)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar pagos por rango de fechas CON relaciones
    public List<Pago> findByRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p " +
                                    "LEFT JOIN FETCH p.contrato " +
                                    "LEFT JOIN FETCH p.inquilino " +
                                    "WHERE p.fechaPago BETWEEN :fechaInicio AND :fechaFin " +
                                    "ORDER BY p.fechaPago",
                            Pago.class)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar pagos por concepto CON relaciones
    public List<Pago> findByConcepto(String concepto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p " +
                                    "LEFT JOIN FETCH p.contrato " +
                                    "LEFT JOIN FETCH p.inquilino " +
                                    "WHERE p.concepto LIKE :concepto " +
                                    "ORDER BY p.fechaPago DESC",
                            Pago.class)
                    .setParameter("concepto", "%" + concepto + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar pagos por monto mayor o igual CON relaciones
    public List<Pago> findByMontoMayorIgual(BigDecimal monto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p " +
                                    "LEFT JOIN FETCH p.contrato " +
                                    "LEFT JOIN FETCH p.inquilino " +
                                    "WHERE p.montoPagado >= :monto " +
                                    "ORDER BY p.montoPagado DESC",
                            Pago.class)
                    .setParameter("monto", monto)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar pagos por monto menor o igual CON relaciones
    public List<Pago> findByMontoMenorIgual(BigDecimal monto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p " +
                                    "LEFT JOIN FETCH p.contrato " +
                                    "LEFT JOIN FETCH p.inquilino " +
                                    "WHERE p.montoPagado <= :monto " +
                                    "ORDER BY p.montoPagado DESC",
                            Pago.class)
                    .setParameter("monto", monto)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar pagos por rango de montos CON relaciones
    public List<Pago> findByRangoMontos(BigDecimal montoMin, BigDecimal montoMax) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p " +
                                    "LEFT JOIN FETCH p.contrato " +
                                    "LEFT JOIN FETCH p.inquilino " +
                                    "WHERE p.montoPagado BETWEEN :montoMin AND :montoMax " +
                                    "ORDER BY p.montoPagado DESC",
                            Pago.class)
                    .setParameter("montoMin", montoMin)
                    .setParameter("montoMax", montoMax)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar pagos por estado CON relaciones
    public List<Pago> findByEstado(String estadoPago) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p " +
                                    "LEFT JOIN FETCH p.contrato " +
                                    "LEFT JOIN FETCH p.inquilino " +
                                    "WHERE p.estadoPago = :estadoPago " +
                                    "ORDER BY p.fechaPago DESC",
                            Pago.class)
                    .setParameter("estadoPago", estadoPago)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar pagos por método de pago CON relaciones
    public List<Pago> findByMetodoPago(String metodoPago) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p " +
                                    "LEFT JOIN FETCH p.contrato " +
                                    "LEFT JOIN FETCH p.inquilino " +
                                    "WHERE p.metodoPago = :metodoPago " +
                                    "ORDER BY p.fechaPago DESC",
                            Pago.class)
                    .setParameter("metodoPago", metodoPago)
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
                            "SELECT COUNT(p) FROM Pago p WHERE p.idPago = :id",
                            Long.class)
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
                            "SELECT COUNT(p) FROM Pago p WHERE p.idContrato = :idContrato",
                            Long.class)
                    .setParameter("idContrato", idContrato)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de pagos por inquilino
    public boolean existsByInquilino(Integer idInquilino) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(p) FROM Pago p WHERE p.idInquilino = :idInquilino",
                            Long.class)
                    .setParameter("idInquilino", idInquilino)
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
                            "SELECT COUNT(p) FROM Pago p WHERE p.idContrato = :idContrato",
                            Long.class)
                    .setParameter("idContrato", idContrato)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para contar pagos por inquilino
    public Long countByInquilino(Integer idInquilino) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(p) FROM Pago p WHERE p.idInquilino = :idInquilino",
                            Long.class)
                    .setParameter("idInquilino", idInquilino)
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
                            "SELECT SUM(p.montoPagado) FROM Pago p WHERE p.idContrato = :idContrato",
                            BigDecimal.class)
                    .setParameter("idContrato", idContrato)
                    .getSingleResult();
        } catch (NoResultException e) {
            return BigDecimal.ZERO;
        } finally {
            em.close();
        }
    }

    // Método para sumar montos de pagos por inquilino
    public BigDecimal sumMontoByInquilino(Integer idInquilino) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT SUM(p.montoPagado) FROM Pago p WHERE p.idInquilino = :idInquilino",
                            BigDecimal.class)
                    .setParameter("idInquilino", idInquilino)
                    .getSingleResult();
        } catch (NoResultException e) {
            return BigDecimal.ZERO;
        } finally {
            em.close();
        }
    }

    // Método para sumar montos de pagos por rango de fechas
    public BigDecimal sumMontoByRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT SUM(p.montoPagado) FROM Pago p WHERE p.fechaPago BETWEEN :fechaInicio AND :fechaFin",
                            BigDecimal.class)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getSingleResult();
        } catch (NoResultException e) {
            return BigDecimal.ZERO;
        } finally {
            em.close();
        }
    }

    // Método para obtener el pago más reciente por contrato CON relaciones
    public Optional<Pago> findLatestByContrato(Integer idContrato) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p " +
                                    "LEFT JOIN FETCH p.contrato " +
                                    "LEFT JOIN FETCH p.inquilino " +
                                    "WHERE p.idContrato = :idContrato " +
                                    "ORDER BY p.fechaPago DESC, p.idPago DESC",
                            Pago.class)
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

    // Método para obtener el pago más antiguo por contrato CON relaciones
    public Optional<Pago> findOldestByContrato(Integer idContrato) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p " +
                                    "LEFT JOIN FETCH p.contrato " +
                                    "LEFT JOIN FETCH p.inquilino " +
                                    "WHERE p.idContrato = :idContrato " +
                                    "ORDER BY p.fechaPago ASC, p.idPago ASC",
                            Pago.class)
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

    // Método para buscar pagos con paginación CON relaciones
    public List<Pago> findPaginados(int inicio, int tamaño) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p " +
                                    "LEFT JOIN FETCH p.contrato " +
                                    "LEFT JOIN FETCH p.inquilino " +
                                    "ORDER BY p.fechaPago DESC",
                            Pago.class)
                    .setFirstResult(inicio)
                    .setMaxResults(tamaño)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar pagos por contrato con paginación CON relaciones
    public List<Pago> findByContratoPaginados(Integer idContrato, int inicio, int tamaño) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pago p " +
                                    "LEFT JOIN FETCH p.contrato " +
                                    "LEFT JOIN FETCH p.inquilino " +
                                    "WHERE p.idContrato = :idContrato " +
                                    "ORDER BY p.fechaPago DESC",
                            Pago.class)
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

    // Método para actualizar estado de un pago
    public int updateEstadoPago(Integer idPago, String nuevoEstado) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            int updated = em.createQuery(
                            "UPDATE Pago p SET p.estadoPago = :estado WHERE p.idPago = :id")
                    .setParameter("estado", nuevoEstado)
                    .setParameter("id", idPago)
                    .executeUpdate();
            em.getTransaction().commit();
            return updated;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar estado", e);
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