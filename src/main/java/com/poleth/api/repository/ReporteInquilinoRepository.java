// ReporteInquilinoRepository.java
package com.poleth.api.repository;

import com.poleth.api.config.DatabaseConfig;
import com.poleth.api.model.ReporteInquilino;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ReporteInquilinoRepository {

    // Método para guardar o actualizar un reporte de inquilino
    public ReporteInquilino save(ReporteInquilino reporteInquilino) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            if (reporteInquilino.getIdReporte() == null) {
                em.persist(reporteInquilino);
            } else {
                reporteInquilino = em.merge(reporteInquilino);
            }
            em.getTransaction().commit();
            return reporteInquilino;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar el reporte del inquilino", e);
        } finally {
            em.close();
        }
    }

    // Método para obtener todos los reportes de inquilinos
    public List<ReporteInquilino> findAll() {
    EntityManager em = DatabaseConfig.createEntityManager();
    try {
        return em.createQuery("SELECT ri FROM ReporteInquilino ri", ReporteInquilino.class)
                .getResultList();
    } finally {
        em.close();
    }
}

    // Método para buscar reporte por ID
    public Optional<ReporteInquilino> findById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            ReporteInquilino reporteInquilino = em.find(ReporteInquilino.class, id);
            return Optional.ofNullable(reporteInquilino);
        } finally {
            em.close();
        }
    }

    // Método para eliminar reporte
    public void delete(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            ReporteInquilino reporteInquilino = em.find(ReporteInquilino.class, id);
            if (reporteInquilino != null) {
                em.remove(reporteInquilino);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar el reporte del inquilino", e);
        } finally {
            em.close();
        }
    }

    // Método para buscar reportes por inquilino
    public List<ReporteInquilino> findByIdInquilino(Integer idInquilino) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ri FROM ReporteInquilino ri WHERE ri.idInquilino = :idInquilino", ReporteInquilino.class)
                    .setParameter("idInquilino", idInquilino)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar reportes por cuarto
    public List<ReporteInquilino> findByIdCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ri FROM ReporteInquilino ri WHERE ri.idCuarto = :idCuarto", ReporteInquilino.class)
                    .setParameter("idCuarto", idCuarto)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar reportes por tipo
    public List<ReporteInquilino> findByTipo(String tipo) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ri FROM ReporteInquilino ri WHERE ri.tipo = :tipo", ReporteInquilino.class)
                    .setParameter("tipo", tipo)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar reportes por estado
    public List<ReporteInquilino> findByEstadoReporte(String estadoReporte) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ri FROM ReporteInquilino ri WHERE ri.estadoReporte = :estadoReporte", ReporteInquilino.class)
                    .setParameter("estadoReporte", estadoReporte)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar reportes abiertos (sin fecha de cierre)
    public List<ReporteInquilino> findAbiertos() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ri FROM ReporteInquilino ri WHERE ri.fechaCierre IS NULL", ReporteInquilino.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar reportes cerrados (con fecha de cierre)
    public List<ReporteInquilino> findCerrados() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ri FROM ReporteInquilino ri WHERE ri.fechaCierre IS NOT NULL", ReporteInquilino.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar reportes por rango de fechas
    public List<ReporteInquilino> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ri FROM ReporteInquilino ri WHERE ri.fecha BETWEEN :fechaInicio AND :fechaFin", ReporteInquilino.class)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar reportes por rango de fechas de cierre
    public List<ReporteInquilino> findByFechaCierreBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ri FROM ReporteInquilino ri WHERE ri.fechaCierre BETWEEN :fechaInicio AND :fechaFin", ReporteInquilino.class)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar reportes que contengan texto en la descripción
    public List<ReporteInquilino> findByDescripcionContaining(String texto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ri FROM ReporteInquilino ri WHERE ri.descripcion LIKE :texto", ReporteInquilino.class)
                    .setParameter("texto", "%" + texto + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar reportes que contengan texto en las acciones tomadas
    public List<ReporteInquilino> findByAccionesTomadasContaining(String texto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ri FROM ReporteInquilino ri WHERE ri.accionesTomadas LIKE :texto", ReporteInquilino.class)
                    .setParameter("texto", "%" + texto + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar reportes por nombre
    public List<ReporteInquilino> findByNombreContaining(String texto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ri FROM ReporteInquilino ri WHERE ri.nombre LIKE :texto", ReporteInquilino.class)
                    .setParameter("texto", "%" + texto + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar reportes abiertos por inquilino
    public List<ReporteInquilino> findAbiertosByInquilino(Integer idInquilino) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ri FROM ReporteInquilino ri WHERE ri.idInquilino = :idInquilino AND ri.fechaCierre IS NULL", ReporteInquilino.class)
                    .setParameter("idInquilino", idInquilino)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar reportes abiertos por cuarto
    public List<ReporteInquilino> findAbiertosByCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ri FROM ReporteInquilino ri WHERE ri.idCuarto = :idCuarto AND ri.fechaCierre IS NULL", ReporteInquilino.class)
                    .setParameter("idCuarto", idCuarto)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de reportes por inquilino
    public boolean existsByInquilino(Integer idInquilino) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(ri) FROM ReporteInquilino ri WHERE ri.idInquilino = :idInquilino", Long.class)
                    .setParameter("idInquilino", idInquilino)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de reportes abiertos por inquilino
    public boolean existsAbiertosByInquilino(Integer idInquilino) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(ri) FROM ReporteInquilino ri WHERE ri.idInquilino = :idInquilino AND ri.fechaCierre IS NULL", Long.class)
                    .setParameter("idInquilino", idInquilino)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de reportes por cuarto
    public boolean existsByCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(ri) FROM ReporteInquilino ri WHERE ri.idCuarto = :idCuarto", Long.class)
                    .setParameter("idCuarto", idCuarto)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método adicional: contar todos los reportes
    public Long count() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT COUNT(ri) FROM ReporteInquilino ri", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para contar reportes abiertos
    public Long countAbiertos() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(ri) FROM ReporteInquilino ri WHERE ri.fechaCierre IS NULL", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para contar reportes cerrados
    public Long countCerrados() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(ri) FROM ReporteInquilino ri WHERE ri.fechaCierre IS NOT NULL", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para contar reportes por estado
    public Long countByEstado(String estadoReporte) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(ri) FROM ReporteInquilino ri WHERE ri.estadoReporte = :estadoReporte", Long.class)
                    .setParameter("estadoReporte", estadoReporte)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para contar reportes por tipo
    public Long countByTipo(String tipo) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(ri) FROM ReporteInquilino ri WHERE ri.tipo = :tipo", Long.class)
                    .setParameter("tipo", tipo)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para obtener reportes con relaciones (inquilino y cuarto)
    public List<ReporteInquilino> findAllWithRelations() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ri FROM ReporteInquilino ri LEFT JOIN FETCH ri.inquilino LEFT JOIN FETCH ri.cuarto", ReporteInquilino.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar reporte por ID con relaciones
    public Optional<ReporteInquilino> findByIdWithRelations(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            ReporteInquilino reporteInquilino = em.createQuery(
                            "SELECT ri FROM ReporteInquilino ri LEFT JOIN FETCH ri.inquilino LEFT JOIN FETCH ri.cuarto WHERE ri.idReporte = :id", ReporteInquilino.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return Optional.ofNullable(reporteInquilino);
        } catch (Exception e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    // Método para buscar reportes recientes (últimos 30 días)
    public List<ReporteInquilino> findRecientes() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            LocalDate fechaLimite = LocalDate.now().minusDays(30);
            return em.createQuery(
                            "SELECT ri FROM ReporteInquilino ri WHERE ri.fecha >= :fechaLimite", ReporteInquilino.class)
                    .setParameter("fechaLimite", fechaLimite)
                    .getResultList();
        } finally {
            em.close();
        }
    }   

    // Método para buscar reportes por tipo y estado
    public List<ReporteInquilino> findByTipoAndEstado(String tipo, String estado) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ri FROM ReporteInquilino ri WHERE ri.tipo = :tipo AND ri.estadoReporte = :estado", ReporteInquilino.class)
                    .setParameter("tipo", tipo)
                    .setParameter("estado", estado)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}