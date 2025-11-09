// InvitadoController.java
package com.poleth.api.controller;

import com.poleth.api.model.Invitado;
import com.poleth.api.service.InvitadoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.List;
import java.util.Optional;

public class InvitadoController {
    private final InvitadoService invitadoService;
    private final ObjectMapper objectMapper;

    public InvitadoController(InvitadoService invitadoService) {
        this.invitadoService = invitadoService;
        this.objectMapper = new ObjectMapper();
    }

    // Crear un nuevo invitado
    public void createInvitado(Context ctx) {
        try {
            Invitado invitado = objectMapper.readValue(ctx.body(), Invitado.class);

            // Usar el método createInvitado que incluye validaciones
            Invitado savedInvitado = invitadoService.createInvitado(invitado);
            ctx.status(HttpStatus.CREATED)
                    .json(savedInvitado);
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al crear el invitado: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al crear el invitado: " + e.getMessage());
        }
    }

    // Obtener todos los invitados
    public void getAllInvitados(Context ctx) {
        try {
            List<Invitado> invitados = invitadoService.getAllInvitados();
            ctx.json(invitados);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los invitados: " + e.getMessage());
        }
    }

    // Obtener invitado por ID
    public void getInvitadoById(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Invitado> invitado = invitadoService.getInvitadoById(id);

            if (invitado.isPresent()) {
                ctx.json(invitado.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Invitado no encontrado con ID: " + id);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de invitado inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el invitado: " + e.getMessage());
        }
    }

    // Obtener invitados por cuarto
    public void getInvitadosByCuarto(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("idCuarto"));
            List<Invitado> invitados = invitadoService.getInvitadosByCuarto(idCuarto);
            ctx.json(invitados);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener invitados del cuarto: " + e.getMessage());
        }
    }

    // Actualizar invitado
    public void updateInvitado(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Invitado invitadoActualizado = objectMapper.readValue(ctx.body(), Invitado.class);

            // Usar el método updateInvitado que incluye validaciones
            Invitado updatedInvitado = invitadoService.updateInvitado(id, invitadoActualizado);
            ctx.json(updatedInvitado);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de invitado inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al actualizar el invitado: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al actualizar el invitado: " + e.getMessage());
        }
    }

    // Eliminar invitado
    public void deleteInvitado(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));

            // Verificar si el invitado existe antes de eliminar
            if (!invitadoService.existsById(id)) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Invitado no encontrado con ID: " + id);
                return;
            }

            invitadoService.deleteInvitado(id);
            ctx.status(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de invitado inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al eliminar el invitado: " + e.getMessage());
        }
    }
}