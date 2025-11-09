// LoginController.java
package com.poleth.api.controller;

import com.poleth.api.model.Login;
import com.poleth.api.service.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.List;
import java.util.Optional;

public class LoginController {
    private final LoginService loginService;
    private final ObjectMapper objectMapper;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
        this.objectMapper = new ObjectMapper();
    }

    // Crear un nuevo login
    public void createLogin(Context ctx) {
        try {
            Login login = objectMapper.readValue(ctx.body(), Login.class);

            Login savedLogin = loginService.createLogin(login);
            ctx.status(HttpStatus.CREATED)
                    .json(savedLogin);
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al crear el login: " + e.getMessage());
        }
    }

    // Obtener todos los logins
    public void getAllLogins(Context ctx) {
        try {
            ctx.json(loginService.getAllLogins());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los logins: " + e.getMessage());
        }
    }

    // Obtener login por ID
    public void getLoginById(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Login> login = loginService.getLoginById(id);

            if (login.isPresent()) {
                ctx.json(login.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Login no encontrado con ID: " + id);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de login inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el login: " + e.getMessage());
        }
    }

    // Obtener login por usuario
    public void getLoginByUsuario(Context ctx) {
        try {
            String usuario = ctx.pathParam("usuario");
            Optional<Login> login = loginService.getLoginByUsuario(usuario);

            if (login.isPresent()) {
                ctx.json(login.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Login no encontrado con usuario: " + usuario);
            }
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el login: " + e.getMessage());
        }
    }

    // Autenticar usuario y generar JWT
    public void autenticar(Context ctx) {
        try {
            AuthRequest authRequest = objectMapper.readValue(ctx.body(), AuthRequest.class);

            if (authRequest.getUsuario() == null || authRequest.getUsuario().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El usuario es requerido");
                return;
            }

            if (authRequest.getContrasena() == null || authRequest.getContrasena().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("La contraseña es requerida");
                return;
            }

            LoginService.AuthResponse authResponse = loginService.autenticar(
                    authRequest.getUsuario(),
                    authRequest.getContrasena()
            );

            // No devolvemos la contraseña por seguridad
            authResponse.getLogin().setContrasena(null);

            AuthResponse response = new AuthResponse(
                    authResponse.getToken(),
                    authResponse.getLogin()
            );

            ctx.json(response);

        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.UNAUTHORIZED)
                    .json("Credenciales inválidas: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error en la autenticación: " + e.getMessage());
        }
    }

    // Actualizar login
    public void updateLogin(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Login login = objectMapper.readValue(ctx.body(), Login.class);

            Login updatedLogin = loginService.updateLogin(id, login);
            ctx.json(updatedLogin);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de login inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al actualizar el login: " + e.getMessage());
        }
    }

    // Cambiar contraseña
    public void cambiarContrasena(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            CambioContrasenaRequest request = objectMapper.readValue(ctx.body(), CambioContrasenaRequest.class);

            if (request.getNuevaContrasena() == null || request.getNuevaContrasena().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("La nueva contraseña es requerida");
                return;
            }

            boolean success = loginService.cambiarContrasena(id, request.getNuevaContrasena());

            if (success) {
                ctx.status(HttpStatus.OK)
                        .json("Contraseña actualizada correctamente");
            } else {
                ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .json("Error al actualizar la contraseña");
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de login inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al cambiar la contraseña: " + e.getMessage());
        }
    }

    // Eliminar login
    public void deleteLogin(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));

            // Verificar si el login existe antes de eliminarlo
            Optional<Login> login = loginService.getLoginById(id);
            if (login.isEmpty()) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Login no encontrado con ID: " + id);
                return;
            }

            loginService.deleteLogin(id);
            ctx.status(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de login inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al eliminar el login: " + e.getMessage());
        }
    }

    // Obtener logins por rol
    public void getLoginsByRol(Context ctx) {
        try {
            Integer idRol = Integer.parseInt(ctx.pathParam("idRol"));
            List<Login> logins = loginService.getLoginsByRol(idRol);
            ctx.json(logins);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de rol inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los logins: " + e.getMessage());
        }
    }

    // Obtener login por propietario
    public void getLoginByPropietario(Context ctx) {
        try {
            Integer idPropietario = Integer.parseInt(ctx.pathParam("idPropietario"));
            Optional<Login> login = loginService.getLoginByPropietario(idPropietario);

            if (login.isPresent()) {
                ctx.json(login.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Login no encontrado para el propietario con ID: " + idPropietario);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de propietario inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el login: " + e.getMessage());
        }
    }

    // Obtener login por inquilino
    public void getLoginByInquilino(Context ctx) {
        try {
            Integer idInquilino = Integer.parseInt(ctx.pathParam("idInquilino"));
            Optional<Login> login = loginService.getLoginByInquilino(idInquilino);

            if (login.isPresent()) {
                ctx.json(login.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Login no encontrado para el inquilino con ID: " + idInquilino);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de inquilino inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el login: " + e.getMessage());
        }
    }

    // Verificar si existe login por usuario
    public void existsByUsuario(Context ctx) {
        try {
            String usuario = ctx.pathParam("usuario");
            boolean exists = loginService.existsByUsuario(usuario);
            ctx.json(new ExistsResponse(exists));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar el login: " + e.getMessage());
        }
    }

    // Clases internas para requests/responses
    private static class AuthRequest {
        private String usuario;
        private String contrasena;

        public String getUsuario() { return usuario; }
        public void setUsuario(String usuario) { this.usuario = usuario; }
        public String getContrasena() { return contrasena; }
        public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    }

    private static class CambioContrasenaRequest {
        private String nuevaContrasena;

        public String getNuevaContrasena() { return nuevaContrasena; }
        public void setNuevaContrasena(String nuevaContrasena) { this.nuevaContrasena = nuevaContrasena; }
    }

    private static class AuthResponse {
        private final String token;
        private final Login login;

        public AuthResponse(String token, Login login) {
            this.token = token;
            this.login = login;
        }

        public String getToken() { return token; }
        public Login getLogin() { return login; }
    }

    private static class ExistsResponse {
        private final boolean exists;

        public ExistsResponse(boolean exists) {
            this.exists = exists;
        }

        public boolean isExists() {
            return exists;
        }
    }
}