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

    // Crear una nueva notificación
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

    // Obtener todas las notificaciones
    public void getAllNotificaciones(Context ctx) {
        try {
            ctx.json(notificacionService.getAllNotificaciones());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener las notificaciones: " + e.getMessage());
        }
    }

    // Obtener notificación por ID
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

    // Obtener notificaciones por inquilino
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

    // Obtener notificaciones por contrato
    public void getNotificacionesByContrato(Context ctx) {
        try {
            Integer idContrato = Integer.parseInt(ctx.pathParam("idContrato"));
            List<Notificacion> notificaciones = notificacionService.getNotificacionesByContrato(idContrato);
            ctx.json(notificaciones);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de contrato inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener las notificaciones: " + e.getMessage());
        }
    }

    // Obtener notificaciones por tipo
    public void getNotificacionesByTipo(Context ctx) {
        try {
            String tipo = ctx.pathParam("tipo");
            List<Notificacion> notificaciones = notificacionService.getNotificacionesByTipo(tipo);
            ctx.json(notificaciones);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener las notificaciones: " + e.getMessage());
        }
    }

    // Obtener notificaciones por fecha de utilización
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

    // Obtener notificaciones por rango de fechas de utilización
    public void getNotificacionesByRangoFechasUtilizacion(Context ctx) {
        try {
            String fechaInicioStr = ctx.queryParam("fechaInicio");
            String fechaFinStr = ctx.queryParam("fechaFin");
            
            if (fechaInicioStr == null || fechaFinStr == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("Los parámetros 'fechaInicio' y 'fechaFin' son requeridos");
                return;
            }

            Date fechaInicio = Date.valueOf(fechaInicioStr);
            Date fechaFin = Date.valueOf(fechaFinStr);
            
            List<Notificacion> notificaciones = notificacionService.getNotificacionesByRangoFechasUtilizacion(fechaInicio, fechaFin);
            ctx.json(notificaciones);
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Formato de fecha inválido. Use YYYY-MM-DD");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener las notificaciones: " + e.getMessage());
        }
    }

    // Obtener notificaciones con fecha de utilización pasada
    public void getNotificacionesWithFechaUtilizacionPasada(Context ctx) {
        try {
            List<Notificacion> notificaciones = notificacionService.getNotificacionesWithFechaUtilizacionPasada();
            ctx.json(notificaciones);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener las notificaciones: " + e.getMessage());
        }
    }

    // Obtener notificaciones con fecha de utilización futura
    public void getNotificacionesWithFechaUtilizacionFutura(Context ctx) {
        try {
            List<Notificacion> notificaciones = notificacionService.getNotificacionesWithFechaUtilizacionFutura();
            ctx.json(notificaciones);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener las notificaciones: " + e.getMessage());
        }
    }

    // Obtener notificaciones con fecha de utilización hoy
    public void getNotificacionesWithFechaUtilizacionHoy(Context ctx) {
        try {
            List<Notificacion> notificaciones = notificacionService.getNotificacionesWithFechaUtilizacionHoy();
            ctx.json(notificaciones);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener las notificaciones: " + e.getMessage());
        }
    }

    // Obtener notificaciones sin inquilino
    public void getNotificacionesWithoutInquilino(Context ctx) {
        try {
            List<Notificacion> notificaciones = notificacionService.getNotificacionesWithoutInquilino();
            ctx.json(notificaciones);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener las notificaciones: " + e.getMessage());
        }
    }

    // Obtener notificaciones sin contrato
    public void getNotificacionesWithoutContrato(Context ctx) {
        try {
            List<Notificacion> notificaciones = notificacionService.getNotificacionesWithoutContrato();
            ctx.json(notificaciones);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener las notificaciones: " + e.getMessage());
        }
    }

    // Obtener notificaciones por inquilino y tipo
    public void getNotificacionesByInquilinoAndTipo(Context ctx) {
        try {
            Integer idInquilino = Integer.parseInt(ctx.pathParam("idInquilino"));
            String tipo = ctx.pathParam("tipo");
            
            List<Notificacion> notificaciones = notificacionService.getNotificacionesByInquilinoAndTipo(idInquilino, tipo);
            ctx.json(notificaciones);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de inquilino inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener las notificaciones: " + e.getMessage());
        }
    }

    // Obtener notificaciones por contrato y tipo
    public void getNotificacionesByContratoAndTipo(Context ctx) {
        try {
            Integer idContrato = Integer.parseInt(ctx.pathParam("idContrato"));
            String tipo = ctx.pathParam("tipo");
            
            List<Notificacion> notificaciones = notificacionService.getNotificacionesByContratoAndTipo(idContrato, tipo);
            ctx.json(notificaciones);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de contrato inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener las notificaciones: " + e.getMessage());
        }
    }

    // Actualizar notificación
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

    // Eliminar notificación
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

    // Actualizar tipo de notificación
    public void updateTipoNotificacion(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            String body = ctx.body();
            String tipo = extractJsonValue(body, "tipo");
            
            if (tipo == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo 'tipo' es requerido");
                return;
            }

            boolean success = notificacionService.updateTipoNotificacion(id, tipo);

            if (success) {
                ctx.status(HttpStatus.OK)
                        .json("Tipo de notificación actualizado correctamente");
            } else {
                ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .json("Error al actualizar el tipo de notificación");
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de notificación inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al actualizar el tipo de notificación: " + e.getMessage());
        }
    }

    // Crear notificación rápida para inquilino
    public void crearNotificacionParaInquilino(Context ctx) {
        try {
            String body = ctx.body();
            String idInquilinoStr = extractJsonValue(body, "idInquilino");
            String tipo = extractJsonValue(body, "tipo");
            String fechaStr = extractJsonValue(body, "fechaUtilizacion");
            
            if (idInquilinoStr == null || tipo == null || fechaStr == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("Los campos 'idInquilino', 'tipo' y 'fechaUtilizacion' son requeridos");
                return;
            }

            Integer idInquilino = Integer.parseInt(idInquilinoStr);
            Date fechaUtilizacion = Date.valueOf(fechaStr);

            Notificacion notificacion = notificacionService.crearNotificacionParaInquilino(idInquilino, tipo, fechaUtilizacion);
            ctx.status(HttpStatus.CREATED)
                    .json(notificacion);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de inquilino inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Formato de fecha inválido. Use YYYY-MM-DD");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al crear la notificación: " + e.getMessage());
        }
    }

    // Crear notificación rápida para contrato
    public void crearNotificacionParaContrato(Context ctx) {
        try {
            String body = ctx.body();
            String idContratoStr = extractJsonValue(body, "idContrato");
            String tipo = extractJsonValue(body, "tipo");
            String fechaStr = extractJsonValue(body, "fechaUtilizacion");
            
            if (idContratoStr == null || tipo == null || fechaStr == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("Los campos 'idContrato', 'tipo' y 'fechaUtilizacion' son requeridos");
                return;
            }

            Integer idContrato = Integer.parseInt(idContratoStr);
            Date fechaUtilizacion = Date.valueOf(fechaStr);

            Notificacion notificacion = notificacionService.crearNotificacionParaContrato(idContrato, tipo, fechaUtilizacion);
            ctx.status(HttpStatus.CREATED)
                    .json(notificacion);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de contrato inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Formato de fecha inválido. Use YYYY-MM-DD");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al crear la notificación: " + e.getMessage());
        }
    }

    // Eliminar todas las notificaciones de un inquilino
    public void deleteNotificacionesByInquilino(Context ctx) {
        try {
            Integer idInquilino = Integer.parseInt(ctx.pathParam("idInquilino"));
            int deletedCount = notificacionService.deleteNotificacionesByInquilino(idInquilino);
            ctx.json(new DeleteResponse(deletedCount, "Notificaciones eliminadas: " + deletedCount));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de inquilino inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al eliminar las notificaciones: " + e.getMessage());
        }
    }

    // Eliminar todas las notificaciones de un contrato
    public void deleteNotificacionesByContrato(Context ctx) {
        try {
            Integer idContrato = Integer.parseInt(ctx.pathParam("idContrato"));
            int deletedCount = notificacionService.deleteNotificacionesByContrato(idContrato);
            ctx.json(new DeleteResponse(deletedCount, "Notificaciones eliminadas: " + deletedCount));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de contrato inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al eliminar las notificaciones: " + e.getMessage());
        }
    }

    // Obtener notificaciones más recientes
