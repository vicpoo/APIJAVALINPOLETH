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
import com.poleth.api.util.PasswordUtil;
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
        List<Login> logins = loginRepository.findAll();
        // Por seguridad, no devolvemos las contraseñas
        logins.forEach(login -> login.setContrasena(null));
        return logins;
    }

    public Optional<Login> getLoginById(Integer id) {
        Optional<Login> login = loginRepository.findById(id);
        // Por seguridad, no devolvemos la contraseña
        login.ifPresent(l -> l.setContrasena(null));
        return login;
    }

    public Optional<Login> getLoginByUsuario(String usuario) {
        Optional<Login> login = loginRepository.findByUsuario(usuario);
        // Por seguridad, no devolvemos la contraseña
        login.ifPresent(l -> l.setContrasena(null));
        return login;
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

        // Validar fortaleza de contraseña (opcional pero recomendado)
        if (!esContrasenaSegura(login.getContrasena())) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres, incluir mayúsculas, minúsculas y números");
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

        // ENCRIPTAR CONTRASEÑA antes de guardar
        String hashedPassword = PasswordUtil.hashPassword(login.getContrasena());
        login.setContrasena(hashedPassword);

        // Guardar el login
        Login savedLogin = loginRepository.save(login);

        // Por seguridad, no devolvemos la contraseña
        savedLogin.setContrasena(null);
        return savedLogin;
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

        // ENCRIPTAR NUEVA CONTRASEÑA
        String hashedPassword = PasswordUtil.hashPassword(loginActualizado.getContrasena());

        // Actualizar los campos
        loginExistente.setUsuario(loginActualizado.getUsuario());
        loginExistente.setContrasena(hashedPassword);
        loginExistente.setRol(rolExistente.get());

        // Guardar los cambios
        Login updatedLogin = loginRepository.save(loginExistente);

        // Por seguridad, no devolvemos la contraseña
        updatedLogin.setContrasena(null);
        return updatedLogin;
    }

    // Método para autenticar usuario y generar JWT
    public AuthResponse autenticar(String usuario, String contrasena) {
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El usuario es requerido");
        }

        if (contrasena == null || contrasena.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es requerida");
        }

        // Buscar usuario por nombre de usuario
        Optional<Login> loginOpt = loginRepository.findByUsuario(usuario);

        if (loginOpt.isPresent()) {
            Login login = loginOpt.get();

            // VERIFICAR CONTRASEÑA CON HASH
            if (PasswordUtil.checkPassword(contrasena, login.getContrasena())) {
                String token = JWTUtil.generarToken(login);

                // No devolvemos la contraseña por seguridad
                login.setContrasena(null);

                return new AuthResponse(token, login);
            }
        }

        throw new IllegalArgumentException("Credenciales inválidas");
    }

    // Método para cambiar contraseña
    public boolean cambiarContrasena(Integer idLogin, String nuevaContrasena) {
        if (nuevaContrasena == null || nuevaContrasena.trim().isEmpty()) {
            throw new IllegalArgumentException("La nueva contraseña es requerida");
        }

        if (nuevaContrasena.length() > 255) {
            throw new IllegalArgumentException("La contraseña no puede exceder 255 caracteres");
        }

        // Validar fortaleza de contraseña
        if (!esContrasenaSegura(nuevaContrasena)) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres, incluir mayúsculas, minúsculas y números");
        }

        // Verificar que el login exista
        Optional<Login> login = loginRepository.findById(idLogin);
        if (login.isEmpty()) {
            throw new IllegalArgumentException("Login no encontrado con ID: " + idLogin);
        }

        // ENCRIPTAR NUEVA CONTRASEÑA
        String hashedPassword = PasswordUtil.hashPassword(nuevaContrasena);

        return loginRepository.actualizarContrasena(idLogin, hashedPassword);
    }

    // Método para buscar logins por rol
    public List<Login> getLoginsByRol(Integer idRol) {
        List<Login> logins = loginRepository.findByRol(idRol);
        // Por seguridad, no devolvemos las contraseñas
        logins.forEach(login -> login.setContrasena(null));
        return logins;
    }

    // Método para obtener login por propietario
    public Optional<Login> getLoginByPropietario(Integer idPropietario) {
        Optional<Login> login = loginRepository.findByPropietario(idPropietario);
        // Por seguridad, no devolvemos la contraseña
        login.ifPresent(l -> l.setContrasena(null));
        return login;
    }

    // Método para obtener login por inquilino
    public Optional<Login> getLoginByInquilino(Integer idInquilino) {
        Optional<Login> login = loginRepository.findByInquilino(idInquilino);
        // Por seguridad, no devolvemos la contraseña
        login.ifPresent(l -> l.setContrasena(null));
        return login;
    }

    // Método para verificar si un login existe por ID
    public boolean existsById(Integer id) {
        return loginRepository.findById(id).isPresent();
    }

    // Método auxiliar para validar fortaleza de contraseña
    private boolean esContrasenaSegura(String contrasena) {
        if (contrasena == null || contrasena.length() < 8) {
            return false;
        }

        // Verificar que tenga al menos una mayúscula, una minúscula y un número
        boolean tieneMayuscula = false;
        boolean tieneMinuscula = false;
        boolean tieneNumero = false;

        for (char c : contrasena.toCharArray()) {
            if (Character.isUpperCase(c)) tieneMayuscula = true;
            if (Character.isLowerCase(c)) tieneMinuscula = true;
            if (Character.isDigit(c)) tieneNumero = true;
        }

        return tieneMayuscula && tieneMinuscula && tieneNumero;
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