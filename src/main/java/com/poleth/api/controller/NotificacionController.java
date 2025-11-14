// NotificacionController.java
package com.poleth.api.controller;

import com.poleth.api.model.Notificacion;
import com.poleth.api.model.Inquilino;
import com.poleth.api.service.NotificacionService;
import com.poleth.api.service.InquilinoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class NotificacionController {
    private final NotificacionService notificacionService;
    private final InquilinoService inquilinoService;
    private final ObjectMapper objectMapper;

    public NotificacionController(NotificacionService notificacionService, InquilinoService inquilinoService) {
        this.notificacionService = notificacionService;
        this.inquilinoService = inquilinoService;
        this.objectMapper = new ObjectMapper();
    }

    // POST: Crear nueva notificación - CORREGIDO
    public void createNotificacion(Context ctx) {
        try {
            JsonNode jsonNode = objectMapper.readTree(ctx.body());
            Notificacion notificacion = new Notificacion();

            // Establecer campos básicos
            if (jsonNode.has("tipoNotificacion")) {
                notificacion.setTipoNotificacion(jsonNode.get("tipoNotificacion").asText());
            }

            if (jsonNode.has("fechaUtilizacion")) {
                notificacion.setFechaUtilizacion(Date.valueOf(jsonNode.get("fechaUtilizacion").asText()));
            }

            if (jsonNode.has("idContrato") && !jsonNode.get("idContrato").isNull()) {
                notificacion.setIdContrato(jsonNode.get("idContrato").asInt());
            }

            // Manejar el inquilino CORRECTAMENTE
            if (jsonNode.has("inquilino") && !jsonNode.get("inquilino").isNull()) {
                JsonNode inquilinoNode = jsonNode.get("inquilino");
                if (inquilinoNode.has("idInquilino")) {
                    Integer idInquilino = inquilinoNode.get("idInquilino").asInt();
                    // Buscar el inquilino completo en la base de datos
                    Optional<Inquilino> inquilinoOpt = inquilinoService.getInquilinoById(idInquilino);
                    if (inquilinoOpt.isPresent()) {
                        notificacion.setInquilino(inquilinoOpt.get());
                    } else {
                        ctx.status(HttpStatus.BAD_REQUEST)
                                .json("Inquilino no encontrado con ID: " + idInquilino);
                        return;
                    }
                }
            }

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

    // PUT: Actualizar notificación - CORREGIDO
    public void updateNotificacion(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            JsonNode jsonNode = objectMapper.readTree(ctx.body());

            // Buscar la notificación existente
            Optional<Notificacion> notificacionExistenteOpt = notificacionService.getNotificacionById(id);
            if (notificacionExistenteOpt.isEmpty()) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Notificación no encontrada con ID: " + id);
                return;
            }

            Notificacion notificacionExistente = notificacionExistenteOpt.get();

            // Actualizar campos básicos
            if (jsonNode.has("tipoNotificacion")) {
                notificacionExistente.setTipoNotificacion(jsonNode.get("tipoNotificacion").asText());
            }

            if (jsonNode.has("fechaUtilizacion")) {
                notificacionExistente.setFechaUtilizacion(Date.valueOf(jsonNode.get("fechaUtilizacion").asText()));
            }

            if (jsonNode.has("idContrato")) {
                if (jsonNode.get("idContrato").isNull()) {
                    notificacionExistente.setIdContrato(null);
                } else {
                    notificacionExistente.setIdContrato(jsonNode.get("idContrato").asInt());
                }
            }

            // Manejar el inquilino CORRECTAMENTE
            if (jsonNode.has("inquilino")) {
                if (jsonNode.get("inquilino").isNull()) {
                    notificacionExistente.setInquilino(null);
                } else {
                    JsonNode inquilinoNode = jsonNode.get("inquilino");
                    if (inquilinoNode.has("idInquilino")) {
                        Integer idInquilino = inquilinoNode.get("idInquilino").asInt();
                        // Buscar el inquilino completo en la base de datos
                        Optional<Inquilino> inquilinoOpt = inquilinoService.getInquilinoById(idInquilino);
                        if (inquilinoOpt.isPresent()) {
                            notificacionExistente.setInquilino(inquilinoOpt.get());
                        } else {
                            ctx.status(HttpStatus.BAD_REQUEST)
                                    .json("Inquilino no encontrado con ID: " + idInquilino);
                            return;
                        }
                    }
                }
            }

            Notificacion updatedNotificacion = notificacionService.updateNotificacion(id, notificacionExistente);
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