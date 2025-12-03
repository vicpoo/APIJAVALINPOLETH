// CatalogoMuebleRepository.java
package com.poleth.api.repository;

import com.poleth.api.config.DatabaseConfig;
import com.poleth.api.model.CatalogoMueble;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class CatalogoMuebleRepository {

    // Método para guardar o actualizar un mueble del catálogo
    public CatalogoMueble save(CatalogoMueble catalogoMueble) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            if (catalogoMueble.getIdCatalogoMueble() == null) {
                em.persist(catalogoMueble);
            } else {
                catalogoMueble = em.merge(catalogoMueble);
            }
            em.getTransaction().commit();
            return catalogoMueble;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar el mueble del catálogo", e);
        } finally {
            em.close();
        }
    }

    // Método para obtener todos los muebles del catálogo
    public List<CatalogoMueble> findAll() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT cm FROM CatalogoMueble cm", CatalogoMueble.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar mueble por ID
    public Optional<CatalogoMueble> findById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            CatalogoMueble catalogoMueble = em.find(CatalogoMueble.class, id);
            return Optional.ofNullable(catalogoMueble);
        } finally {
            em.close();
        }
    }

    // Método para eliminar mueble
    public void delete(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            CatalogoMueble catalogoMueble = em.find(CatalogoMueble.class, id);
            if (catalogoMueble != null) {
                em.remove(catalogoMueble);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar el mueble del catálogo", e);
        } finally {
            em.close();
        }
    }

    // Método para buscar mueble por nombre exacto
    public Optional<CatalogoMueble> findByNombreMueble(String nombreMueble) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT cm FROM CatalogoMueble cm WHERE cm.nombreMueble = :nombreMueble", CatalogoMueble.class)
                    .setParameter("nombreMueble", nombreMueble)
                    .getResultStream()
                    .findFirst();
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    // Método para buscar muebles que contengan texto en el nombre
    public List<CatalogoMueble> findByNombreContaining(String texto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT cm FROM CatalogoMueble cm WHERE cm.nombreMueble LIKE :texto", CatalogoMueble.class)
                    .setParameter("texto", "%" + texto + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar muebles que contengan texto en la descripción
    public List<CatalogoMueble> findByDescripcionContaining(String texto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT cm FROM CatalogoMueble cm WHERE cm.descripcion LIKE :texto", CatalogoMueble.class)
                    .setParameter("texto", "%" + texto + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de mueble por nombre
    public boolean existsByNombreMueble(String nombreMueble) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(cm) FROM CatalogoMueble cm WHERE cm.nombreMueble = :nombreMueble", Long.class)
                    .setParameter("nombreMueble", nombreMueble)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de mueble por ID
    public boolean existsById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(cm) FROM CatalogoMueble cm WHERE cm.idCatalogoMueble = :id", Long.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para buscar muebles por estado
    public List<CatalogoMueble> findByEstado(String estado) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT cm FROM CatalogoMueble cm WHERE cm.estadoMueble = :estado", CatalogoMueble.class)
                    .setParameter("estado", estado)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método adicional: contar todos los muebles del catálogo
    public Long count() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT COUNT(cm) FROM CatalogoMueble cm", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para obtener muebles con descripción (no nula)
    public List<CatalogoMueble> findWithDescripcion() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT cm FROM CatalogoMueble cm WHERE cm.descripcion IS NOT NULL", CatalogoMueble.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para obtener muebles sin descripción
    public List<CatalogoMueble> findWithoutDescripcion() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT cm FROM CatalogoMueble cm WHERE cm.descripcion IS NULL", CatalogoMueble.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar muebles por nombre ordenados alfabéticamente
    public List<CatalogoMueble> findAllOrderByNombre() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT cm FROM CatalogoMueble cm ORDER BY cm.nombreMueble", CatalogoMueble.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar muebles activos
    public List<CatalogoMueble> findActivos() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT cm FROM CatalogoMueble cm WHERE cm.estadoMueble = 'activo'", CatalogoMueble.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}