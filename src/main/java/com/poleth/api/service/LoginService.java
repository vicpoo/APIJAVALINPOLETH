// LoginService.java
package com.poleth.api.service;

import com.poleth.api.model.Login;
import com.poleth.api.model.Rol;
import com.poleth.api.model.Propietario;
import com.poleth.api.model.Inquilino;
import com.poleth.api.repository.LoginRepository;
import com.poleth.api.repository.RolRepository;
import com.poleth.api.repository.PropietarioRepository;
import com.poleth.api.repository.InquilinoRepository;
import com.poleth.api.util.JWTUtil;
import java.util.List;
import java.util.Optional;

public class LoginService {
    private final LoginRepository loginRepository;
    private final RolRepository rolRepository;
    private final PropietarioRepository propietarioRepository;
    private final InquilinoRepository inquilinoRepository;

    public LoginService(LoginRepository loginRepository, RolRepository rolRepository,
                        PropietarioRepository propietarioRepository, InquilinoRepository inquilinoRepository) {
        this.loginRepository = loginRepository;
        this.rolRepository = rolRepository;
        this.propietarioRepository = propietarioRepository;
        this.inquilinoRepository = inquilinoRepository;
    }

    // Métodos CRUD básicos
    public Login saveLogin(Login login) {
        return loginRepository.save(login);
    }

    public List<Login> getAllLogins() {
        return loginRepository.findAll();
    }

    public Optional<Login> getLoginById(Integer id) {
        return loginRepository.findById(id);
    }

    public Optional<Login> getLoginByUsuario(String usuario) {
        return loginRepository.findByUsuario(usuario);
    }

    public void deleteLogin(Integer id) {
        loginRepository.delete(id);
    }

    public boolean existsByUsuario(String usuario) {
        return loginRepository.existsByUsuario(usuario);
    }

    // Método para crear un nuevo login con validaciones
    public Login createLogin(Login login) {
        // Validaciones básicas
        if (login.getUsuario() == null || login.getUsuario().trim().isEmpty()) {
            throw new IllegalArgumentException("El usuario es requerido");
        }

        if (login.getContrasena() == null || login.getContrasena().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es requerida");
        }

        if (login.getRol() == null || login.getRol().getIdRol() == null) {
            throw new IllegalArgumentException("El rol es requerido");
        }

        // Validar longitudes
        if (login.getUsuario().length() > 50) {
            throw new IllegalArgumentException("El usuario no puede exceder 50 caracteres");
        }

        if (login.getContrasena().length() > 255) {
            throw new IllegalArgumentException("La contraseña no puede exceder 255 caracteres");
        }

        // Verificar si el usuario ya existe
        if (existsByUsuario(login.getUsuario())) {
            throw new IllegalArgumentException("El usuario '" + login.getUsuario() + "' ya está registrado");
        }

        // Verificar que el rol exista
        Optional<Rol> rolExistente = rolRepository.findById(login.getRol().getIdRol());
        if (rolExistente.isEmpty()) {
            throw new IllegalArgumentException("El rol especificado no existe");
        }
        login.setRol(rolExistente.get());

        // Guardar el login
        return loginRepository.save(login);
    }

    // Método para actualizar un login existente
    public Login updateLogin(Integer id, Login loginActualizado) {
        // Validaciones básicas
        if (loginActualizado.getUsuario() == null || loginActualizado.getUsuario().trim().isEmpty()) {
            throw new IllegalArgumentException("El usuario es requerido");
        }

        if (loginActualizado.getContrasena() == null || loginActualizado.getContrasena().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es requerida");
        }

        if (loginActualizado.getRol() == null || loginActualizado.getRol().getIdRol() == null) {
            throw new IllegalArgumentException("El rol es requerido");
        }

        // Validar longitudes
        if (loginActualizado.getUsuario().length() > 50) {
            throw new IllegalArgumentException("El usuario no puede exceder 50 caracteres");
        }

        if (loginActualizado.getContrasena().length() > 255) {
            throw new IllegalArgumentException("La contraseña no puede exceder 255 caracteres");
        }

        // Buscar el login existente
        Optional<Login> loginExistenteOpt = loginRepository.findById(id);
        if (loginExistenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Login no encontrado con ID: " + id);
        }

        Login loginExistente = loginExistenteOpt.get();

        // Verificar si el nuevo usuario ya existe (excluyendo el login actual)
        Optional<Login> loginConMismoUsuario = loginRepository.findByUsuario(loginActualizado.getUsuario());
        if (loginConMismoUsuario.isPresent() && !loginConMismoUsuario.get().getIdLogin().equals(id)) {
            throw new IllegalArgumentException("El usuario '" + loginActualizado.getUsuario() + "' ya está registrado");
        }

        // Verificar que el rol exista
        Optional<Rol> rolExistente = rolRepository.findById(loginActualizado.getRol().getIdRol());
        if (rolExistente.isEmpty()) {
            throw new IllegalArgumentException("El rol especificado no existe");
        }

        // Actualizar los campos
        loginExistente.setUsuario(loginActualizado.getUsuario());
        loginExistente.setContrasena(loginActualizado.getContrasena());
        loginExistente.setRol(rolExistente.get());

        // Guardar los cambios
        return loginRepository.save(loginExistente);
    }

    // Método para autenticar usuario y generar JWT
    public AuthResponse autenticar(String usuario, String contrasena) {
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El usuario es requerido");
        }

        if (contrasena == null || contrasena.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es requerida");
        }

        Optional<Login> login = loginRepository.autenticar(usuario, contrasena);

        if (login.isPresent()) {
            String token = JWTUtil.generarToken(login.get());
            return new AuthResponse(token, login.get());
        } else {
            throw new IllegalArgumentException("Credenciales inválidas");
        }
    }

    // Método para cambiar contraseña
    public boolean cambiarContrasena(Integer idLogin, String nuevaContrasena) {
        if (nuevaContrasena == null || nuevaContrasena.trim().isEmpty()) {
            throw new IllegalArgumentException("La nueva contraseña es requerida");
        }

        if (nuevaContrasena.length() > 255) {
            throw new IllegalArgumentException("La contraseña no puede exceder 255 caracteres");
        }

        // Verificar que el login exista
        Optional<Login> login = loginRepository.findById(idLogin);
        if (login.isEmpty()) {
            throw new IllegalArgumentException("Login no encontrado con ID: " + idLogin);
        }

        return loginRepository.actualizarContrasena(idLogin, nuevaContrasena);
    }

    // Método para buscar logins por rol
    public List<Login> getLoginsByRol(Integer idRol) {
        return loginRepository.findByRol(idRol);
    }

    // Método para obtener login por propietario
    public Optional<Login> getLoginByPropietario(Integer idPropietario) {
        return loginRepository.findByPropietario(idPropietario);
    }

    // Método para obtener login por inquilino
    public Optional<Login> getLoginByInquilino(Integer idInquilino) {
        return loginRepository.findByInquilino(idInquilino);
    }

    // Método para verificar si un login existe por ID
    public boolean existsById(Integer id) {
        return loginRepository.findById(id).isPresent();
    }

    // Clase para respuesta de autenticación
    public static class AuthResponse {
        private final String token;
        private final Login login;

        public AuthResponse(String token, Login login) {
            this.token = token;
            this.login = login;
        }

        public String getToken() {
            return token;
        }

        public Login getLogin() {
            return login;
        }
    }
}