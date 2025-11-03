// LoginRepository.java
package com.poleth.api.repository;

import com.poleth.api.config.DatabaseConfig;
import com.poleth.api.model.Login;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class LoginRepository {

    // Método para guardar o actualizar un login
    public Login save(Login login) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            if (login.getIdLogin() == null) {
                em.persist(login);
            } else {
                login = em.merge(login);
            }
            em.getTransaction().commit();
            return login;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar el login", e);
        } finally {
            em.close();
        }
    }

    // Método para obtener todos los logins
    public List<Login> findAll() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT l FROM Login l", Login.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar login por ID
    public Optional<Login> findById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Login login = em.find(Login.class, id);
            return Optional.ofNullable(login);
        } finally {
            em.close();
        }
    }

    // Método para eliminar login
    public void delete(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            Login login = em.find(Login.class, id);
            if (login != null) {
                em.remove(login);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar el login", e);
        } finally {
            em.close();
        }
    }

    // Método para buscar login por usuario
    public Optional<Login> findByUsuario(String usuario) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT l FROM Login l WHERE l.usuario = :usuario", Login.class)
                    .setParameter("usuario", usuario)
                    .getResultStream()
                    .findFirst();
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    // Método para buscar logins por rol
    public List<Login> findByRol(Integer idRol) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT l FROM Login l WHERE l.rol.idRol = :idRol", Login.class)
                    .setParameter("idRol", idRol)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar login por propietario
    public Optional<Login> findByPropietario(Integer idPropietario) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT l FROM Login l WHERE l.propietario.idPropietario = :idPropietario", Login.class)
                    .setParameter("idPropietario", idPropietario)
                    .getResultStream()
                    .findFirst();
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    // Método para buscar login por inquilino
    public Optional<Login> findByInquilino(Integer idInquilino) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT l FROM Login l WHERE l.inquilino.idInquilino = :idInquilino", Login.class)
                    .setParameter("idInquilino", idInquilino)
                    .getResultStream()
                    .findFirst();
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    // Método para buscar login por invitado
    public Optional<Login> findByInvitado(Integer idInvitado) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT l FROM Login l WHERE l.invitado.idInvitado = :idInvitado", Login.class)
                    .setParameter("idInvitado", idInvitado)
                    .getResultStream()
                    .findFirst();
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de login por usuario
    public boolean existsByUsuario(String usuario) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(l) FROM Login l WHERE l.usuario = :usuario", Long.class)
                    .setParameter("usuario", usuario)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para verificar si existe login para un propietario
    public boolean existsByPropietario(Integer idPropietario) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(l) FROM Login l WHERE l.propietario.idPropietario = :idPropietario", Long.class)
                    .setParameter("idPropietario", idPropietario)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para verificar si existe login para un inquilino
    public boolean existsByInquilino(Integer idInquilino) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(l) FROM Login l WHERE l.inquilino.idInquilino = :idInquilino", Long.class)
                    .setParameter("idInquilino", idInquilino)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para verificar si existe login para un invitado
    public boolean existsByInvitado(Integer idInvitado) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(l) FROM Login l WHERE l.invitado.idInvitado = :idInvitado", Long.class)
                    .setParameter("idInvitado", idInvitado)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método adicional: contar todos los logins
    public Long count() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT COUNT(l) FROM Login l", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método para buscar logins con paginación
    public List<Login> findPaginados(int inicio, int tamaño) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT l FROM Login l", Login.class)
                    .setFirstResult(inicio)
                    .setMaxResults(tamaño)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar logins por tipo de usuario
    public List<Login> findByTipoUsuario(String tipo) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            String query = "";
            switch (tipo.toUpperCase()) {
                case "PROPIETARIO":
                    query = "SELECT l FROM Login l WHERE l.propietario IS NOT NULL";
                    break;
                case "INQUILINO":
                    query = "SELECT l FROM Login l WHERE l.inquilino IS NOT NULL";
                    break;
                case "INVITADO":
                    query = "SELECT l FROM Login l WHERE l.invitado IS NOT NULL";
                    break;
                case "SISTEMA":
                    query = "SELECT l FROM Login l WHERE l.propietario IS NULL AND l.inquilino IS NULL AND l.invitado IS NULL";
                    break;
                default:
                    return List.of();
            }
            
            return em.createQuery(query, Login.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para autenticar usuario
    public Optional<Login> autenticar(String usuario, String contrasena) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT l FROM Login l WHERE l.usuario = :usuario AND l.contrasena = :contrasena", Login.class)
                    .setParameter("usuario", usuario)
                    .setParameter("contrasena", contrasena)
                    .getResultStream()
                    .findFirst();
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    // Método para actualizar contraseña
    public boolean actualizarContrasena(Integer idLogin, String nuevaContrasena) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            int updated = em.createQuery(
                            "UPDATE Login l SET l.contrasena = :contrasena WHERE l.idLogin = :id")
                    .setParameter("contrasena", nuevaContrasena)
                    .setParameter("id", idLogin)
                    .executeUpdate();
            em.getTransaction().commit();
            return updated > 0;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar contraseña", e);
        } finally {
            em.close();
        }
    }
}