//UsuarioRepository.java
package com.poleth.api.repository;

import com.poleth.api.config.DatabaseConfig;
import com.poleth.api.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class UsuarioRepository {

    // Método para guardar o actualizar un usuario
    public Usuario save(Usuario usuario) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            if (usuario.getIdUsuario() == null) {
                em.persist(usuario);
            } else {
                usuario = em.merge(usuario);
            }
            em.getTransaction().commit();
            return usuario;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar el usuario", e);
        } finally {
            em.close();
        }
    }

    // Método para obtener todos los usuarios
    public List<Usuario> findAll() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u JOIN FETCH u.rol", Usuario.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar usuario por ID
    public Optional<Usuario> findById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Usuario usuario = em.createQuery(
                            "SELECT u FROM Usuario u JOIN FETCH u.rol WHERE u.idUsuario = :id", Usuario.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return Optional.ofNullable(usuario);
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    // Método para eliminar usuario
    public void delete(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            Usuario usuario = em.find(Usuario.class, id);
            if (usuario != null) {
                em.remove(usuario);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar el usuario", e);
        } finally {
            em.close();
        }
    }

    // Método para buscar usuario por username
    public Optional<Usuario> findByUsername(String username) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT u FROM Usuario u JOIN FETCH u.rol WHERE u.username = :username", Usuario.class)
                    .setParameter("username", username)
                    .getResultStream()
                    .findFirst();
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    // Método para buscar usuario por email
    public Optional<Usuario> findByEmail(String email) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT u FROM Usuario u JOIN FETCH u.rol WHERE u.email = :email", Usuario.class)
                    .setParameter("email", email)
                    .getResultStream()
                    .findFirst();
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de usuario por username
    public boolean existsByUsername(String username) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(u) FROM Usuario u WHERE u.username = :username", Long.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de usuario por email
    public boolean existsByEmail(String email) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(u) FROM Usuario u WHERE u.email = :email", Long.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para buscar usuarios por estado
    public List<Usuario> findByEstado(String estado) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT u FROM Usuario u JOIN FETCH u.rol WHERE u.estadoUsuario = :estado", Usuario.class)
                    .setParameter("estado", estado)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar usuarios por rol
    public List<Usuario> findByRolId(Integer rolId) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT u FROM Usuario u JOIN FETCH u.rol WHERE u.rol.idRoles = :rolId", Usuario.class)
                    .setParameter("rolId", rolId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}