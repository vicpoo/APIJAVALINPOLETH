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

    public Optional<Invitado> getInvitadoByEmail(String email) {
        return invitadoRepository.findByEmail(email);
    }

    public void deleteInvitado(Integer id) {
        invitadoRepository.delete(id);
    }

    public boolean existsByEmail(String email) {
        return invitadoRepository.existsByEmail(email);
    }

    // Método para contar todos los invitados
    public Long countInvitados() {
        return invitadoRepository.count();
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

        // Verificar si el email ya existe
        if (existsByEmail(invitado.getEmail())) {
            throw new IllegalArgumentException("El email '" + invitado.getEmail() + "' ya está registrado");
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

        // Verificar si el nuevo email ya existe (excluyendo el invitado actual)
        Optional<Invitado> invitadoConMismoEmail = invitadoRepository.findByEmail(invitadoActualizado.getEmail());
        if (invitadoConMismoEmail.isPresent() && !invitadoConMismoEmail.get().getIdInvitado().equals(id)) {
            throw new IllegalArgumentException("El email '" + invitadoActualizado.getEmail() + "' ya está registrado");
        }

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


    // Métodos específicos para Invitado
    public List<Invitado> getInvitadosByNombre(String nombre) {
        return invitadoRepository.findByNombre(nombre);
    }

    public List<Invitado> getInvitadosByCuarto(Integer idCuartoAcceso) {
        return invitadoRepository.findByIdCuartoAcceso(idCuartoAcceso);
    }

    public List<Invitado> getInvitadosByImagen(Integer idImagenVista) {
        return invitadoRepository.findByIdImagenVista(idImagenVista);
    }

    public List<Invitado> getInvitadosWithoutCuarto() {
        return invitadoRepository.findWithoutCuarto();
    }

    public List<Invitado> getInvitadosWithoutImagen() {
        return invitadoRepository.findWithoutImagen();
    }

    // Método para asignar cuarto a un invitado
    public Invitado asignarCuarto(Integer idInvitado, Integer idCuartoAcceso) {
        Optional<Invitado> invitadoOpt = invitadoRepository.findById(idInvitado);
        if (invitadoOpt.isEmpty()) {
            throw new IllegalArgumentException("Invitado no encontrado con ID: " + idInvitado);
        }

        Invitado invitado = invitadoOpt.get();
        invitado.setIdCuartoAcceso(idCuartoAcceso);

        return invitadoRepository.save(invitado);
    }

    // Método para asignar imagen a un invitado
    public Invitado asignarImagen(Integer idInvitado, Integer idImagenVista) {
        Optional<Invitado> invitadoOpt = invitadoRepository.findById(idInvitado);
        if (invitadoOpt.isEmpty()) {
            throw new IllegalArgumentException("Invitado no encontrado con ID: " + idInvitado);
        }

        Invitado invitado = invitadoOpt.get();
        invitado.setIdImagenVista(idImagenVista);

        return invitadoRepository.save(invitado);
    }

    // Método para remover cuarto de un invitado
    public Invitado removerCuarto(Integer idInvitado) {
        Optional<Invitado> invitadoOpt = invitadoRepository.findById(idInvitado);
        if (invitadoOpt.isEmpty()) {
            throw new IllegalArgumentException("Invitado no encontrado con ID: " + idInvitado);
        }

        Invitado invitado = invitadoOpt.get();
        invitado.setIdCuartoAcceso(null);

        return invitadoRepository.save(invitado);
    }

    // Método para remover imagen de un invitado
    public Invitado removerImagen(Integer idInvitado) {
        Optional<Invitado> invitadoOpt = invitadoRepository.findById(idInvitado);
        if (invitadoOpt.isEmpty()) {
            throw new IllegalArgumentException("Invitado no encontrado con ID: " + idInvitado);
        }

        Invitado invitado = invitadoOpt.get();
        invitado.setIdImagenVista(null);

        return invitadoRepository.save(invitado);
    }

    // Método auxiliar para validar formato de email
    private boolean isValidEmail(String email) {
        if (email == null) return false;
        
        // Validación básica de formato de email
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}