public void getNotificacionesMasRecientes(Context ctx) {
    try {
        String limiteStr = ctx.queryParam("limite");
        if (limiteStr == null) {
            limiteStr = "10";
        }
        int limite = Integer.parseInt(limiteStr);
        
        List<Notificacion> notificaciones = notificacionService.getNotificacionesMasRecientes(limite);
        ctx.json(notificaciones);
    } catch (NumberFormatException e) {
        ctx.status(HttpStatus.BAD_REQUEST)
                .json("Límite inválido");
    } catch (Exception e) {
        ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .json("Error al obtener las notificaciones: " + e.getMessage());
    }
}


    // Obtener estadísticas de notificaciones
    public void getStats(Context ctx) {
        try {
            NotificacionService.NotificacionStats stats = notificacionService.getStats();
            ctx.json(stats);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener estadísticas: " + e.getMessage());
        }
    }

    // Obtener estadísticas por inquilino
    public void getStatsByInquilino(Context ctx) {
        try {
            Integer idInquilino = Integer.parseInt(ctx.pathParam("idInquilino"));
            NotificacionService.NotificacionStats stats = notificacionService.getStatsByInquilino(idInquilino);
            ctx.json(stats);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de inquilino inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener estadísticas: " + e.getMessage());
        }
    }

    // Contar notificaciones
    public void countNotificaciones(Context ctx) {
        try {
            Long count = notificacionService.countNotificaciones();
            ctx.json(new CountResponse(count));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar las notificaciones: " + e.getMessage());
        }
    }

    // Contar notificaciones por inquilino
    public void countNotificacionesByInquilino(Context ctx) {
        try {
            Integer idInquilino = Integer.parseInt(ctx.pathParam("idInquilino"));
            Long count = notificacionService.countByInquilino(idInquilino);
            ctx.json(new CountResponse(count));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de inquilino inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar las notificaciones: " + e.getMessage());
        }
    }

    // Contar notificaciones por contrato
    public void countNotificacionesByContrato(Context ctx) {
        try {
            Integer idContrato = Integer.parseInt(ctx.pathParam("idContrato"));
            Long count = notificacionService.countByContrato(idContrato);
            ctx.json(new CountResponse(count));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de contrato inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar las notificaciones: " + e.getMessage());
        }
    }

    // Contar notificaciones por tipo
    public void countNotificacionesByTipo(Context ctx) {
        try {
            String tipo = ctx.pathParam("tipo");
            Long count = notificacionService.countByTipo(tipo);
            ctx.json(new CountResponse(count));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar las notificaciones: " + e.getMessage());
        }
    }

    // Verificar si existe notificación por ID
    public void existsById(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            boolean exists = notificacionService.existsById(id);
            ctx.json(new ExistsResponse(exists));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de notificación inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar la notificación: " + e.getMessage());
        }
    }

    // Verificar si existen notificaciones por inquilino
    public void existsByInquilino(Context ctx) {
        try {
            Integer idInquilino = Integer.parseInt(ctx.pathParam("idInquilino"));
            boolean exists = notificacionService.existsByInquilino(idInquilino);
            ctx.json(new ExistsResponse(exists));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de inquilino inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar las notificaciones: " + e.getMessage());
        }
    }

    // Verificar si existen notificaciones por contrato
    public void existsByContrato(Context ctx) {
        try {
            Integer idContrato = Integer.parseInt(ctx.pathParam("idContrato"));
            boolean exists = notificacionService.existsByContrato(idContrato);
            ctx.json(new ExistsResponse(exists));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de contrato inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar las notificaciones: " + e.getMessage());
        }
    }

    // Verificar si existen notificaciones por tipo
    public void existsByTipo(Context ctx) {
        try {
            String tipo = ctx.pathParam("tipo");
            boolean exists = notificacionService.existsByTipo(tipo);
            ctx.json(new ExistsResponse(exists));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar las notificaciones: " + e.getMessage());
        }
    }

    // Método auxiliar para extraer valores del JSON
    private String extractJsonValue(String json, String key) {
        try {
            String[] parts = json.split("\"" + key + "\"");
            if (parts.length > 1) {
                String valuePart = parts[1].split("\"")[1];
                return valuePart;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    // Clases internas para respuestas JSON
    private static class CountResponse {
        private final Long count;
        
        public CountResponse(Long count) {
            this.count = count;
        }
        
        public Long getCount() {
            return count;
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

    private static class DeleteResponse {
        private final int deletedCount;
        private final String message;
        
        public DeleteResponse(int deletedCount, String message) {
            this.deletedCount = deletedCount;
            this.message = message;
        }
        
        public int getDeletedCount() {
            return deletedCount;
        }
        
        public String getMessage() {
            return message;
        }
    }
}