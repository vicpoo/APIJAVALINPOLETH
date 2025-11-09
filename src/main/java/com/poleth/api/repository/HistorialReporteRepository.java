// HistorialReporteRepository.java
package com.poleth.api.repository;

import com.poleth.api.config.DatabaseConfig;
import com.poleth.api.model.HistorialReporte;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class HistorialReporteRepository {

    // Método para guardar o actualizar un historial de reporte
    public HistorialReporte save(HistorialReporte historialReporte) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            if (historialReporte.getIdHistorial() == null) {
                em.persist(historialReporte);
            } else {
                historialReporte = em.merge(historialReporte);
            }
            em.getTransaction().commit();
            return historialReporte;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar el historial del reporte", e);
        } finally {
            em.close();
        }
    }

    // Método para obtener todos los historiales de reportes
    public List<HistorialReporte> findAll() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT hr FROM HistorialReporte hr", HistorialReporte.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar historial por ID
    public Optional<HistorialReporte> findById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            HistorialReporte historialReporte = em.find(HistorialReporte.class, id);
            return Optional.ofNullable(historialReporte);
        } finally {
            em.close();
        }
    }

    // Método para eliminar historial
    public void delete(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            HistorialReporte historialReporte = em.find(HistorialReporte.class, id);
            if (historialReporte != null) {
                em.remove(historialReporte);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar el historial del reporte", e);
        } finally {
            em.close();
        }
    }

    // Método para buscar historiales por reporte
    public List<HistorialReporte> findByIdReporte(Integer idReporte) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT hr FROM HistorialReporte hr WHERE hr.idReporte = :idReporte", HistorialReporte.class)
                    .setParameter("idReporte", idReporte)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar historiales por tipo de reporte
    public List<HistorialReporte> findByTipoReporteHist(String tipoReporteHist) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT hr FROM HistorialReporte hr WHERE hr.tipoReporteHist = :tipoReporteHist", HistorialReporte.class)
                    .setParameter("tipoReporteHist", tipoReporteHist)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar historiales por usuario de registro
    public List<HistorialReporte> findByUsuarioRegistro(String usuarioRegistro) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT hr FROM HistorialReporte hr WHERE hr.usuarioRegistro = :usuarioRegistro", HistorialReporte.class)
                    .setParameter("usuarioRegistro", usuarioRegistro)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar historiales que contengan texto en la descripción
    public List<HistorialReporte> findByDescripcionContaining(String texto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT hr FROM HistorialReporte hr WHERE hr.descripcionHist LIKE :texto", HistorialReporte.class)
                    .setParameter("texto", "%" + texto + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar historiales que contengan texto en el nombre
    public List<HistorialReporte> findByNombreContaining(String texto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT hr FROM HistorialReporte hr WHERE hr.nombreReporteHist LIKE :texto", HistorialReporte.class)
                    .setParameter("texto", "%" + texto + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para obtener el historial más reciente de un reporte
    public Optional<HistorialReporte> findUltimoByReporte(Integer idReporte) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT hr FROM HistorialReporte hr WHERE hr.idReporte = :idReporte ORDER BY hr.fechaRegistro DESC", HistorialReporte.class)
                    .setParameter("idReporte", idReporte)
                    .setMaxResults(1)
                    .getResultStream()
                    .findFirst();
        } finally {
            em.close();
        }
    }

    // Método para obtener historiales ordenados por fecha (más recientes primero)
    public List<HistorialReporte> findAllOrderByFechaDesc() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT hr FROM HistorialReporte hr ORDER BY hr.fechaRegistro DESC", HistorialReporte.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para obtener historiales de un reporte ordenados por fecha
    public List<HistorialReporte> findByIdReporteOrderByFechaDesc(Integer idReporte) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT hr FROM HistorialReporte hr WHERE hr.idReporte = :idReporte ORDER BY hr.fechaRegistro DESC", HistorialReporte.class)
                    .setParameter("idReporte", idReporte)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}