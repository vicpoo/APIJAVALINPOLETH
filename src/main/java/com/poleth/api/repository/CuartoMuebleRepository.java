// CuartoMuebleRepository.java
package com.poleth.api.repository;

import com.poleth.api.config.DatabaseConfig;
import com.poleth.api.model.CuartoMueble;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class CuartoMuebleRepository {

    // Método para guardar o actualizar un cuarto mueble
    public CuartoMueble save(CuartoMueble cuartoMueble) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            if (cuartoMueble.getIdCuartoMueble() == null) {
                em.persist(cuartoMueble);
            } else {
                cuartoMueble = em.merge(cuartoMueble);
            }
            em.getTransaction().commit();
            return cuartoMueble;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar el cuarto mueble", e);
        } finally {
            em.close();
        }
    }

    // Método para obtener todos los cuarto muebles
    public List<CuartoMueble> findAll() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT cm FROM CuartoMueble cm", CuartoMueble.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar cuarto mueble por ID
    public Optional<CuartoMueble> findById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            CuartoMueble cuartoMueble = em.find(CuartoMueble.class, id);
            return Optional.ofNullable(cuartoMueble);
        } finally {
            em.close();
        }
    }

    // Método para eliminar cuarto mueble
    public void delete(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            CuartoMueble cuartoMueble = em.find(CuartoMueble.class, id);
            if (cuartoMueble != null) {
                em.remove(cuartoMueble);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar el cuarto mueble", e);
        } finally {
            em.close();
        }
    }

    // Método para buscar cuarto muebles por cuarto
    public List<CuartoMueble> findByCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT cm FROM CuartoMueble cm WHERE cm.idCuarto = :idCuarto",
                            CuartoMueble.class)
                    .setParameter("idCuarto", idCuarto)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar cuarto muebles por catálogo de mueble
    public List<CuartoMueble> findByCatalogoMueble(Integer idCatalogoMueble) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT cm FROM CuartoMueble cm WHERE cm.idCatalogoMueble = :idCatalogoMueble",
                            CuartoMueble.class)
                    .setParameter("idCatalogoMueble", idCatalogoMueble)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar cuarto mueble específico por cuarto y catálogo
    public Optional<CuartoMueble> findByCuartoAndCatalogo(Integer idCuarto, Integer idCatalogoMueble) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT cm FROM CuartoMueble cm WHERE cm.idCuarto = :idCuarto AND cm.idCatalogoMueble = :idCatalogoMueble",
                            CuartoMueble.class)
                    .setParameter("idCuarto", idCuarto)
                    .setParameter("idCatalogoMueble", idCatalogoMueble)
                    .getResultStream()
                    .findFirst();
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    // Método para buscar cuarto muebles por estado
    public List<CuartoMueble> findByEstado(String estado) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT cm FROM CuartoMueble cm WHERE cm.estado = :estado",
                            CuartoMueble.class)
                    .setParameter("estado", estado)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar cuarto muebles con stock (cantidad > 0)
    public List<CuartoMueble> findWithStock() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT cm FROM CuartoMueble cm WHERE cm.cantidad > 0",
                            CuartoMueble.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar cuarto muebles sin stock (cantidad = 0)
    public List<CuartoMueble> findWithoutStock() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT cm FROM CuartoMueble cm WHERE cm.cantidad = 0",
                            CuartoMueble.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar cuarto muebles con stock por cuarto
    public List<CuartoMueble> findWithStockByCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT cm FROM CuartoMueble cm WHERE cm.idCuarto = :idCuarto AND cm.cantidad > 0",
                            CuartoMueble.class)
                    .setParameter("idCuarto", idCuarto)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar cuarto muebles sin stock por cuarto
    public List<CuartoMueble> findWithoutStockByCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT cm FROM CuartoMueble cm WHERE cm.idCuarto = :idCuarto AND cm.cantidad = 0",
                            CuartoMueble.class)
                    .setParameter("idCuarto", idCuarto)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de cuarto mueble por cuarto y catálogo
    public boolean existsByCuartoAndCatalogo(Integer idCuarto, Integer idCatalogoMueble) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(cm) FROM CuartoMueble cm WHERE cm.idCuarto = :idCuarto AND cm.idCatalogoMueble = :idCatalogoMueble",
                            Long.class)
                    .setParameter("idCuarto", idCuarto)
                    .setParameter("idCatalogoMueble", idCatalogoMueble)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de cuarto muebles por cuarto
    public boolean existsByCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(cm) FROM CuartoMueble cm WHERE cm.idCuarto = :idCuarto",
                            Long.class)
                    .setParameter("idCuarto", idCuarto)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de cuarto muebles por catálogo
    public boolean existsByCatalogoMueble(Integer idCatalogoMueble) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(cm) FROM CuartoMueble cm WHERE cm.idCatalogoMueble = :idCatalogoMueble",
                            Long.class)
                    .setParameter("idCatalogoMueble", idCatalogoMueble)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método adicional: contar todos los cuarto muebles
    public Long count() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT COUNT(cm) FROM CuartoMueble cm", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para contar cuarto muebles por cuarto
    public Long countByCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(cm) FROM CuartoMueble cm WHERE cm.idCuarto = :idCuarto",
                            Long.class)
                    .setParameter("idCuarto", idCuarto)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para contar cuarto muebles por catálogo
    public Long countByCatalogoMueble(Integer idCatalogoMueble) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(cm) FROM CuartoMueble cm WHERE cm.idCatalogoMueble = :idCatalogoMueble",
                            Long.class)
                    .setParameter("idCatalogoMueble", idCatalogoMueble)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para sumar la cantidad total de muebles por cuarto
    public Long sumCantidadByCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT SUM(cm.cantidad) FROM CuartoMueble cm WHERE cm.idCuarto = :idCuarto",
                            Long.class)
                    .setParameter("idCuarto", idCuarto)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para buscar cuarto muebles con paginación
    public List<CuartoMueble> findPaginados(int inicio, int tamaño) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT cm FROM CuartoMueble cm", CuartoMueble.class)
                    .setFirstResult(inicio)
                    .setMaxResults(tamaño)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para eliminar todos los cuarto muebles de un cuarto
    public int deleteByCuarto(Integer idCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            int deleted = em.createQuery(
                            "DELETE FROM CuartoMueble cm WHERE cm.idCuarto = :idCuarto")
                    .setParameter("idCuarto", idCuarto)
                    .executeUpdate();
            em.getTransaction().commit();
            return deleted;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar cuarto muebles del cuarto", e);
        } finally {
            em.close();
        }
    }

    // Método para actualizar cantidad de un cuarto mueble
    public int updateCantidad(Integer idCuartoMueble, Integer nuevaCantidad) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            int updated = em.createQuery(
                            "UPDATE CuartoMueble cm SET cm.cantidad = :cantidad WHERE cm.idCuartoMueble = :id")
                    .setParameter("cantidad", nuevaCantidad)
                    .setParameter("id", idCuartoMueble)
                    .executeUpdate();
            em.getTransaction().commit();
            return updated;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar cantidad", e);
        } finally {
            em.close();
        }
    }

    // Método para incrementar cantidad
    public int incrementarCantidad(Integer idCuartoMueble, Integer incremento) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            int updated = em.createQuery(
                            "UPDATE CuartoMueble cm SET cm.cantidad = cm.cantidad + :incremento WHERE cm.idCuartoMueble = :id")
                    .setParameter("incremento", incremento)
                    .setParameter("id", idCuartoMueble)
                    .executeUpdate();
            em.getTransaction().commit();
            return updated;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al incrementar cantidad", e);
        } finally {
            em.close();
        }
    }

    // Método para decrementar cantidad
    public int decrementarCantidad(Integer idCuartoMueble, Integer decremento) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            int updated = em.createQuery(
                            "UPDATE CuartoMueble cm SET cm.cantidad = GREATEST(0, cm.cantidad - :decremento) WHERE cm.idCuartoMueble = :id")
                    .setParameter("decremento", decremento)
                    .setParameter("id", idCuartoMueble)
                    .executeUpdate();
            em.getTransaction().commit();
            return updated;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al decrementar cantidad", e);
        } finally {
            em.close();
        }
    }
}