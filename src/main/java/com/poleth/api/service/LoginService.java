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

        // Validar relaciones únicas
        if (login.getPropietario() != null && login.getPropietario().getIdPropietario() != null) {
            Optional<Propietario> propietarioExistente = propietarioRepository.findById(login.getPropietario().getIdPropietario());
            if (propietarioExistente.isEmpty()) {
                throw new IllegalArgumentException("El propietario especificado no existe");
            }
            login.setPropietario(propietarioExistente.get());

            // Verificar que no exista ya un login para este propietario
            if (loginRepository.existsByPropietario(login.getPropietario().getIdPropietario())) {
                throw new IllegalArgumentException("Ya existe un login para este propietario");
            }
        }

        if (login.getInquilino() != null && login.getInquilino().getIdInquilino() != null) {
            Optional<Inquilino> inquilinoExistente = inquilinoRepository.findById(login.getInquilino().getIdInquilino());
            if (inquilinoExistente.isEmpty()) {
                throw new IllegalArgumentException("El inquilino especificado no existe");
            }
            login.setInquilino(inquilinoExistente.get());

            // Verificar que no exista ya un login para este inquilino
            if (loginRepository.existsByInquilino(login.getInquilino().getIdInquilino())) {
                throw new IllegalArgumentException("Ya existe un login para este inquilino");
            }
        }

        // Solo puede tener una relación (propietario, inquilino o invitado)
        int relaciones = 0;
        if (login.getPropietario() != null) relaciones++;
        if (login.getInquilino() != null) relaciones++;
        if (login.getInvitado() != null) relaciones++;

        if (relaciones > 1) {
            throw new IllegalArgumentException("El login solo puede estar asociado a un tipo de usuario (propietario, inquilino o invitado)");
        }

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

    // Método para autenticar usuario
    public Optional<Login> autenticar(String usuario, String contrasena) {
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El usuario es requerido");
        }

        if (contrasena == null || contrasena.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es requerida");
        }

        return loginRepository.autenticar(usuario, contrasena);
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

    // Método para buscar logins por tipo de usuario
    public List<Login> getLoginsByTipoUsuario(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de usuario es requerido");
        }

        String tipoUpper = tipo.toUpperCase();
        if (!tipoUpper.equals("PROPIETARIO") && !tipoUpper.equals("INQUILINO") && 
            !tipoUpper.equals("INVITADO") && !tipoUpper.equals("SISTEMA")) {
            throw new IllegalArgumentException("Tipo de usuario no válido. Use: PROPIETARIO, INQUILINO, INVITADO o SISTEMA");
        }

        return loginRepository.findByTipoUsuario(tipoUpper);
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

    // Método para contar todos los logins
    public Long countLogins() {
        return loginRepository.count();
    }

    // Método para obtener estadísticas básicas
    public LoginStats getStats() {
        List<Login> todosLogins = loginRepository.findAll();
        Long totalLogins = (long) todosLogins.size();
        Long loginsPropietarios = todosLogins.stream()
                .filter(Login::isPropietario)
                .count();
        Long loginsInquilinos = todosLogins.stream()
                .filter(Login::isInquilino)
                .count();
        Long loginsSistema = todosLogins.stream()
                .filter(l -> !l.isPropietario() && !l.isInquilino() && !l.isInvitado())
                .count();

        return new LoginStats(totalLogins, loginsPropietarios, loginsInquilinos, loginsSistema);
    }

    // Clase interna para estadísticas
    public static class LoginStats {
        private final Long totalLogins;
        private final Long loginsPropietarios;
        private final Long loginsInquilinos;
        private final Long loginsSistema;

        public LoginStats(Long totalLogins, Long loginsPropietarios, Long loginsInquilinos, Long loginsSistema) {
            this.totalLogins = totalLogins;
            this.loginsPropietarios = loginsPropietarios;
            this.loginsInquilinos = loginsInquilinos;
            this.loginsSistema = loginsSistema;
        }

        public Long getTotalLogins() {
            return totalLogins;
        }

        public Long getLoginsPropietarios() {
            return loginsPropietarios;
        }

        public Long getLoginsInquilinos() {
            return loginsInquilinos;
        }

        public Long getLoginsSistema() {
            return loginsSistema;
        }

        public Long getLoginsInvitados() {
            return totalLogins - loginsPropietarios - loginsInquilinos - loginsSistema;
        }
    }
}