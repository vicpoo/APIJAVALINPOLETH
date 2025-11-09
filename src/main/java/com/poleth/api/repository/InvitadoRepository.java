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
}