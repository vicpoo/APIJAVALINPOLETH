// InquilinoService.java
package com.poleth.api.service;

import com.poleth.api.model.Inquilino;
import com.poleth.api.repository.InquilinoRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InquilinoService {
    private final InquilinoRepository inquilinoRepository;

    public InquilinoService(InquilinoRepository inquilinoRepository) {
        this.inquilinoRepository = inquilinoRepository;
    }

    // Métodos CRUD básicos
    public Inquilino saveInquilino(Inquilino inquilino) {
        return inquilinoRepository.save(inquilino);
    }

    public List<Inquilino> getAllInquilinos() {
        return inquilinoRepository.findAll();
    }

    public Optional<Inquilino> getInquilinoById(Integer id) {
        return inquilinoRepository.findById(id);
    }

    public Optional<Inquilino> getInquilinoByEmail(String email) {
        return inquilinoRepository.findByEmail(email);
    }

    public Optional<Inquilino> getInquilinoByIne(String ine) {
        return inquilinoRepository.findByIne(ine);
    }

    public List<Inquilino> getInquilinosByNombre(String nombre) {
        return inquilinoRepository.findByNombre(nombre);
    }

    public List<Inquilino> getInquilinosByTelefono(String telefono) {
        return inquilinoRepository.findByTelefono(telefono);
    }

    public void deleteInquilino(Integer id) {
        inquilinoRepository.delete(id);
    }

    public boolean existsByEmail(String email) {
        return inquilinoRepository.existsByEmail(email);
    }

    public boolean existsByIne(String ine) {
        return inquilinoRepository.existsByIne(ine);
    }

    public boolean existsByNombre(String nombre) {
        return inquilinoRepository.existsByNombre(nombre);
    }

    // Método para crear un nuevo inquilino con validaciones
    public Inquilino createInquilino(Inquilino inquilino) {
        // Validaciones básicas
        if (inquilino.getNombreInquilino() == null || inquilino.getNombreInquilino().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del inquilino es requerido");
        }

        if (inquilino.getEmail() == null || inquilino.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email del inquilino es requerido");
        }

        if (inquilino.getIne() == null || inquilino.getIne().trim().isEmpty()) {
            throw new IllegalArgumentException("El INE del inquilino es requerido");
        }

        // Validar longitudes
        if (inquilino.getNombreInquilino().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
        }

        if (inquilino.getTelefonoInquilino() != null && inquilino.getTelefonoInquilino().length() > 20) {
            throw new IllegalArgumentException("El teléfono no puede exceder 20 caracteres");
        }

        if (inquilino.getEmail().length() > 100) {
            throw new IllegalArgumentException("El email no puede exceder 100 caracteres");
        }

        if (inquilino.getIne().length() > 50) {
            throw new IllegalArgumentException("El INE no puede exceder 50 caracteres");
        }

        // Validar formato de email
        if (!isValidEmailFormat(inquilino.getEmail())) {
            throw new IllegalArgumentException("El formato del email no es válido");
        }

        // Verificar si el email ya existe
        if (existsByEmail(inquilino.getEmail())) {
            throw new IllegalArgumentException("El email '" + inquilino.getEmail() + "' ya está registrado");
        }

        // Verificar si el INE ya existe
        if (existsByIne(inquilino.getIne())) {
            throw new IllegalArgumentException("El INE '" + inquilino.getIne() + "' ya está registrado");
        }

        // Guardar el inquilino
        return inquilinoRepository.save(inquilino);
    }

    // Método para actualizar un inquilino existente
    public Inquilino updateInquilino(Integer id, Inquilino inquilinoActualizado) {
        // Validaciones básicas
        if (inquilinoActualizado.getNombreInquilino() == null || inquilinoActualizado.getNombreInquilino().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del inquilino es requerido");
        }

        if (inquilinoActualizado.getEmail() == null || inquilinoActualizado.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email del inquilino es requerido");
        }

        if (inquilinoActualizado.getIne() == null || inquilinoActualizado.getIne().trim().isEmpty()) {
            throw new IllegalArgumentException("El INE del inquilino es requerido");
        }

        // Validar longitudes
        if (inquilinoActualizado.getNombreInquilino().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
        }

        if (inquilinoActualizado.getTelefonoInquilino() != null && inquilinoActualizado.getTelefonoInquilino().length() > 20) {
            throw new IllegalArgumentException("El teléfono no puede exceder 20 caracteres");
        }

        if (inquilinoActualizado.getEmail().length() > 100) {
            throw new IllegalArgumentException("El email no puede exceder 100 caracteres");
        }

        if (inquilinoActualizado.getIne().length() > 50) {
            throw new IllegalArgumentException("El INE no puede exceder 50 caracteres");
        }

        // Validar formato de email
        if (!isValidEmailFormat(inquilinoActualizado.getEmail())) {
            throw new IllegalArgumentException("El formato del email no es válido");
        }

        // Buscar el inquilino existente
        Optional<Inquilino> inquilinoExistenteOpt = inquilinoRepository.findById(id);
        if (inquilinoExistenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Inquilino no encontrado con ID: " + id);
        }

        Inquilino inquilinoExistente = inquilinoExistenteOpt.get();

        // Verificar si el nuevo email ya existe (excluyendo el inquilino actual)
        Optional<Inquilino> inquilinoConMismoEmail = inquilinoRepository.findByEmail(inquilinoActualizado.getEmail());
        if (inquilinoConMismoEmail.isPresent() && !inquilinoConMismoEmail.get().getIdInquilino().equals(id)) {
            throw new IllegalArgumentException("El email '" + inquilinoActualizado.getEmail() + "' ya está registrado");
        }

        // Verificar si el nuevo INE ya existe (excluyendo el inquilino actual)
        Optional<Inquilino> inquilinoConMismoIne = inquilinoRepository.findByIne(inquilinoActualizado.getIne());
        if (inquilinoConMismoIne.isPresent() && !inquilinoConMismoIne.get().getIdInquilino().equals(id)) {
            throw new IllegalArgumentException("El INE '" + inquilinoActualizado.getIne() + "' ya está registrado");
        }

        // Actualizar los campos
        inquilinoExistente.setNombreInquilino(inquilinoActualizado.getNombreInquilino());
        inquilinoExistente.setTelefonoInquilino(inquilinoActualizado.getTelefonoInquilino());
        inquilinoExistente.setEmail(inquilinoActualizado.getEmail());
        inquilinoExistente.setIne(inquilinoActualizado.getIne());

        // Guardar los cambios
        return inquilinoRepository.save(inquilinoExistente);
    }

    // Método para verificar si un inquilino existe por ID
    public boolean existsById(Integer id) {
        return inquilinoRepository.findById(id).isPresent();
    }

    // Método para buscar inquilinos por múltiples criterios
    public List<Inquilino> searchInquilinos(String nombre, String email, String telefono) {
        return inquilinoRepository.findByMultipleCriteria(nombre, email, telefono);
    }

    // Método para buscar inquilinos por término general - CORREGIDO
    public List<Inquilino> searchInquilinos(String termino) {
        List<Inquilino> todosInquilinos = inquilinoRepository.findAll();

        return todosInquilinos.stream()
                .filter(i -> i.getNombreInquilino().toLowerCase().contains(termino.toLowerCase()) ||
                        (i.getEmail() != null && i.getEmail().toLowerCase().contains(termino.toLowerCase())) ||
                        (i.getTelefonoInquilino() != null && i.getTelefonoInquilino().contains(termino)) ||
                        (i.getIne() != null && i.getIne().toLowerCase().contains(termino.toLowerCase())))
                .collect(Collectors.toList());
    }

    // Método de validación básica de formato de email
    private boolean isValidEmailFormat(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // Validación básica de formato de email
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    // Método para obtener estadísticas básicas
    public InquilinoStats getStats() {
        List<Inquilino> todosInquilinos = inquilinoRepository.findAll();
        Long totalInquilinos = (long) todosInquilinos.size();
        Long inquilinosConTelefono = todosInquilinos.stream()
                .filter(i -> i.getTelefonoInquilino() != null && !i.getTelefonoInquilino().trim().isEmpty())
                .count();

        return new InquilinoStats(totalInquilinos, inquilinosConTelefono);
    }

    // Clase interna para estadísticas
    public static class InquilinoStats {
        private final Long totalInquilinos;
        private final Long inquilinosConTelefono;

        public InquilinoStats(Long totalInquilinos, Long inquilinosConTelefono) {
            this.totalInquilinos = totalInquilinos;
            this.inquilinosConTelefono = inquilinosConTelefono;
        }

        public Long getTotalInquilinos() {
            return totalInquilinos;
        }

        public Long getInquilinosConTelefono() {
            return inquilinosConTelefono;
        }

        public Long getInquilinosSinTelefono() {
            return totalInquilinos - inquilinosConTelefono;
        }
    }
}