//ReporteInquilinoController.java
package com.poleth.api.controller;

import com.poleth.api.model.ReporteInquilino;
import com.poleth.api.service.ReporteInquilinoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;

public class ReporteInquilinoController {
    private final ReporteInquilinoService reporteInquilinoService;
    private final ObjectMapper objectMapper;

    public ReporteInquilinoController(ReporteInquilinoService reporteInquilinoService) {
        this.reporteInquilinoService = reporteInquilinoService;
        this.objectMapper = createConfiguredObjectMapper();
    }

    private ObjectMapper createConfiguredObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    // POST: Crear nuevo reporte de inquilino
    public void createReporteInquilino(Context ctx) {
        try {
            String requestBody = ctx.body();
            ReporteInquilino reporteInquilino = objectMapper.readValue(requestBody, ReporteInquilino.class);

            // Validar campos requeridos
            if (reporteInquilino.getIdInquilino() == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo idInquilino es requerido");
                return;
            }

            if (reporteInquilino.getNombre() == null || reporteInquilino.getNombre().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo nombre es requerido");
                return;
            }

            if (reporteInquilino.getDescripcion() == null || reporteInquilino.getDescripcion().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo descripcion es requerido");
                return;
            }

            if (reporteInquilino.getIdCuarto() == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo idCuarto es requerido");
                return;
            }

            // Fecha por defecto si no se especifica
            if (reporteInquilino.getFecha() == null) {
                reporteInquilino.setFecha(LocalDate.now());
            }

            // Estado por defecto si no se especifica
            if (reporteInquilino.getEstadoReporte() == null ||
                    reporteInquilino.getEstadoReporte().trim().isEmpty()) {
                reporteInquilino.setEstadoReporte("abierto");
            }

            ReporteInquilino savedReporteInquilino = reporteInquilinoService.createReporteInquilino(reporteInquilino);
            ctx.status(HttpStatus.CREATED)
                    .json(savedReporteInquilino);
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al crear el reporte del inquilino: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al crear el reporte del inquilino: " + e.getMessage());
        }
    }

    // GET: Obtener todos los reportes de inquilinos
    public void getAllReportesInquilinos(Context ctx) {
        try {
            List<ReporteInquilino> reportesInquilinos = reporteInquilinoService.getAllReportesInquilinos();
            ctx.json(reportesInquilinos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los reportes de inquilinos: " + e.getMessage());
        }
    }

    // GET: Obtener reporte por ID
    public void getReporteInquilinoById(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Optional<ReporteInquilino> reporteInquilino = reporteInquilinoService.getReporteInquilinoById(id);

            if (reporteInquilino.isPresent()) {
                ctx.json(reporteInquilino.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Reporte no encontrado con ID: " + id);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de reporte inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el reporte del inquilino: " + e.getMessage());
        }
    }

    // PUT: Actualizar reporte completo
    public void updateReporteInquilino(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            String requestBody = ctx.body();
            ReporteInquilino reporteActualizado = objectMapper.readValue(requestBody, ReporteInquilino.class);

            // Validar campos requeridos
            if (reporteActualizado.getIdInquilino() == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo idInquilino es requerido");
                return;
            }

            if (reporteActualizado.getNombre() == null || reporteActualizado.getNombre().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo nombre es requerido");
                return;
            }

            if (reporteActualizado.getDescripcion() == null || reporteActualizado.getDescripcion().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo descripcion es requerido");
                return;
            }

            if (reporteActualizado.getIdCuarto() == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo idCuarto es requerido");
                return;
            }

            ReporteInquilino updatedReporteInquilino = reporteInquilinoService.updateReporteInquilino(id, reporteActualizado);
            ctx.json(updatedReporteInquilino);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de reporte inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al actualizar el reporte del inquilino: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al actualizar el reporte del inquilino: " + e.getMessage());
        }
    }

