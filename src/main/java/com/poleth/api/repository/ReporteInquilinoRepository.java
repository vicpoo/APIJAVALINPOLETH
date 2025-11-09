// ReporteInquilinoRepository.java
package com.poleth.api.repository;

import com.poleth.api.config.DatabaseConfig;
import com.poleth.api.model.ReporteInquilino;
import jakarta.persistence.EntityManager;
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
}