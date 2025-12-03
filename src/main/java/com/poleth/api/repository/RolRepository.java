// RolRepository.java
package com.poleth.api.repository;

import com.poleth.api.config.DatabaseConfig;
import com.poleth.api.model.Rol;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class RolRepository {

    // Método para guardar o actualizar un rol
    public Rol save(Rol rol) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            if (rol.getIdRoles() == null) {
                em.persist(rol);
            } else {
                rol = em.merge(rol);
            }
            em.getTransaction().commit();
            return rol;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar el rol", e);
        } finally {
            em.close();
        }
    }

    // Método para obtener todos los roles
    public List<Rol> findAll() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT r FROM Rol r", Rol.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar rol por ID
    public Optional<Rol> findById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Rol rol = em.find(Rol.class, id);
            return Optional.ofNullable(rol);
        } finally {
            em.close();
        }
    }

    // Método para eliminar rol
    public void delete(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            Rol rol = em.find(Rol.class, id);
            if (rol != null) {
                em.remove(rol);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar el rol", e);
        } finally {
            em.close();
        }
    }

    // Método para buscar rol por título (antes era por nombre)
    public Optional<Rol> findByTitulo(String titulo) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT r FROM Rol r WHERE r.titulo = :titulo", Rol.class)
                    .setParameter("titulo", titulo)
                    .getResultStream()
                    .findFirst();
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de rol por título
    public boolean existsByTitulo(String titulo) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(r) FROM Rol r WHERE r.titulo = :titulo", Long.class)
                    .setParameter("titulo", titulo)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }
}