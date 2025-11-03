// PropietarioRepository.java
package com.poleth.api.repository;

import com.poleth.api.config.DatabaseConfig;
import com.poleth.api.model.Propietario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class PropietarioRepository {

    // Método para guardar o actualizar un propietario
    public Propietario save(Propietario propietario) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            if (propietario.getIdPropietario() == null) {
                em.persist(propietario);
            } else {
                propietario = em.merge(propietario);
            }
            em.getTransaction().commit();
            return propietario;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar el propietario", e);
        } finally {
            em.close();
        }
    }

    // Método para obtener todos los propietarios
    public List<Propietario> findAll() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Propietario p", Propietario.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar propietario por ID
    public Optional<Propietario> findById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Propietario propietario = em.find(Propietario.class, id);
            return Optional.ofNullable(propietario);
        } finally {
            em.close();
        }
    }

    // Método para eliminar propietario
    public void delete(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            Propietario propietario = em.find(Propietario.class, id);
            if (propietario != null) {
                em.remove(propietario);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar el propietario", e);
        } finally {
            em.close();
        }
    }

    // Método para buscar propietario por Gmail
    public Optional<Propietario> findByGmail(String gmail) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Propietario p WHERE p.gmail = :gmail", Propietario.class)
                    .setParameter("gmail", gmail)
                    .getResultStream()
                    .findFirst();
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    // Método para buscar propietarios por nombre (puede haber múltiples con mismo nombre)
    public List<Propietario> findByNombre(String nombre) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Propietario p WHERE p.nombre LIKE :nombre", Propietario.class)
                    .setParameter("nombre", "%" + nombre + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de propietario por Gmail
    public boolean existsByGmail(String gmail) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(p) FROM Propietario p WHERE p.gmail = :gmail", Long.class)
                    .setParameter("gmail", gmail)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de propietario por nombre
    public boolean existsByNombre(String nombre) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(p) FROM Propietario p WHERE p.nombre = :nombre", Long.class)
                    .setParameter("nombre", nombre)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método adicional: contar todos los propietarios
    public Long count() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT COUNT(p) FROM Propietario p", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

}