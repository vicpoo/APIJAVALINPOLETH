// CuartoRepository.java
package com.poleth.api.repository;

import com.poleth.api.config.DatabaseConfig;
import com.poleth.api.model.Cuarto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class CuartoRepository {

    // Método para guardar o actualizar un cuarto
    public Cuarto save(Cuarto cuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            if (cuarto.getIdCuarto() == null) {
                em.persist(cuarto);
            } else {
                cuarto = em.merge(cuarto);
            }
            em.getTransaction().commit();
            return cuarto;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar el cuarto", e);
        } finally {
            em.close();
        }
    }

    // Método para obtener todos los cuartos CON propietario - USANDO JOIN FETCH
    public List<Cuarto> findAll() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Cuarto c LEFT JOIN FETCH c.propietario ORDER BY c.createdAt DESC",
                    Cuarto.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar cuarto por ID CON propietario - USANDO JOIN FETCH
    public Optional<Cuarto> findById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            TypedQuery<Cuarto> query = em.createQuery(
                    "SELECT c FROM Cuarto c LEFT JOIN FETCH c.propietario WHERE c.idCuarto = :id",
                    Cuarto.class
            );
            query.setParameter("id", id);

            try {
                Cuarto cuarto = query.getSingleResult();
                return Optional.of(cuarto);
            } catch (NoResultException e) {
                return Optional.empty();
            }
        } finally {
            em.close();
        }
    }

    // Método para eliminar cuarto
    public void delete(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            Cuarto cuarto = em.find(Cuarto.class, id);
            if (cuarto != null) {
                em.remove(cuarto);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar el cuarto", e);
        } finally {
            em.close();
        }
    }

    // Método para buscar cuartos por propietario CON propietario - USANDO JOIN FETCH
    public List<Cuarto> findByIdPropietario(Integer idPropietario) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Cuarto c LEFT JOIN FETCH c.propietario " +
                                    "WHERE c.idPropietario = :idPropietario ORDER BY c.createdAt DESC",
                            Cuarto.class
                    )
                    .setParameter("idPropietario", idPropietario)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar cuartos por nombre CON propietario - USANDO JOIN FETCH
    public List<Cuarto> findByNombreCuarto(String nombreCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Cuarto c LEFT JOIN FETCH c.propietario WHERE c.nombreCuarto = :nombreCuarto",
                            Cuarto.class
                    )
                    .setParameter("nombreCuarto", nombreCuarto)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar cuartos por estado CON propietario - USANDO JOIN FETCH
    public List<Cuarto> findByEstado(String estadoCuarto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Cuarto c LEFT JOIN FETCH c.propietario " +
                                    "WHERE c.estadoCuarto = :estadoCuarto ORDER BY c.createdAt DESC",
                            Cuarto.class
                    )
                    .setParameter("estadoCuarto", estadoCuarto)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para obtener cuartos disponibles CON propietario - USANDO JOIN FETCH
    public List<Cuarto> findAvailable() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Cuarto c LEFT JOIN FETCH c.propietario " +
                                    "WHERE c.estadoCuarto = 'disponible' ORDER BY c.createdAt DESC",
                            Cuarto.class
                    )
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar cuartos que contengan texto en la descripción CON propietario - USANDO JOIN FETCH
    public List<Cuarto> findByDescripcionContaining(String texto) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Cuarto c LEFT JOIN FETCH c.propietario " +
                                    "WHERE c.descripcionCuarto LIKE :texto ORDER BY c.createdAt DESC",
                            Cuarto.class
                    )
                    .setParameter("texto", "%" + texto + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de cuarto por nombre y propietario
    public boolean existsByNombreAndPropietario(String nombreCuarto, Integer idPropietario) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(c) FROM Cuarto c WHERE c.nombreCuarto = :nombreCuarto AND c.idPropietario = :idPropietario",
                            Long.class
                    )
                    .setParameter("nombreCuarto", nombreCuarto)
                    .setParameter("idPropietario", idPropietario)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de cuarto por propietario
    public boolean existsByPropietario(Integer idPropietario) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(c) FROM Cuarto c WHERE c.idPropietario = :idPropietario",
                            Long.class
                    )
                    .setParameter("idPropietario", idPropietario)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método adicional: contar todos los cuartos
    public Long count() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT COUNT(c) FROM Cuarto c", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para contar cuartos por propietario
    public Long countByPropietario(Integer idPropietario) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(c) FROM Cuarto c WHERE c.idPropietario = :idPropietario",
                            Long.class
                    )
                    .setParameter("idPropietario", idPropietario)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de cuarto por ID
    public boolean existsById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(c) FROM Cuarto c WHERE c.idCuarto = :id",
                            Long.class
                    )
                    .setParameter("id", id)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }
}