    // PATCH: Cerrar reporte
    public void cerrarReporte(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            String requestBody = ctx.body();

            // Parsear request body
            CerrarReporteRequest request = objectMapper.readValue(requestBody, CerrarReporteRequest.class);

            // Validar acciones tomadas
            if (request.getAccionesTomadas() == null || request.getAccionesTomadas().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo accionesTomadas es requerido para cerrar un reporte");
                return;
            }

            ReporteInquilino reporte = reporteInquilinoService.cerrarReporte(
                    id,
                    request.getAccionesTomadas(),
                    request.getEstado()
            );
            ctx.json(reporte);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de reporte inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al cerrar el reporte: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al cerrar el reporte: " + e.getMessage());
        }
    }

    // PATCH: Actualizar solo el estado del reporte
    public void actualizarEstadoReporte(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            String requestBody = ctx.body();
            EstadoReporteRequest request = objectMapper.readValue(requestBody, EstadoReporteRequest.class);

            // Validar campo requerido
            if (request.getEstado() == null || request.getEstado().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo estado es requerido");
                return;
            }

            ReporteInquilino reporte = reporteInquilinoService.actualizarEstadoReporte(id, request.getEstado());
            ctx.json(reporte);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de reporte inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al actualizar estado: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al actualizar estado: " + e.getMessage());
        }
    }

    // DELETE: Eliminar reporte
    public void deleteReporteInquilino(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));

            if (!reporteInquilinoService.existsById(id)) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Reporte no encontrado con ID: " + id);
                return;
            }

            reporteInquilinoService.deleteReporteInquilino(id);
            ctx.status(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de reporte inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al eliminar el reporte del inquilino: " + e.getMessage());
        }
    }

    // GET: Obtener reportes por inquilino
    public void getReportesByInquilino(Context ctx) {
        try {
            Integer idInquilino = Integer.parseInt(ctx.pathParam("idInquilino"));
            List<ReporteInquilino> reportes = reporteInquilinoService.getReportesByInquilino(idInquilino);
            ctx.json(reportes);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de inquilino inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener reportes del inquilino: " + e.getMessage());
        }
    }

    // GET: Obtener reportes por cuarto
    public void getReportesByCuarto(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("idCuarto"));
            List<ReporteInquilino> reportes = reporteInquilinoService.getReportesByCuarto(idCuarto);
            ctx.json(reportes);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener reportes del cuarto: " + e.getMessage());
        }
    }

    // GET: Obtener reportes por estado
    public void getReportesByEstado(Context ctx) {
        try {
            String estado = ctx.pathParam("estado");
            List<ReporteInquilino> reportes = reporteInquilinoService.getReportesByEstado(estado);
            ctx.json(reportes);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener reportes por estado: " + e.getMessage());
        }
    }

    // GET: Obtener reportes abiertos
    public void getReportesAbiertos(Context ctx) {
        try {
            List<ReporteInquilino> reportes = reporteInquilinoService.getReportesAbiertos();
            ctx.json(reportes);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener reportes abiertos: " + e.getMessage());
        }
    }

    // GET: Obtener reportes cerrados
    public void getReportesCerrados(Context ctx) {
        try {
            List<ReporteInquilino> reportes = reporteInquilinoService.getReportesCerrados();
            ctx.json(reportes);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener reportes cerrados: " + e.getMessage());
        }
    }

    // GET: Buscar reportes por rango de fechas
    public void getReportesByFechaRange(Context ctx) {
        try {
            LocalDate fechaInicio = LocalDate.parse(ctx.queryParam("fechaInicio"));
            LocalDate fechaFin = LocalDate.parse(ctx.queryParam("fechaFin"));
            List<ReporteInquilino> reportes = reporteInquilinoService.getReportesByFechaBetween(fechaInicio, fechaFin);
            ctx.json(reportes);
        } catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error en los parámetros de fecha. Use formato YYYY-MM-DD");
        }
    }

    // GET: Estadísticas de tipos de reportes
    public void getEstadisticasTiposReportes(Context ctx) {
        try {
            Map<String, Integer> estadisticas = reporteInquilinoService.getEstadisticasTiposReportes();
            ctx.json(estadisticas);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener estadísticas de tipos de reportes: " + e.getMessage());
        }
    }

    // GET: Estadísticas completas de reportes
    public void getEstadisticasCompletas(Context ctx) {
        try {
            Map<String, Object> estadisticas = reporteInquilinoService.getEstadisticasCompletas();
            ctx.json(estadisticas);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener estadísticas completas: " + e.getMessage());
        }
    }

    // GET: Buscar reportes por texto en descripción
    public void buscarReportesPorTexto(Context ctx) {
        try {
            String texto = ctx.queryParam("texto");
            if (texto == null || texto.trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro 'texto' es requerido");
                return;
            }

            List<ReporteInquilino> reportes = reporteInquilinoService.buscarReportesPorTexto(texto);
            ctx.json(reportes);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar reportes: " + e.getMessage());
        }
    }

    // Clases internas para requests
    private static class CerrarReporteRequest {
        private String accionesTomadas;
        private String estado;

        public String getAccionesTomadas() {
            return accionesTomadas;
        }

        public void setAccionesTomadas(String accionesTomadas) {
            this.accionesTomadas = accionesTomadas;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }
    }

    private static class EstadoReporteRequest {
        private String estado;

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }
    }
}