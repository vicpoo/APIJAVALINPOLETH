// InvitadoRepository.java
package com.poleth.api.repository;

import com.poleth.api.config.DatabaseConfig;
import com.poleth.api.model.Invitado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class InvitadoRepository {

    // Método para guardar o actualizar un invitado
    public Invitado save(Invitado invitado) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            if (invitado.getIdInvitado() == null) {
                em.persist(invitado);
            } else {
                invitado = em.merge(invitado);
            }
            em.getTransaction().commit();
            return invitado;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar el invitado", e);
        } finally {
            em.close();
        }
    }

    // Método para obtener todos los invitados
    public List<Invitado> findAll() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT i FROM Invitado i", Invitado.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar invitado por ID
    public Optional<Invitado> findById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Invitado invitado = em.find(Invitado.class, id);
            return Optional.ofNullable(invitado);
        } finally {
            em.close();
        }
    }

    // Método para eliminar invitado
    public void delete(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            Invitado invitado = em.find(Invitado.class, id);
            if (invitado != null) {
                em.remove(invitado);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar el invitado", e);
        } finally {
            em.close();
        }
    }

    // Método para buscar invitado por email
    public Optional<Invitado> findByEmail(String email) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT i FROM Invitado i WHERE i.email = :email", Invitado.class)
                    .setParameter("email", email)
                    .getResultStream()
                    .findFirst();
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de invitado por email
    public boolean existsByEmail(String email) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(i) FROM Invitado i WHERE i.email = :email", Long.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para buscar invitados por nombre (puede haber múltiples con mismo nombre)
    public List<Invitado> findByNombre(String nombre) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT i FROM Invitado i WHERE i.nombre = :nombre", Invitado.class)
                    .setParameter("nombre", nombre)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar invitados por cuarto de acceso
    public List<Invitado> findByIdCuartoAcceso(Integer idCuartoAcceso) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT i FROM Invitado i WHERE i.idCuartoAcceso = :idCuartoAcceso", Invitado.class)
                    .setParameter("idCuartoAcceso", idCuartoAcceso)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar invitados por imagen vista
    public List<Invitado> findByIdImagenVista(Integer idImagenVista) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT i FROM Invitado i WHERE i.idImagenVista = :idImagenVista", Invitado.class)
                    .setParameter("idImagenVista", idImagenVista)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método adicional: contar todos los invitados
    public Long count() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT COUNT(i) FROM Invitado i", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Método adicional: buscar invitados que no tienen cuarto asignado
    public List<Invitado> findWithoutCuarto() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT i FROM Invitado i WHERE i.idCuartoAcceso IS NULL", Invitado.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método adicional: buscar invitados que no tienen imagen asignada
    public List<Invitado> findWithoutImagen() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT i FROM Invitado i WHERE i.idImagenVista IS NULL", Invitado.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}