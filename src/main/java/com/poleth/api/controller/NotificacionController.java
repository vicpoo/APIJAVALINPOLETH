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
                    .json(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json(new ErrorResponse("Error interno del servidor: " + e.getMessage()));
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
                    .json(new ErrorResponse("ID de notificación inválido"));
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json(new ErrorResponse("Error al actualizar la notificación: " + e.getMessage()));
        }
    }

    // PATCH: Marcar notificación como leída
    public void marcarComoLeida(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Notificacion notificacion = notificacionService.marcarComoLeida(id);
            ctx.json(notificacion);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json(new ErrorResponse("ID de notificación inválido"));
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.NOT_FOUND)
                    .json(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json(new ErrorResponse("Error al marcar como leída: " + e.getMessage()));
        }
    }

    // GET: Obtener todas las notificaciones
    public void getAllNotificaciones(Context ctx) {
        try {
            List<Notificacion> notificaciones = notificacionService.getAllNotificaciones();
            ctx.json(notificaciones);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json(new ErrorResponse("Error al obtener las notificaciones: " + e.getMessage()));
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
                        .json(new ErrorResponse("Notificación no encontrada con ID: " + id));
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json(new ErrorResponse("ID de notificación inválido"));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json(new ErrorResponse("Error al obtener la notificación: " + e.getMessage()));
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
                    .json(new ErrorResponse("ID de inquilino inválido"));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json(new ErrorResponse("Error al obtener las notificaciones: " + e.getMessage()));
        }
    }

    // GET: Obtener notificaciones por contrato
    public void getNotificacionesByContrato(Context ctx) {
        try {
            Integer idContrato = Integer.parseInt(ctx.pathParam("idContrato"));
            List<Notificacion> notificaciones = notificacionService.getNotificacionesByContrato(idContrato);
            ctx.json(notificaciones);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json(new ErrorResponse("ID de contrato inválido"));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json(new ErrorResponse("Error al obtener las notificaciones: " + e.getMessage()));
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
                    .json(new ErrorResponse("Formato de fecha inválido. Use YYYY-MM-DD"));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json(new ErrorResponse("Error al obtener las notificaciones: " + e.getMessage()));
        }
    }

    // GET: Obtener notificaciones no leídas por inquilino
    public void getNotificacionesNoLeidasByInquilino(Context ctx) {
        try {
            Integer idInquilino = Integer.parseInt(ctx.pathParam("idInquilino"));
            List<Notificacion> notificaciones = notificacionService.getNotificacionesNoLeidasByInquilino(idInquilino);
            ctx.json(notificaciones);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json(new ErrorResponse("ID de inquilino inválido"));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json(new ErrorResponse("Error al obtener las notificaciones no leídas: " + e.getMessage()));
        }
    }

    // GET: Obtener contador de notificaciones no leídas
    public void getContadorNoLeidasByInquilino(Context ctx) {
        try {
            Integer idInquilino = Integer.parseInt(ctx.pathParam("idInquilino"));
            Long count = notificacionService.contarNotificacionesNoLeidasByInquilino(idInquilino);
            ctx.json(new ContadorResponse(count));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json(new ErrorResponse("ID de inquilino inválido"));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json(new ErrorResponse("Error al obtener el contador: " + e.getMessage()));
        }
    }

    // GET: Obtener notificaciones recientes
    public void getNotificacionesRecientes(Context ctx) {
        try {
            String limiteStr = ctx.queryParam("limite");
            int limite = limiteStr != null ? Integer.parseInt(limiteStr) : 10;

            if (limite < 1 || limite > 100) {
                throw new IllegalArgumentException("El límite debe estar entre 1 y 100");
            }

            List<Notificacion> notificaciones = notificacionService.getNotificacionesRecientes(limite);
            ctx.json(notificaciones);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json(new ErrorResponse("Parámetro límite inválido"));
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json(new ErrorResponse("Error al obtener notificaciones recientes: " + e.getMessage()));
        }
    }

    // DELETE: Eliminar notificación
    public void deleteNotificacion(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            boolean deleted = notificacionService.deleteNotificacion(id);

            if (deleted) {
                ctx.status(HttpStatus.NO_CONTENT);
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json(new ErrorResponse("Notificación no encontrada con ID: " + id));
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json(new ErrorResponse("ID de notificación inválido"));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json(new ErrorResponse("Error al eliminar la notificación: " + e.getMessage()));
        }
    }

    // GET: Verificar si existe notificación
    public void existeNotificacion(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            boolean exists = notificacionService.existeNotificacion(id);
            ctx.json(new ExistsResponse(exists));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json(new ErrorResponse("ID de notificación inválido"));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json(new ErrorResponse("Error al verificar existencia: " + e.getMessage()));
        }
    }

    // Clases internas para responses
    private static class ErrorResponse {
        private final String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
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

    private static class ContadorResponse {
        private final Long count;

        public ContadorResponse(Long count) {
            this.count = count;
        }

        public Long getCount() {
            return count;
        }
    }
}