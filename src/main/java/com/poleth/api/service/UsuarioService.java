//UsuarioService.java
package com.poleth.api.service;

import com.poleth.api.model.Usuario;
import com.poleth.api.model.Rol;
import com.poleth.api.repository.UsuarioRepository;
import com.poleth.api.repository.RolRepository;
import com.poleth.api.util.JWTUtil;
import java.util.List;
import java.util.Optional;

public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    // Método para login
    public String login(String username, String password) {
        // Validaciones básicas
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El username es requerido");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es requerida");
        }

        // Buscar usuario por username
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos");
        }

        Usuario usuario = usuarioOpt.get();

        // Verificar estado del usuario
        if (!"activo".equalsIgnoreCase(usuario.getEstadoUsuario())) {
            throw new IllegalArgumentException("El usuario no está activo");
        }

        // Verificar contraseña
        if (!usuario.verificarPassword(password)) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos");
        }

        // Generar token JWT
        return JWTUtil.generarToken(usuario);
    }

    // Métodos CRUD básicos
    public Usuario saveUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioById(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> getUsuarioByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Optional<Usuario> getUsuarioByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public void deleteUsuario(Integer id) {
        usuarioRepository.delete(id);
    }

    public boolean existsByUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    // Método para crear un nuevo usuario con validaciones
    public Usuario createUsuario(Usuario usuario) {
        // Validaciones básicas
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El username es requerido");
        }

        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es requerida");
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es requerido");
        }

        if (usuario.getRol() == null || usuario.getRol().getIdRoles() == null) {
            throw new IllegalArgumentException("El rol es requerido");
        }

        // Validar longitudes
        if (usuario.getUsername().length() > 50) {
            throw new IllegalArgumentException("El username no puede exceder 50 caracteres");
        }

        if (usuario.getEmail().length() > 100) {
            throw new IllegalArgumentException("El email no puede exceder 100 caracteres");
        }

        if (usuario.getTelefono() != null && usuario.getTelefono().length() > 20) {
            throw new IllegalArgumentException("El teléfono no puede exceder 20 caracteres");
        }

        // Verificar si el username ya existe
        if (existsByUsername(usuario.getUsername())) {
            throw new IllegalArgumentException("El username '" + usuario.getUsername() + "' ya existe");
        }

        // Verificar si el email ya existe
        if (existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El email '" + usuario.getEmail() + "' ya existe");
        }

        // Verificar que el rol exista
        Optional<Rol> rolExistente = rolRepository.findById(usuario.getRol().getIdRoles());
        if (rolExistente.isEmpty()) {
            throw new IllegalArgumentException("El rol especificado no existe");
        }

        // Asignar el rol completo al usuario
        usuario.setRol(rolExistente.get());

        // La contraseña se hashea automáticamente en el setter
        // Guardar el usuario
        return usuarioRepository.save(usuario);
    }

    // Método para actualizar un usuario existente
    public Usuario updateUsuario(Integer id, Usuario usuarioActualizado) {
        // Validaciones básicas
        if (usuarioActualizado.getUsername() == null || usuarioActualizado.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El username es requerido");
        }

        if (usuarioActualizado.getEmail() == null || usuarioActualizado.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es requerido");
        }

        if (usuarioActualizado.getRol() == null || usuarioActualizado.getRol().getIdRoles() == null) {
            throw new IllegalArgumentException("El rol es requerido");
        }

        // Validar longitudes
        if (usuarioActualizado.getUsername().length() > 50) {
            throw new IllegalArgumentException("El username no puede exceder 50 caracteres");
        }

        if (usuarioActualizado.getEmail().length() > 100) {
            throw new IllegalArgumentException("El email no puede exceder 100 caracteres");
        }

        if (usuarioActualizado.getTelefono() != null && usuarioActualizado.getTelefono().length() > 20) {
            throw new IllegalArgumentException("El teléfono no puede exceder 20 caracteres");
        }

        // Buscar el usuario existente
        Optional<Usuario> usuarioExistenteOpt = usuarioRepository.findById(id);
        if (usuarioExistenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado con ID: " + id);
        }

        Usuario usuarioExistente = usuarioExistenteOpt.get();

        // Verificar si el nuevo username ya existe (excluyendo el usuario actual)
        Optional<Usuario> usuarioConMismoUsername = usuarioRepository.findByUsername(usuarioActualizado.getUsername());
        if (usuarioConMismoUsername.isPresent() && !usuarioConMismoUsername.get().getIdUsuario().equals(id)) {
            throw new IllegalArgumentException("El username '" + usuarioActualizado.getUsername() + "' ya existe");
        }

        // Verificar si el nuevo email ya existe (excluyendo el usuario actual)
        Optional<Usuario> usuarioConMismoEmail = usuarioRepository.findByEmail(usuarioActualizado.getEmail());
        if (usuarioConMismoEmail.isPresent() && !usuarioConMismoEmail.get().getIdUsuario().equals(id)) {
            throw new IllegalArgumentException("El email '" + usuarioActualizado.getEmail() + "' ya existe");
        }

        // Verificar que el rol exista
        Optional<Rol> rolExistente = rolRepository.findById(usuarioActualizado.getRol().getIdRoles());
        if (rolExistente.isEmpty()) {
            throw new IllegalArgumentException("El rol especificado no existe");
        }

        // Actualizar los campos
        usuarioExistente.setUsername(usuarioActualizado.getUsername());
        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
        usuarioExistente.setRol(rolExistente.get());
        usuarioExistente.setEstadoUsuario(usuarioActualizado.getEstadoUsuario());

        // Solo actualizar la contraseña si se proporciona una nueva
        if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().trim().isEmpty()) {
            usuarioExistente.setPassword(usuarioActualizado.getPassword());
        }

        // Guardar los cambios
        return usuarioRepository.save(usuarioExistente);
    }

    // Método para cambiar el estado de un usuario
    public Usuario cambiarEstadoUsuario(Integer id, String nuevoEstado) {
        Optional<Usuario> usuarioExistenteOpt = usuarioRepository.findById(id);
        if (usuarioExistenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado con ID: " + id);
        }

        Usuario usuarioExistente = usuarioExistenteOpt.get();
        usuarioExistente.setEstadoUsuario(nuevoEstado);

        return usuarioRepository.save(usuarioExistente);
    }

    // Métodos adicionales
    public List<Usuario> getUsuariosByEstado(String estado) {
        return usuarioRepository.findByEstado(estado);
    }

    public List<Usuario> getUsuariosByRol(Integer rolId) {
        return usuarioRepository.findByRolId(rolId);
    }
}