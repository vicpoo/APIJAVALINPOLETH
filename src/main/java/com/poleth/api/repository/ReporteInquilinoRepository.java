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
            return em.createQuery(
                            "SELECT ri FROM ReporteInquilino ri ORDER BY ri.fecha DESC",
                            ReporteInquilino.class)
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
                            "SELECT ri FROM ReporteInquilino ri WHERE ri.idInquilino = :idInquilino ORDER BY ri.fecha DESC",
                            ReporteInquilino.class)
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
                            "SELECT ri FROM ReporteInquilino ri WHERE ri.idCuarto = :idCuarto ORDER BY ri.fecha DESC",
                            ReporteInquilino.class)
                    .setParameter("idCuarto", idCuarto)
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
                            "SELECT ri FROM ReporteInquilino ri WHERE ri.estadoReporte = :estadoReporte ORDER BY ri.fecha DESC",
                            ReporteInquilino.class)
                    .setParameter("estadoReporte", estadoReporte)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar reportes abiertos
    public List<ReporteInquilino> findReportesAbiertos() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ri FROM ReporteInquilino ri WHERE ri.estadoReporte = 'abierto' ORDER BY ri.fecha DESC",
                            ReporteInquilino.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar reportes cerrados
    public List<ReporteInquilino> findReportesCerrados() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ri FROM ReporteInquilino ri WHERE ri.estadoReporte IN ('cerrado', 'resuelto', 'completado') ORDER BY ri.fechaCierre DESC",
                            ReporteInquilino.class)
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
                            "SELECT ri FROM ReporteInquilino ri WHERE ri.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY ri.fecha DESC",
                            ReporteInquilino.class)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar reportes por texto en descripción o nombre
    public List<ReporteInquilino> findByTexto(String texto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ri FROM ReporteInquilino ri WHERE ri.descripcion LIKE :texto OR ri.nombre LIKE :texto ORDER BY ri.fecha DESC",
                            ReporteInquilino.class)
                    .setParameter("texto", "%" + texto + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para obtener estadísticas de tipos de reportes
    public List<Object[]> getEstadisticasTiposReportes() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ri.tipo, COUNT(ri) FROM ReporteInquilino ri " +
                                    "WHERE ri.tipo IS NOT NULL " +
                                    "GROUP BY ri.tipo " +
                                    "ORDER BY COUNT(ri) DESC", Object[].class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para contar total de reportes
    public int countTotal() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(ri) FROM ReporteInquilino ri", Long.class)
                    .getSingleResult();
            return count != null ? count.intValue() : 0;
        } finally {
            em.close();
        }
    }

    // Método para contar reportes por estado
    public int countByEstado(String estado) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(ri) FROM ReporteInquilino ri WHERE ri.estadoReporte = :estado",
                            Long.class)
                    .setParameter("estado", estado)
                    .getSingleResult();
            return count != null ? count.intValue() : 0;
        } finally {
            em.close();
        }
    }

    // Método para contar reportes por mes
    public int countByMes(int year, int month) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            LocalDate fechaInicio = LocalDate.of(year, month, 1);
            LocalDate fechaFin = fechaInicio.withDayOfMonth(fechaInicio.lengthOfMonth());

            Long count = em.createQuery(
                            "SELECT COUNT(ri) FROM ReporteInquilino ri WHERE ri.fecha BETWEEN :fechaInicio AND :fechaFin",
                            Long.class)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getSingleResult();
            return count != null ? count.intValue() : 0;
        } finally {
            em.close();
        }
    }

    // Método para obtener reportes recientes
    public List<ReporteInquilino> findRecientes(int limite) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ri FROM ReporteInquilino ri ORDER BY ri.fecha DESC",
                            ReporteInquilino.class)
                    .setMaxResults(limite)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para obtener reportes urgentes
    public List<ReporteInquilino> findUrgentes() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ri FROM ReporteInquilino ri WHERE " +
                                    "(ri.descripcion LIKE '%urgente%' OR ri.descripcion LIKE '%emergencia%') " +
                                    "AND ri.estadoReporte = 'abierto' " +
                                    "ORDER BY ri.fecha DESC",
                            ReporteInquilino.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para obtener reportes sin acciones tomadas
    public List<ReporteInquilino> findSinAccionesTomadas() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ri FROM ReporteInquilino ri WHERE " +
                                    "ri.accionesTomadas IS NULL OR ri.accionesTomadas = '' " +
                                    "ORDER BY ri.fecha DESC",
                            ReporteInquilino.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar reportes por tipo específico
    public List<ReporteInquilino> findByTipo(String tipo) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ri FROM ReporteInquilino ri WHERE ri.tipo = :tipo ORDER BY ri.fecha DESC",
                            ReporteInquilino.class)
                    .setParameter("tipo", tipo)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}