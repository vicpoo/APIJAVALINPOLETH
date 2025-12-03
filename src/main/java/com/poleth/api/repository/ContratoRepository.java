// ContratoRepository.java
package com.poleth.api.repository;

import com.poleth.api.config.DatabaseConfig;
import com.poleth.api.model.Contrato;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ContratoRepository {

    // Método para guardar o actualizar un contrato
    public Contrato save(Contrato contrato) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            if (contrato.getIdContrato() == null) {
                em.persist(contrato);
            } else {
                contrato = em.merge(contrato);
            }
            em.getTransaction().commit();
            return contrato;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar el contrato", e);
        } finally {
            em.close();
        }
    }

    // Método para obtener todos los contratos CON relaciones COMPLETAS
    public List<Contrato> findAll() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Contrato c " +
                                    "LEFT JOIN FETCH c.cuarto " +
                                    "LEFT JOIN FETCH c.inquilino " +
                                    "ORDER BY c.createdAt DESC",
                            Contrato.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar contrato por ID CON relaciones
    public Optional<Contrato> findById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            TypedQuery<Contrato> query = em.createQuery(
                    "SELECT c FROM Contrato c " +
                            "LEFT JOIN FETCH c.cuarto " +
                            "LEFT JOIN FETCH c.inquilino " +
                            "WHERE c.idContrato = :id",
                    Contrato.class);
            query.setParameter("id", id);

            try {
                Contrato contrato = query.getSingleResult();
                return Optional.of(contrato);
            } catch (NoResultException e) {
                return Optional.empty();
            }
        } finally {
            em.close();
        }
    }

    // Método para eliminar contrato
    public void delete(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            Contrato contrato = em.find(Contrato.class, id);
            if (contrato != null) {
                em.remove(contrato);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar el contrato", e);
        } finally {
            em.close();
        }
    }

    // Método para buscar contratos por cuarto CON relaciones
    public List<Contrato> findByIdCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Contrato c " +
                                    "LEFT JOIN FETCH c.cuarto " +
                                    "LEFT JOIN FETCH c.inquilino " +
                                    "WHERE c.idCuarto = :idCuarto " +
                                    "ORDER BY c.createdAt DESC",
                            Contrato.class)
                    .setParameter("idCuarto", idCuarto)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar contratos por inquilino CON relaciones
    public List<Contrato> findByIdInquilino(Integer idInquilino) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Contrato c " +
                                    "LEFT JOIN FETCH c.cuarto " +
                                    "LEFT JOIN FETCH c.inquilino " +
                                    "WHERE c.idInquilino = :idInquilino " +
                                    "ORDER BY c.createdAt DESC",
                            Contrato.class)
                    .setParameter("idInquilino", idInquilino)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar contratos por estado CON relaciones
    public List<Contrato> findByEstadoContrato(String estadoContrato) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Contrato c " +
                                    "LEFT JOIN FETCH c.cuarto " +
                                    "LEFT JOIN FETCH c.inquilino " +
                                    "WHERE c.estadoContrato = :estadoContrato " +
                                    "ORDER BY c.createdAt DESC",
                            Contrato.class)
                    .setParameter("estadoContrato", estadoContrato)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar contratos activos CON relaciones
    public List<Contrato> findContratosActivos() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Contrato c " +
                                    "LEFT JOIN FETCH c.cuarto " +
                                    "LEFT JOIN FETCH c.inquilino " +
                                    "WHERE c.estadoContrato = 'activo' " +
                                    "ORDER BY c.createdAt DESC",
                            Contrato.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar contratos por rango de fechas de inicio CON relaciones
    public List<Contrato> findByFechaInicioBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Contrato c " +
                                    "LEFT JOIN FETCH c.cuarto " +
                                    "LEFT JOIN FETCH c.inquilino " +
                                    "WHERE c.fechaInicio BETWEEN :fechaInicio AND :fechaFin " +
                                    "ORDER BY c.fechaInicio",
                            Contrato.class)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar contratos que expiran en una fecha específica CON relaciones
    public List<Contrato> findByFechaFinalizacion(LocalDate fechaFinalizacion) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Contrato c " +
                                    "LEFT JOIN FETCH c.cuarto " +
                                    "LEFT JOIN FETCH c.inquilino " +
                                    "WHERE c.fechaFinalizacion = :fechaFinalizacion " +
                                    "ORDER BY c.createdAt DESC",
                            Contrato.class)
                    .setParameter("fechaFinalizacion", fechaFinalizacion)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar contratos próximos a expirar CON relaciones
    public List<Contrato> findContratosProximosAExpirar(int dias) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            LocalDate hoy = LocalDate.now();
            LocalDate fechaLimite = hoy.plusDays(dias);
            return em.createQuery(
                            "SELECT c FROM Contrato c " +
                                    "LEFT JOIN FETCH c.cuarto " +
                                    "LEFT JOIN FETCH c.inquilino " +
                                    "WHERE c.fechaFinalizacion BETWEEN :hoy AND :fechaLimite " +
                                    "AND c.estadoContrato = 'activo' " +
                                    "ORDER BY c.fechaFinalizacion",
                            Contrato.class)
                    .setParameter("hoy", hoy)
                    .setParameter("fechaLimite", fechaLimite)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de contrato activo para un cuarto
    public boolean existsContratoActivoByCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(c) FROM Contrato c " +
                                    "WHERE c.idCuarto = :idCuarto " +
                                    "AND c.estadoContrato = 'activo' " +
                                    "AND (c.fechaFinalizacion IS NULL OR c.fechaFinalizacion >= CURRENT_DATE)",
                            Long.class)
                    .setParameter("idCuarto", idCuarto)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de contrato activo para un inquilino
    public boolean existsContratoActivoByInquilino(Integer idInquilino) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(c) FROM Contrato c " +
                                    "WHERE c.idInquilino = :idInquilino " +
                                    "AND c.estadoContrato = 'activo' " +
                                    "AND (c.fechaFinalizacion IS NULL OR c.fechaFinalizacion >= CURRENT_DATE)",
                            Long.class)
                    .setParameter("idInquilino", idInquilino)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de contrato por cuarto e inquilino
    public boolean existsByCuartoAndInquilino(Integer idCuarto, Integer idInquilino) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(c) FROM Contrato c " +
                                    "WHERE c.idCuarto = :idCuarto AND c.idInquilino = :idInquilino",
                            Long.class)
                    .setParameter("idCuarto", idCuarto)
                    .setParameter("idInquilino", idInquilino)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método adicional: contar todos los contratos
    public Long count() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT COUNT(c) FROM Contrato c", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para contar contratos activos
    public Long countContratosActivos() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(c) FROM Contrato c " +
                                    "WHERE c.estadoContrato = 'activo' " +
                                    "AND (c.fechaFinalizacion IS NULL OR c.fechaFinalizacion >= CURRENT_DATE)",
                            Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para contar contratos por estado
    public Long countByEstado(String estadoContrato) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(c) FROM Contrato c " +
                                    "WHERE c.estadoContrato = :estadoContrato",
                            Long.class)
                    .setParameter("estadoContrato", estadoContrato)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de contrato por ID
    public boolean existsById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(c) FROM Contrato c WHERE c.idContrato = :id",
                            Long.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para buscar contratos con paginación
    public List<Contrato> findPaginados(int inicio, int tamaño) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Contrato c " +
                                    "LEFT JOIN FETCH c.cuarto " +
                                    "LEFT JOIN FETCH c.inquilino " +
                                    "ORDER BY c.createdAt DESC",
                            Contrato.class)
                    .setFirstResult(inicio)
                    .setMaxResults(tamaño)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar contratos vencidos
    public List<Contrato> findContratosVencidos() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Contrato c " +
                                    "LEFT JOIN FETCH c.cuarto " +
                                    "LEFT JOIN FETCH c.inquilino " +
                                    "WHERE c.fechaFinalizacion < CURRENT_DATE " +
                                    "AND c.estadoContrato = 'activo' " +
                                    "ORDER BY c.fechaFinalizacion",
                            Contrato.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar contratos por fecha de pago establecida
    public List<Contrato> findByFechaPagoEstablecida(LocalDate fechaPago) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Contrato c " +
                                    "LEFT JOIN FETCH c.cuarto " +
                                    "LEFT JOIN FETCH c.inquilino " +
                                    "WHERE c.fechaPagoEstablecida = :fechaPago " +
                                    "ORDER BY c.createdAt DESC",
                            Contrato.class)
                    .setParameter("fechaPago", fechaPago)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar contratos con pagos próximos
    public List<Contrato> findContratosConPagosProximos(int dias) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            LocalDate hoy = LocalDate.now();
            LocalDate fechaLimite = hoy.plusDays(dias);
            return em.createQuery(
                            "SELECT c FROM Contrato c " +
                                    "LEFT JOIN FETCH c.cuarto " +
                                    "LEFT JOIN FETCH c.inquilino " +
                                    "WHERE c.fechaPagoEstablecida BETWEEN :hoy AND :fechaLimite " +
                                    "AND c.estadoContrato = 'activo' " +
                                    "ORDER BY c.fechaPagoEstablecida",
                            Contrato.class)
                    .setParameter("hoy", hoy)
                    .setParameter("fechaLimite", fechaLimite)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}