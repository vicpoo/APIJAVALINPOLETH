// RolController.java
package com.poleth.api.controller;

import com.poleth.api.model.Rol;
import com.poleth.api.service.RolService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.Optional;

public class RolController {
    private final RolService rolService;
    private final ObjectMapper objectMapper;

    public RolController(RolService rolService) {
        this.rolService = rolService;
        this.objectMapper = new ObjectMapper();
    }

    // Crear un nuevo rol
    public void createRol(Context ctx) {
        try {
            Rol rol = objectMapper.readValue(ctx.body(), Rol.class);

            // Validar que el título no esté vacío
            if (rol.getTitulo() == null || rol.getTitulo().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El título del rol es requerido");
                return;
            }

            // Verificar si el rol ya existe
            if (rolService.existsByTitulo(rol.getTitulo())) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El rol '" + rol.getTitulo() + "' ya existe");
                return;
            }

            Rol savedRol = rolService.createRol(rol);
            ctx.status(HttpStatus.CREATED)
                    .json(savedRol);
        } catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al crear el rol: " + e.getMessage());
        }
    }

    // Obtener todos los roles
    public void getAllRoles(Context ctx) {
        try {
            ctx.json(rolService.getAllRoles());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los roles: " + e.getMessage());
        }
    }

    // Obtener rol por ID
    public void getRolById(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Rol> rol = rolService.getRolById(id);

            if (rol.isPresent()) {
                ctx.json(rol.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Rol no encontrado con ID: " + id);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de rol inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el rol: " + e.getMessage());
        }
    }

    // Obtener rol por título
    public void getRolByTitulo(Context ctx) {
        try {
            String titulo = ctx.pathParam("titulo");
            Optional<Rol> rol = rolService.getRolByTitulo(titulo);

            if (rol.isPresent()) {
                ctx.json(rol.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Rol no encontrado: " + titulo);
            }
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el rol: " + e.getMessage());
        }
    }

    // Actualizar rol
    public void updateRol(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Rol rol = objectMapper.readValue(ctx.body(), Rol.class);

            Rol updatedRol = rolService.updateRol(id, rol);
            ctx.json(updatedRol);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de rol inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al actualizar el rol: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al actualizar el rol: " + e.getMessage());
        }
    }

    // Eliminar rol
    public void deleteRol(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));

            // Verificar si el rol existe antes de eliminarlo
            Optional<Rol> rol = rolService.getRolById(id);
            if (rol.isEmpty()) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Rol no encontrado con ID: " + id);
                return;
            }

            rolService.deleteRol(id);
            ctx.status(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de rol inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al eliminar el rol: " + e.getMessage());
        }
    }

    // Verificar si existe rol por título
    public void existsByTitulo(Context ctx) {
        try {
            String titulo = ctx.pathParam("titulo");
            boolean exists = rolService.existsByTitulo(titulo);
            ctx.json(new ExistsResponse(exists));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar el rol: " + e.getMessage());
        }
    }

    // Clase interna para respuesta JSON
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