// MantenimientoController.java
package com.poleth.api.controller;

import com.poleth.api.model.Mantenimiento;
import com.poleth.api.service.MantenimientoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MantenimientoController {
    private final MantenimientoService mantenimientoService;
    private final ObjectMapper objectMapper;

    public MantenimientoController(MantenimientoService mantenimientoService) {
        this.mantenimientoService = mantenimientoService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule()); // Registrar módulo Java Time
    }

    public void createMantenimiento(Context ctx) {
        try {
            Mantenimiento mantenimiento = objectMapper.readValue(ctx.body(), Mantenimiento.class);

            // Usar el método createMantenimiento que incluye validaciones
            Mantenimiento savedMantenimiento = mantenimientoService.createMantenimiento(mantenimiento);
            ctx.status(HttpStatus.CREATED)
                    .json(savedMantenimiento);
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al crear el mantenimiento: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al crear el mantenimiento: " + e.getMessage());
        }
    }

    public void getAllMantenimientos(Context ctx) {
        try {
            List<Mantenimiento> mantenimientos = mantenimientoService.getAllMantenimientos();
            ctx.json(mantenimientos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los mantenimientos: " + e.getMessage());
        }
    }

    public void getMantenimientoById(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Mantenimiento> mantenimiento = mantenimientoService.getMantenimientoById(id);

            if (mantenimiento.isPresent()) {
                ctx.json(mantenimiento.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Mantenimiento no encontrado con ID: " + id);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de mantenimiento inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el mantenimiento: " + e.getMessage());
        }
    }

    public void updateMantenimiento(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Mantenimiento mantenimientoActualizado = objectMapper.readValue(ctx.body(), Mantenimiento.class);

            // Usar el método updateMantenimiento que incluye validaciones
            Mantenimiento updatedMantenimiento = mantenimientoService.updateMantenimiento(id, mantenimientoActualizado);
            ctx.json(updatedMantenimiento);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de mantenimiento inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al actualizar el mantenimiento: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al actualizar el mantenimiento: " + e.getMessage());
        }
    }

    public void deleteMantenimiento(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            
            // Verificar si el mantenimiento existe antes de eliminar
            if (!mantenimientoService.existsById(id)) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Mantenimiento no encontrado con ID: " + id);
                return;
            }
            
            mantenimientoService.deleteMantenimiento(id);
            ctx.status(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de mantenimiento inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al eliminar el mantenimiento: " + e.getMessage());
        }
    }

    // Métodos específicos para Mantenimiento

    public void getMantenimientosByCuarto(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("idCuarto"));
            List<Mantenimiento> mantenimientos = mantenimientoService.getMantenimientosByCuarto(idCuarto);
            ctx.json(mantenimientos);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener mantenimientos del cuarto: " + e.getMessage());
        }
    }

    public void getMantenimientosByEstado(Context ctx) {
        try {
            String estado = ctx.queryParam("estado");
            if (estado == null || estado.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro estado es requerido");
                return;
            }

            List<Mantenimiento> mantenimientos = mantenimientoService.getMantenimientosByEstado(estado);
            ctx.json(mantenimientos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar mantenimientos por estado: " + e.getMessage());
        }
    }

    public void getMantenimientosPendientes(Context ctx) {
        try {
            List<Mantenimiento> mantenimientos = mantenimientoService.getMantenimientosPendientes();
            ctx.json(mantenimientos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener mantenimientos pendientes: " + e.getMessage());
        }
    }

    public void getMantenimientosCompletados(Context ctx) {
        try {
            List<Mantenimiento> mantenimientos = mantenimientoService.getMantenimientosCompletados();
            ctx.json(mantenimientos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener mantenimientos completados: " + e.getMessage());
        }
    }

    public void getMantenimientosByFechaReporte(Context ctx) {
        try {
            String fechaInicioStr = ctx.queryParam("fechaInicio");
            String fechaFinStr = ctx.queryParam("fechaFin");
            
            if (fechaInicioStr == null || fechaFinStr == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("Los parámetros fechaInicio y fechaFin son requeridos");
                return;
            }

            LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
            LocalDate fechaFin = LocalDate.parse(fechaFinStr);

            List<Mantenimiento> mantenimientos = mantenimientoService.getMantenimientosByFechaReporteBetween(fechaInicio, fechaFin);
            ctx.json(mantenimientos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar mantenimientos por fecha de reporte: " + e.getMessage());
        }
    }

    public void getMantenimientosByDescripcion(Context ctx) {
        try {
            String texto = ctx.queryParam("texto");
            if (texto == null || texto.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro texto es requerido");
                return;
            }

            List<Mantenimiento> mantenimientos = mantenimientoService.getMantenimientosByDescripcionContaining(texto);
            ctx.json(mantenimientos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar mantenimientos por descripción: " + e.getMessage());
        }
    }

    public void getMantenimientosPendientesByCuarto(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("idCuarto"));
            List<Mantenimiento> mantenimientos = mantenimientoService.getMantenimientosPendientesByCuarto(idCuarto);
            ctx.json(mantenimientos);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener mantenimientos pendientes del cuarto: " + e.getMessage());
        }
    }

    public void getMantenimientosRecientes(Context ctx) {
        try {
            List<Mantenimiento> mantenimientos = mantenimientoService.getMantenimientosRecientes();
            ctx.json(mantenimientos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener mantenimientos recientes: " + e.getMessage());
        }
    }

    public void atenderMantenimiento(Context ctx) {
        try {
            Integer idMantenimiento = Integer.parseInt(ctx.pathParam("id"));
            String body = ctx.body();
            
            // Extraer datos del cuerpo JSON
            LocalDate fechaAtencion = LocalDate.parse(objectMapper.readTree(body).get("fechaAtencion").asText());
            BigDecimal costo = new BigDecimal(objectMapper.readTree(body).get("costo").asText());
            String estado = objectMapper.readTree(body).get("estado").asText();

            Mantenimiento mantenimiento = mantenimientoService.atenderMantenimiento(idMantenimiento, fechaAtencion, costo, estado);
            ctx.json(mantenimiento);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de mantenimiento inválido o formato de costo incorrecto");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al atender mantenimiento: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al atender mantenimiento: " + e.getMessage());
        }
    }

    public void actualizarEstadoMantenimiento(Context ctx) {
        try {
            Integer idMantenimiento = Integer.parseInt(ctx.pathParam("id"));
            String body = ctx.body();
            
            // Extraer nuevoEstado del cuerpo JSON
            String nuevoEstado = objectMapper.readTree(body).get("estado").asText();

            Mantenimiento mantenimiento = mantenimientoService.actualizarEstadoMantenimiento(idMantenimiento, nuevoEstado);
            ctx.json(mantenimiento);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de mantenimiento inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al actualizar estado: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al actualizar estado: " + e.getMessage());
        }
    }

    public void actualizarCostoMantenimiento(Context ctx) {
        try {
            Integer idMantenimiento = Integer.parseInt(ctx.pathParam("id"));
            String body = ctx.body();
            
            // Extraer nuevoCosto del cuerpo JSON
            BigDecimal nuevoCosto = new BigDecimal(objectMapper.readTree(body).get("costo").asText());

            Mantenimiento mantenimiento = mantenimientoService.actualizarCostoMantenimiento(idMantenimiento, nuevoCosto);
            ctx.json(mantenimiento);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de mantenimiento inválido o formato de costo incorrecto");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al actualizar costo: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al actualizar costo: " + e.getMessage());
        }
    }

    public void countMantenimientos(Context ctx) {
        try {
            Long count = mantenimientoService.countMantenimientos();
            ctx.json(count);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar mantenimientos: " + e.getMessage());
        }
    }

    public void countMantenimientosPendientes(Context ctx) {
        try {
            Long count = mantenimientoService.countMantenimientosPendientes();
            ctx.json(count);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar mantenimientos pendientes: " + e.getMessage());
        }
    }

    public void countMantenimientosCompletados(Context ctx) {
        try {
            Long count = mantenimientoService.countMantenimientosCompletados();
            ctx.json(count);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar mantenimientos completados: " + e.getMessage());
        }
    }

    public void existsPendientesByCuarto(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("idCuarto"));
            boolean exists = mantenimientoService.existsPendientesByCuarto(idCuarto);
            ctx.json(exists);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar existencia de mantenimientos pendientes: " + e.getMessage());
        }
    }

    public void obtenerEstadisticas(Context ctx) {
        try {
            String estadisticas = mantenimientoService.obtenerEstadisticasMantenimiento();
            ctx.json(estadisticas);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener estadísticas: " + e.getMessage());
        }
    }
}