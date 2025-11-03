// InquilinoRepository.java
package com.poleth.api.repository;

import com.poleth.api.config.DatabaseConfig;
import com.poleth.api.model.Inquilino;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class InquilinoRepository {

    // Método para guardar o actualizar un inquilino
    public Inquilino save(Inquilino inquilino) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            if (inquilino.getIdInquilino() == null) {
                em.persist(inquilino);
            } else {
                inquilino = em.merge(inquilino);
            }
            em.getTransaction().commit();
            return inquilino;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar el inquilino", e);
        } finally {
            em.close();
        }
    }

    // Método para obtener todos los inquilinos
    public List<Inquilino> findAll() {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery("SELECT i FROM Inquilino i", Inquilino.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar inquilino por ID
    public Optional<Inquilino> findById(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Inquilino inquilino = em.find(Inquilino.class, id);
            return Optional.ofNullable(inquilino);
        } finally {
            em.close();
        }
    }

    // Método para eliminar inquilino
    public void delete(Integer id) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            em.getTransaction().begin();
            Inquilino inquilino = em.find(Inquilino.class, id);
            if (inquilino != null) {
                em.remove(inquilino);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar el inquilino", e);
        } finally {
            em.close();
        }
    }

    // Método para buscar inquilino por email
    public Optional<Inquilino> findByEmail(String email) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT i FROM Inquilino i WHERE i.email = :email", Inquilino.class)
                    .setParameter("email", email)
                    .getResultStream()
                    .findFirst();
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    // Método para buscar inquilino por INE
    public Optional<Inquilino> findByIne(String ine) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT i FROM Inquilino i WHERE i.ine = :ine", Inquilino.class)
                    .setParameter("ine", ine)
                    .getResultStream()
                    .findFirst();
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    // Método para buscar inquilinos por nombre
    public List<Inquilino> findByNombre(String nombre) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT i FROM Inquilino i WHERE i.nombreInquilino LIKE :nombre", Inquilino.class)
                    .setParameter("nombre", "%" + nombre + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para buscar inquilinos por teléfono
    public List<Inquilino> findByTelefono(String telefono) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT i FROM Inquilino i WHERE i.telefonoInquilino LIKE :telefono", Inquilino.class)
                    .setParameter("telefono", "%" + telefono + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de inquilino por email
    public boolean existsByEmail(String email) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(i) FROM Inquilino i WHERE i.email = :email", Long.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de inquilino por INE
    public boolean existsByIne(String ine) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(i) FROM Inquilino i WHERE i.ine = :ine", Long.class)
                    .setParameter("ine", ine)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Método para verificar existencia de inquilino por nombre
    public boolean existsByNombre(String nombre) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(i) FROM Inquilino i WHERE i.nombreInquilino = :nombre", Long.class)
                    .setParameter("nombre", nombre)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }


    // Método para buscar inquilinos por múltiples criterios
    public List<Inquilino> findByMultipleCriteria(String nombre, String email, String telefono) {
        EntityManager em = DatabaseConfig.createEntityManager();
        try {
            StringBuilder query = new StringBuilder("SELECT i FROM Inquilino i WHERE 1=1");
            
            if (nombre != null && !nombre.trim().isEmpty()) {
                query.append(" AND i.nombreInquilino LIKE :nombre");
            }
            if (email != null && !email.trim().isEmpty()) {
                query.append(" AND i.email LIKE :email");
            }
            if (telefono != null && !telefono.trim().isEmpty()) {
                query.append(" AND i.telefonoInquilino LIKE :telefono");
            }

            var typedQuery = em.createQuery(query.toString(), Inquilino.class);
            
            if (nombre != null && !nombre.trim().isEmpty()) {
                typedQuery.setParameter("nombre", "%" + nombre + "%");
            }
            if (email != null && !email.trim().isEmpty()) {
                typedQuery.setParameter("email", "%" + email + "%");
            }
            if (telefono != null && !telefono.trim().isEmpty()) {
                typedQuery.setParameter("telefono", "%" + telefono + "%");
            }

            return typedQuery.getResultList();
        } finally {
            em.close();
        }
    }
}