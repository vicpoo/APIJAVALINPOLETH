// ImagenCuartoPublicaRepository.java
package com.poleth.api.repository;

import com.poleth.api.config.DatabaseConfig;
import com.poleth.api.model.ImagenCuartoPublica;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class ImagenCuartoPublicaRepository {

    // Método para guardar o actualizar una imagen
    public ImagenCuartoPublica save(ImagenCuartoPublica imagen) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            if (imagen.getIdImagen() == null) {
                em.persist(imagen);
            } else {
                imagen = em.merge(imagen);
            }
            em.getTransaction().commit();
            return imagen;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar la imagen", e);
        } finally {
            em.close();
        }
    }

    // Método para obtener todas las imágenes
    public List<ImagenCuartoPublica> findAll() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT i FROM ImagenCuartoPublica i", ImagenCuartoPublica.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar imagen por ID
    public Optional<ImagenCuartoPublica> findById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            ImagenCuartoPublica imagen = em.find(ImagenCuartoPublica.class, id);
            return Optional.ofNullable(imagen);
        } finally {
            em.close();
        }
    }

    // Método para eliminar imagen
    public void delete(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            ImagenCuartoPublica imagen = em.find(ImagenCuartoPublica.class, id);
            if (imagen != null) {
                em.remove(imagen);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar la imagen", e);
        } finally {
            em.close();
        }
    }

    // Método para buscar imágenes por cuarto
    public List<ImagenCuartoPublica> findByCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT i FROM ImagenCuartoPublica i WHERE i.idCuarto = :idCuarto", ImagenCuartoPublica.class)
                    .setParameter("idCuarto", idCuarto)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar imágenes públicas por cuarto
    public List<ImagenCuartoPublica> findPublicasByCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT i FROM ImagenCuartoPublica i WHERE i.idCuarto = :idCuarto AND i.esPublica = true", ImagenCuartoPublica.class)
                    .setParameter("idCuarto", idCuarto)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar imágenes privadas por cuarto
    public List<ImagenCuartoPublica> findPrivadasByCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT i FROM ImagenCuartoPublica i WHERE i.idCuarto = :idCuarto AND i.esPublica = false", ImagenCuartoPublica.class)
                    .setParameter("idCuarto", idCuarto)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar todas las imágenes públicas
    public List<ImagenCuartoPublica> findAllPublicas() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT i FROM ImagenCuartoPublica i WHERE i.esPublica = true", ImagenCuartoPublica.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar todas las imágenes privadas
    public List<ImagenCuartoPublica> findAllPrivadas() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT i FROM ImagenCuartoPublica i WHERE i.esPublica = false", ImagenCuartoPublica.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar imagen por URL
    public Optional<ImagenCuartoPublica> findByUrl(String urlImagen) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT i FROM ImagenCuartoPublica i WHERE i.urlImagen = :urlImagen", ImagenCuartoPublica.class)
                    .setParameter("urlImagen", urlImagen)
                    .getResultStream()
                    .findFirst();
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de imagen por URL
    public boolean existsByUrl(String urlImagen) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(i) FROM ImagenCuartoPublica i WHERE i.urlImagen = :urlImagen", Long.class)
                    .setParameter("urlImagen", urlImagen)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de imágenes para un cuarto
    public boolean existsByCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(i) FROM ImagenCuartoPublica i WHERE i.idCuarto = :idCuarto", Long.class)
                    .setParameter("idCuarto", idCuarto)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para contar imágenes por cuarto
    public Long countByCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(i) FROM ImagenCuartoPublica i WHERE i.idCuarto = :idCuarto", Long.class)
                    .setParameter("idCuarto", idCuarto)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para contar imágenes públicas por cuarto
    public Long countPublicasByCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(i) FROM ImagenCuartoPublica i WHERE i.idCuarto = :idCuarto AND i.esPublica = true", Long.class)
                    .setParameter("idCuarto", idCuarto)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para contar imágenes privadas por cuarto
    public Long countPrivadasByCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(i) FROM ImagenCuartoPublica i WHERE i.idCuarto = :idCuarto AND i.esPublica = false", Long.class)
                    .setParameter("idCuarto", idCuarto)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método adicional: contar todas las imágenes
    public Long count() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT COUNT(i) FROM ImagenCuartoPublica i", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para buscar imágenes con paginación
    public List<ImagenCuartoPublica> findPaginados(int inicio, int tamaño) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT i FROM ImagenCuartoPublica i", ImagenCuartoPublica.class)
                    .setFirstResult(inicio)
                    .setMaxResults(tamaño)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para eliminar todas las imágenes de un cuarto
    public int deleteByCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            int deleted = em.createQuery(
                            "DELETE FROM ImagenCuartoPublica i WHERE i.idCuarto = :idCuarto")
                    .setParameter("idCuarto", idCuarto)
                    .executeUpdate();
            em.getTransaction().commit();
            return deleted;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar imágenes del cuarto", e);
        } finally {
            em.close();
        }
    }

    // Método para actualizar visibilidad de imágenes de un cuarto
    public int updateVisibilidadByCuarto(Integer idCuarto, Boolean esPublica) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            int updated = em.createQuery(
                            "UPDATE ImagenCuartoPublica i SET i.esPublica = :esPublica WHERE i.idCuarto = :idCuarto")
                    .setParameter("esPublica", esPublica)
                    .setParameter("idCuarto", idCuarto)
                    .executeUpdate();
            em.getTransaction().commit();
            return updated;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar visibilidad de imágenes", e);
        } finally {
            em.close();
        }
    }
}