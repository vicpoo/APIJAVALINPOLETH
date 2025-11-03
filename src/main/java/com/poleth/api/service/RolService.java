// RolService.java
package com.poleth.api.service;

import com.poleth.api.model.Rol;
import com.poleth.api.repository.RolRepository;
import java.util.List;
import java.util.Optional;

public class RolService {
    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    // Métodos CRUD básicos
    public Rol saveRol(Rol rol) {
        return rolRepository.save(rol);
    }

    public List<Rol> getAllRoles() {
        return rolRepository.findAll();
    }

    public Optional<Rol> getRolById(Integer id) {
        return rolRepository.findById(id);
    }

    public Optional<Rol> getRolByNombre(String nombreRol) {
        return rolRepository.findByNombreRol(nombreRol);
    }

    public void deleteRol(Integer id) {
        rolRepository.delete(id);
    }

    public boolean existsByNombreRol(String nombreRol) {
        return rolRepository.existsByNombreRol(nombreRol);
    }

    // Método para contar todos los roles
    public Long countRoles() {
        return rolRepository.count();
    }

    // Método para crear un nuevo rol con validaciones
    public Rol createRol(Rol rol) {
        // Validaciones básicas
        if (rol.getNombreRol() == null || rol.getNombreRol().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del rol es requerido");
        }

        // Validar longitud del nombre
        if (rol.getNombreRol().length() > 50) {
            throw new IllegalArgumentException("El nombre del rol no puede exceder 50 caracteres");
        }

        // Verificar si el rol ya existe
        if (existsByNombreRol(rol.getNombreRol())) {
            throw new IllegalArgumentException("El rol '" + rol.getNombreRol() + "' ya existe");
        }

        // Guardar el rol
        return rolRepository.save(rol);
    }

    // Método para actualizar un rol existente
    public Rol updateRol(Integer id, Rol rolActualizado) {
        // Validaciones básicas
        if (rolActualizado.getNombreRol() == null || rolActualizado.getNombreRol().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del rol es requerido");
        }

        // Validar longitud del nombre
        if (rolActualizado.getNombreRol().length() > 50) {
            throw new IllegalArgumentException("El nombre del rol no puede exceder 50 caracteres");
        }

        // Buscar el rol existente
        Optional<Rol> rolExistenteOpt = rolRepository.findById(id);
        if (rolExistenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Rol no encontrado con ID: " + id);
        }

        Rol rolExistente = rolExistenteOpt.get();

        // Verificar si el nuevo nombre ya existe (excluyendo el rol actual)
        Optional<Rol> rolConMismoNombre = rolRepository.findByNombreRol(rolActualizado.getNombreRol());
        if (rolConMismoNombre.isPresent() && !rolConMismoNombre.get().getIdRol().equals(id)) {
            throw new IllegalArgumentException("El rol '" + rolActualizado.getNombreRol() + "' ya existe");
        }

        // Actualizar los campos
        rolExistente.setNombreRol(rolActualizado.getNombreRol());

        // Guardar los cambios
        return rolRepository.save(rolExistente);
    }

    // Método para verificar si un rol existe por ID
    public boolean existsById(Integer id) {
        return rolRepository.findById(id).isPresent();
    }

    // Método para obtener roles con paginación (simplificado)
    public List<Rol> getRolesPaginados(int pagina, int tamaño) {
        List<Rol> todosRoles = rolRepository.findAll();
        int inicio = Math.max(0, (pagina - 1) * tamaño);
        int fin = Math.min(todosRoles.size(), inicio + tamaño);
        
        if (inicio >= todosRoles.size()) {
            return List.of();
        }
        
        return todosRoles.subList(inicio, fin);
    }
}