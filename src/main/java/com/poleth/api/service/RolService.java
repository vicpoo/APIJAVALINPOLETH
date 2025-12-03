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

    public Optional<Rol> getRolByTitulo(String titulo) {
        return rolRepository.findByTitulo(titulo);
    }

    public void deleteRol(Integer id) {
        rolRepository.delete(id);
    }

    public boolean existsByTitulo(String titulo) {
        return rolRepository.existsByTitulo(titulo);
    }

    // Método para crear un nuevo rol con validaciones
    public Rol createRol(Rol rol) {
        // Validaciones básicas
        if (rol.getTitulo() == null || rol.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título del rol es requerido");
        }

        // Validar longitud del título
        if (rol.getTitulo().length() > 50) {
            throw new IllegalArgumentException("El título del rol no puede exceder 50 caracteres");
        }

        // Verificar si el rol ya existe
        if (existsByTitulo(rol.getTitulo())) {
            throw new IllegalArgumentException("El rol '" + rol.getTitulo() + "' ya existe");
        }

        // Guardar el rol
        return rolRepository.save(rol);
    }

    // Método para actualizar un rol existente
    public Rol updateRol(Integer id, Rol rolActualizado) {
        // Validaciones básicas
        if (rolActualizado.getTitulo() == null || rolActualizado.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título del rol es requerido");
        }

        // Validar longitud del título
        if (rolActualizado.getTitulo().length() > 50) {
            throw new IllegalArgumentException("El título del rol no puede exceder 50 caracteres");
        }

        // Buscar el rol existente
        Optional<Rol> rolExistenteOpt = rolRepository.findById(id);
        if (rolExistenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Rol no encontrado con ID: " + id);
        }

        Rol rolExistente = rolExistenteOpt.get();

        // Verificar si el nuevo título ya existe (excluyendo el rol actual)
        Optional<Rol> rolConMismoTitulo = rolRepository.findByTitulo(rolActualizado.getTitulo());
        if (rolConMismoTitulo.isPresent() && !rolConMismoTitulo.get().getIdRoles().equals(id)) {
            throw new IllegalArgumentException("El rol '" + rolActualizado.getTitulo() + "' ya existe");
        }

        // Actualizar los campos
        rolExistente.setTitulo(rolActualizado.getTitulo());

        // Guardar los cambios
        return rolRepository.save(rolExistente);
    }
}