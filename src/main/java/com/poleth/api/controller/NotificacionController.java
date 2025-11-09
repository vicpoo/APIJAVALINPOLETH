// NotificacionController.java
package com.poleth.api.controller;

import com.poleth.api.model.Notificacion;
import com.poleth.api.service.NotificacionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class NotificacionController {
    private final NotificacionService notificacionService;
    private final ObjectMapper objectMapper;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
        this.objectMapper = new ObjectMapper();
    }

    // POST: Crear nueva notificación
    public void createNotificacion(Context ctx) {
        try {
            Notificacion notificacion = objectMapper.readValue(ctx.body(), Notificacion.class);

            Notificacion savedNotificacion = notificacionService.createNotificacion(notificacion);
            ctx.status(HttpStatus.CREATED)
                    .json(savedNotificacion);
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al crear la notificación: " + e.getMessage());
        }
    }

    // GET: Obtener todas las notificaciones
    public void getAllNotificaciones(Context ctx) {
        try {
            ctx.json(notificacionService.getAllNotificaciones());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener las notificaciones: " + e.getMessage());
        }
    }

    // GET: Obtener notificación por ID
    public void getNotificacionById(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Notificacion> notificacion = notificacionService.getNotificacionById(id);

            if (notificacion.isPresent()) {
                ctx.json(notificacion.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Notificación no encontrada con ID: " + id);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de notificación inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener la notificación: " + e.getMessage());
        }
    }

    // GET: Obtener notificaciones por inquilino
    public void getNotificacionesByInquilino(Context ctx) {
        try {
            Integer idInquilino = Integer.parseInt(ctx.pathParam("idInquilino"));
            List<Notificacion> notificaciones = notificacionService.getNotificacionesByInquilino(idInquilino);
            ctx.json(notificaciones);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de inquilino inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener las notificaciones: " + e.getMessage());
        }
    }

    // GET: Obtener notificaciones por fecha de utilización
    public void getNotificacionesByFechaUtilizacion(Context ctx) {
        try {
            String fechaStr = ctx.pathParam("fecha");
            Date fecha = Date.valueOf(fechaStr);
            List<Notificacion> notificaciones = notificacionService.getNotificacionesByFechaUtilizacion(fecha);
            ctx.json(notificaciones);
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Formato de fecha inválido. Use YYYY-MM-DD");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener las notificaciones: " + e.getMessage());
        }
    }

    // PUT: Actualizar notificación
    public void updateNotificacion(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Notificacion notificacion = objectMapper.readValue(ctx.body(), Notificacion.class);

            Notificacion updatedNotificacion = notificacionService.updateNotificacion(id, notificacion);
            ctx.json(updatedNotificacion);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de notificación inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al actualizar la notificación: " + e.getMessage());
        }
    }

    // DELETE: Eliminar notificación
    public void deleteNotificacion(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));

            // Verificar si la notificación existe antes de eliminarla
            Optional<Notificacion> notificacion = notificacionService.getNotificacionById(id);
            if (notificacion.isEmpty()) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Notificación no encontrada con ID: " + id);
                return;
            }

            notificacionService.deleteNotificacion(id);
            ctx.status(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de notificación inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al eliminar la notificación: " + e.getMessage());
        }
    }
}