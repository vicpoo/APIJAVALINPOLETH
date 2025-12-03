//UsuarioController.java
package com.poleth.api.controller;

import com.poleth.api.model.Usuario;
import com.poleth.api.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.List;
import java.util.Optional;

public class UsuarioController {
    private final UsuarioService usuarioService;
    private final ObjectMapper objectMapper;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        this.objectMapper = new ObjectMapper();
    }

    // Método para login
    public void login(Context ctx) {
        try {
            LoginRequest loginRequest = objectMapper.readValue(ctx.body(), LoginRequest.class);

            // Validaciones básicas
            if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El username es requerido");
                return;
            }

            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("La contraseña es requerida");
                return;
            }

            String token = usuarioService.login(loginRequest.getUsername(), loginRequest.getPassword());

            LoginResponse response = new LoginResponse(
                    true,
                    "Login exitoso",
                    token,
                    loginRequest.getUsername()
            );

            ctx.status(HttpStatus.OK)
                    .json(response);

        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.UNAUTHORIZED)
                    .json(new LoginResponse(false, e.getMessage(), null, null));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json(new LoginResponse(false, "Error en el servidor: " + e.getMessage(), null, null));
        }
    }

    // Crear un nuevo usuario
    public void createUsuario(Context ctx) {
        try {
            Usuario usuario = objectMapper.readValue(ctx.body(), Usuario.class);

            // Validaciones básicas
            if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El username es requerido");
                return;
            }

            if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("La contraseña es requerida");
                return;
            }

            if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El email es requerido");
                return;
            }

            if (usuario.getRol() == null || usuario.getRol().getIdRoles() == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El rol es requerido");
                return;
            }

            Usuario savedUsuario = usuarioService.createUsuario(usuario);
            ctx.status(HttpStatus.CREATED)
                    .json(savedUsuario);
        } catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al crear el usuario: " + e.getMessage());
        }
    }

    // Obtener todos los usuarios
    public void getAllUsuarios(Context ctx) {
        try {
            ctx.json(usuarioService.getAllUsuarios());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los usuarios: " + e.getMessage());
        }
    }

    // Obtener usuario por ID
    public void getUsuarioById(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Usuario> usuario = usuarioService.getUsuarioById(id);

            if (usuario.isPresent()) {
                ctx.json(usuario.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Usuario no encontrado con ID: " + id);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de usuario inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el usuario: " + e.getMessage());
        }
    }

    // Obtener usuario por username
    public void getUsuarioByUsername(Context ctx) {
        try {
            String username = ctx.pathParam("username");
            Optional<Usuario> usuario = usuarioService.getUsuarioByUsername(username);

            if (usuario.isPresent()) {
                ctx.json(usuario.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Usuario no encontrado: " + username);
            }
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el usuario: " + e.getMessage());
        }
    }

    // Obtener usuario por email
    public void getUsuarioByEmail(Context ctx) {
        try {
            String email = ctx.pathParam("email");
            Optional<Usuario> usuario = usuarioService.getUsuarioByEmail(email);

            if (usuario.isPresent()) {
                ctx.json(usuario.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Usuario no encontrado con email: " + email);
            }
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el usuario: " + e.getMessage());
        }
    }

    // Actualizar usuario
    public void updateUsuario(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Usuario usuario = objectMapper.readValue(ctx.body(), Usuario.class);

            Usuario updatedUsuario = usuarioService.updateUsuario(id, usuario);
            ctx.json(updatedUsuario);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de usuario inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al actualizar el usuario: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al actualizar el usuario: " + e.getMessage());
        }
    }

    // Eliminar usuario
    public void deleteUsuario(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));

            // Verificar si el usuario existe antes de eliminarlo
            Optional<Usuario> usuario = usuarioService.getUsuarioById(id);
            if (usuario.isEmpty()) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Usuario no encontrado con ID: " + id);
                return;
            }

            usuarioService.deleteUsuario(id);
            ctx.status(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de usuario inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al eliminar el usuario: " + e.getMessage());
        }
    }

    // Verificar si existe usuario por username
    public void existsByUsername(Context ctx) {
        try {
            String username = ctx.pathParam("username");
            boolean exists = usuarioService.existsByUsername(username);
            ctx.json(new ExistsResponse(exists));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar el usuario: " + e.getMessage());
        }
    }

    // Verificar si existe usuario por email
    public void existsByEmail(Context ctx) {
        try {
            String email = ctx.pathParam("email");
            boolean exists = usuarioService.existsByEmail(email);
            ctx.json(new ExistsResponse(exists));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar el usuario: " + e.getMessage());
        }
    }

    // Cambiar estado de usuario
    public void cambiarEstadoUsuario(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            CambioEstadoRequest request = objectMapper.readValue(ctx.body(), CambioEstadoRequest.class);

            Usuario usuarioActualizado = usuarioService.cambiarEstadoUsuario(id, request.getEstado());
            ctx.json(usuarioActualizado);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de usuario inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al cambiar el estado: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al cambiar el estado: " + e.getMessage());
        }
    }

    // Obtener usuarios por estado
    public void getUsuariosByEstado(Context ctx) {
        try {
            String estado = ctx.pathParam("estado");
            List<Usuario> usuarios = usuarioService.getUsuariosByEstado(estado);
            ctx.json(usuarios);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los usuarios: " + e.getMessage());
        }
    }

    // Obtener usuarios por rol
    public void getUsuariosByRol(Context ctx) {
        try {
            Integer rolId = Integer.parseInt(ctx.pathParam("rolId"));
            List<Usuario> usuarios = usuarioService.getUsuariosByRol(rolId);
            ctx.json(usuarios);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de rol inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los usuarios: " + e.getMessage());
        }
    }

    // Clases internas para requests/responses
    private static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    private static class LoginResponse {
        private boolean success;
        private String message;
        private String token;
        private String username;

        public LoginResponse(boolean success, String message, String token, String username) {
            this.success = success;
            this.message = message;
            this.token = token;
            this.username = username;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getToken() { return token; }
        public String getUsername() { return username; }
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

    private static class CambioEstadoRequest {
        private String estado;

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }
    }
}