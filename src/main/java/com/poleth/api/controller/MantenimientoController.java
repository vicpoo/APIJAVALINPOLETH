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
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    // POST: Crear nuevo mantenimiento
    public void createMantenimiento(Context ctx) {
        try {
            // Leer JSON y mapear a objeto
            String requestBody = ctx.body();
            Mantenimiento mantenimiento = objectMapper.readValue(requestBody, Mantenimiento.class);

            // Validar campos requeridos
            if (mantenimiento.getIdCuarto() == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo idCuarto es requerido");
                return;
            }

            if (mantenimiento.getFechaReporte() == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo fechaReporte es requerido");
                return;
            }

            if (mantenimiento.getDescripcionProblema() == null ||
                    mantenimiento.getDescripcionProblema().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo descripcionProblema es requerido");
                return;
            }

            // Estado por defecto si no se especifica
            if (mantenimiento.getEstadoMantenimiento() == null ||
                    mantenimiento.getEstadoMantenimiento().trim().isEmpty()) {
                mantenimiento.setEstadoMantenimiento("pendiente");
            }

            // Costo por defecto si no se especifica
            if (mantenimiento.getCostoMantenimiento() == null) {
                mantenimiento.setCostoMantenimiento(BigDecimal.ZERO);
            }

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

    // GET: Obtener todos los mantenimientos
    public void getAllMantenimientos(Context ctx) {
        try {
            List<Mantenimiento> mantenimientos = mantenimientoService.getAllMantenimientos();
            ctx.json(mantenimientos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los mantenimientos: " + e.getMessage());
        }
    }

    // GET: Obtener mantenimiento por ID
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

    // GET: Obtener mantenimientos por cuarto
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
                    .json("Error al obtener los mantenimientos: " + e.getMessage());
        }
    }

    // GET: Obtener mantenimientos por estado
    public void getMantenimientosByEstado(Context ctx) {
        try {
            String estado = ctx.pathParam("estado");
            List<Mantenimiento> mantenimientos = mantenimientoService.getMantenimientosByEstado(estado);
            ctx.json(mantenimientos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los mantenimientos: " + e.getMessage());
        }
    }

    // GET: Obtener mantenimientos pendientes
    public void getMantenimientosPendientes(Context ctx) {
        try {
            List<Mantenimiento> mantenimientos = mantenimientoService.getMantenimientosPendientes();
            ctx.json(mantenimientos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los mantenimientos pendientes: " + e.getMessage());
        }
    }

    // GET: Obtener mantenimientos completados
    public void getMantenimientosCompletados(Context ctx) {
        try {
            List<Mantenimiento> mantenimientos = mantenimientoService.getMantenimientosCompletados();
            ctx.json(mantenimientos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los mantenimientos completados: " + e.getMessage());
        }
    }

    // GET: Buscar mantenimientos por rango de fechas
    public void getMantenimientosByFechaRange(Context ctx) {
        try {
            LocalDate fechaInicio = LocalDate.parse(ctx.queryParam("fechaInicio"));
            LocalDate fechaFin = LocalDate.parse(ctx.queryParam("fechaFin"));
            List<Mantenimiento> mantenimientos = mantenimientoService.getMantenimientosByFechaReporteBetween(fechaInicio, fechaFin);
            ctx.json(mantenimientos);
        } catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error en los parámetros de fecha. Use formato YYYY-MM-DD");
        }
    }

    // PUT: Actualizar mantenimiento completo
    public void updateMantenimiento(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            String requestBody = ctx.body();
            Mantenimiento mantenimientoActualizado = objectMapper.readValue(requestBody, Mantenimiento.class);

            // Validar campos requeridos
            if (mantenimientoActualizado.getIdCuarto() == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo idCuarto es requerido");
                return;
            }

            if (mantenimientoActualizado.getFechaReporte() == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo fechaReporte es requerido");
                return;
            }

            if (mantenimientoActualizado.getDescripcionProblema() == null ||
                    mantenimientoActualizado.getDescripcionProblema().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo descripcionProblema es requerido");
                return;
            }

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

    // PATCH: Atender mantenimiento (fecha atención, costo y estado)
    public void atenderMantenimiento(Context ctx) {
        try {
            Integer idMantenimiento = Integer.parseInt(ctx.pathParam("id"));
            String requestBody = ctx.body();

            // Parsear request body manualmente
            AtenderMantenimientoRequest request = objectMapper.readValue(requestBody, AtenderMantenimientoRequest.class);

            // Validar campos requeridos
            if (request.getFechaAtencion() == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo fechaAtencion es requerido");
                return;
            }

            Mantenimiento mantenimiento = mantenimientoService.atenderMantenimiento(
                    idMantenimiento,
                    request.getFechaAtencion(),
                    request.getCosto(),
                    request.getEstado()
            );
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

    // PATCH: Actualizar solo el estado del mantenimiento
    public void actualizarEstadoMantenimiento(Context ctx) {
        try {
            Integer idMantenimiento = Integer.parseInt(ctx.pathParam("id"));
            String requestBody = ctx.body();
            EstadoMantenimientoRequest request = objectMapper.readValue(requestBody, EstadoMantenimientoRequest.class);

            // Validar campo requerido
            if (request.getEstado() == null || request.getEstado().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo estado es requerido");
                return;
            }

            Mantenimiento mantenimiento = mantenimientoService.actualizarEstadoMantenimiento(idMantenimiento, request.getEstado());
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

    // DELETE: Eliminar mantenimiento
    public void deleteMantenimiento(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));

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

    // Clases internas para requests
    private static class AtenderMantenimientoRequest {
        private LocalDate fechaAtencion;
        private BigDecimal costo;
        private String estado;

        public LocalDate getFechaAtencion() {
            return fechaAtencion;
        }

        public void setFechaAtencion(LocalDate fechaAtencion) {
            this.fechaAtencion = fechaAtencion;
        }

        public BigDecimal getCosto() {
            return costo;
        }

        public void setCosto(BigDecimal costo) {
            this.costo = costo;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }
    }

    private static class EstadoMantenimientoRequest {
        private String estado;

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }
    }
}