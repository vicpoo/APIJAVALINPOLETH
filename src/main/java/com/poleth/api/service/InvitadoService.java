// InvitadoService.java
package com.poleth.api.service;

import com.poleth.api.model.Invitado;
import com.poleth.api.repository.InvitadoRepository;
import java.util.List;
import java.util.Optional;

public class InvitadoService {
    private final InvitadoRepository invitadoRepository;

    public InvitadoService(InvitadoRepository invitadoRepository) {
        this.invitadoRepository = invitadoRepository;
    }

    // Métodos CRUD básicos
    public Invitado saveInvitado(Invitado invitado) {
        return invitadoRepository.save(invitado);
    }

    public List<Invitado> getAllInvitados() {
        return invitadoRepository.findAll();
    }

    public Optional<Invitado> getInvitadoById(Integer id) {
        return invitadoRepository.findById(id);
    }

    public void deleteInvitado(Integer id) {
        invitadoRepository.delete(id);
    }

    // Método para crear un nuevo invitado con validaciones
    public Invitado createInvitado(Invitado invitado) {
        // Validaciones básicas
        if (invitado.getNombre() == null || invitado.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del invitado es requerido");
        }

        if (invitado.getEmail() == null || invitado.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email del invitado es requerido");
        }

        // Validar longitud de campos
        if (invitado.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
        }

        if (invitado.getEmail().length() > 100) {
            throw new IllegalArgumentException("El email no puede exceder 100 caracteres");
        }

        // Validar formato de email básico
        if (!isValidEmail(invitado.getEmail())) {
            throw new IllegalArgumentException("El formato del email no es válido");
        }

        // Guardar el invitado
        return invitadoRepository.save(invitado);
    }

    // Método para actualizar un invitado existente
    public Invitado updateInvitado(Integer id, Invitado invitadoActualizado) {
        // Validaciones básicas
        if (invitadoActualizado.getNombre() == null || invitadoActualizado.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del invitado es requerido");
        }

        if (invitadoActualizado.getEmail() == null || invitadoActualizado.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email del invitado es requerido");
        }

        // Validar longitud de campos
        if (invitadoActualizado.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
        }

        if (invitadoActualizado.getEmail().length() > 100) {
            throw new IllegalArgumentException("El email no puede exceder 100 caracteres");
        }

        // Validar formato de email básico
        if (!isValidEmail(invitadoActualizado.getEmail())) {
            throw new IllegalArgumentException("El formato del email no es válido");
        }

        // Buscar el invitado existente
        Optional<Invitado> invitadoExistenteOpt = invitadoRepository.findById(id);
        if (invitadoExistenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Invitado no encontrado con ID: " + id);
        }

        Invitado invitadoExistente = invitadoExistenteOpt.get();

        // Actualizar los campos
        invitadoExistente.setNombre(invitadoActualizado.getNombre());
        invitadoExistente.setEmail(invitadoActualizado.getEmail());
        invitadoExistente.setIdCuartoAcceso(invitadoActualizado.getIdCuartoAcceso());
        invitadoExistente.setIdImagenVista(invitadoActualizado.getIdImagenVista());

        // Guardar los cambios
        return invitadoRepository.save(invitadoExistente);
    }

    // Método para verificar si un invitado existe por ID
    public boolean existsById(Integer id) {
        return invitadoRepository.findById(id).isPresent();
    }

    // Método específico para Invitado
    public List<Invitado> getInvitadosByCuarto(Integer idCuartoAcceso) {
        return invitadoRepository.findByIdCuartoAcceso(idCuartoAcceso);
    }

    // Método auxiliar para validar formato de email
    private boolean isValidEmail(String email) {
        if (email == null) return false;

        // Validación básica de formato de email
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}