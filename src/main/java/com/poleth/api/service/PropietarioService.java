// PropietarioService.java
package com.poleth.api.service;

import com.poleth.api.model.Propietario;
import com.poleth.api.repository.PropietarioRepository;
import java.util.List;
import java.util.Optional;

public class PropietarioService {
    private final PropietarioRepository propietarioRepository;

    public PropietarioService(PropietarioRepository propietarioRepository) {
        this.propietarioRepository = propietarioRepository;
    }

    // Métodos CRUD básicos
    public Propietario savePropietario(Propietario propietario) {
        return propietarioRepository.save(propietario);
    }

    public List<Propietario> getAllPropietarios() {
        return propietarioRepository.findAll();
    }

    public Optional<Propietario> getPropietarioById(Integer id) {
        return propietarioRepository.findById(id);
    }

    public Optional<Propietario> getPropietarioByGmail(String gmail) {
        return propietarioRepository.findByGmail(gmail);
    }

    public List<Propietario> getPropietariosByNombre(String nombre) {
        return propietarioRepository.findByNombre(nombre);
    }

    public void deletePropietario(Integer id) {
        propietarioRepository.delete(id);
    }

    public boolean existsByGmail(String gmail) {
        return propietarioRepository.existsByGmail(gmail);
    }

    public boolean existsByNombre(String nombre) {
        return propietarioRepository.existsByNombre(nombre);
    }

    // Método para crear un nuevo propietario con validaciones
    public Propietario createPropietario(Propietario propietario) {
        // Validaciones básicas
        if (propietario.getNombre() == null || propietario.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del propietario es requerido");
        }

        // Validar longitud del nombre
        if (propietario.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
        }

        // Validar formato de gmail si se proporciona
        if (propietario.getGmail() != null && !propietario.getGmail().trim().isEmpty()) {
            if (propietario.getGmail().length() > 100) {
                throw new IllegalArgumentException("El gmail no puede exceder 100 caracteres");
            }

            // Verificar si el gmail ya existe
            if (existsByGmail(propietario.getGmail())) {
                throw new IllegalArgumentException("El gmail '" + propietario.getGmail() + "' ya está registrado");
            }

            // Validación básica de formato de email
            if (!isValidEmailFormat(propietario.getGmail())) {
                throw new IllegalArgumentException("El formato del gmail no es válido");
            }
        }

        // Guardar el propietario
        return propietarioRepository.save(propietario);
    }

    // Método para actualizar un propietario existente
    public Propietario updatePropietario(Integer id, Propietario propietarioActualizado) {
        // Validaciones básicas
        if (propietarioActualizado.getNombre() == null || propietarioActualizado.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del propietario es requerido");
        }

        // Validar longitud del nombre
        if (propietarioActualizado.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
        }

        // Buscar el propietario existente
        Optional<Propietario> propietarioExistenteOpt = propietarioRepository.findById(id);
        if (propietarioExistenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Propietario no encontrado con ID: " + id);
        }

        Propietario propietarioExistente = propietarioExistenteOpt.get();

        // Validar gmail si se proporciona
        if (propietarioActualizado.getGmail() != null && !propietarioActualizado.getGmail().trim().isEmpty()) {
            if (propietarioActualizado.getGmail().length() > 100) {
                throw new IllegalArgumentException("El gmail no puede exceder 100 caracteres");
            }

            // Validación básica de formato de email
            if (!isValidEmailFormat(propietarioActualizado.getGmail())) {
                throw new IllegalArgumentException("El formato del gmail no es válido");
            }

            // Verificar si el nuevo gmail ya existe (excluyendo el propietario actual)
            Optional<Propietario> propietarioConMismoGmail = propietarioRepository.findByGmail(propietarioActualizado.getGmail());
            if (propietarioConMismoGmail.isPresent() && !propietarioConMismoGmail.get().getIdPropietario().equals(id)) {
                throw new IllegalArgumentException("El gmail '" + propietarioActualizado.getGmail() + "' ya está registrado");
            }
        }

        // Actualizar los campos
        propietarioExistente.setNombre(propietarioActualizado.getNombre());
        propietarioExistente.setGmail(propietarioActualizado.getGmail());

        // Guardar los cambios
        return propietarioRepository.save(propietarioExistente);
    }

    // Método de validación básica de formato de email
    private boolean isValidEmailFormat(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // Validación básica de formato de email
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}