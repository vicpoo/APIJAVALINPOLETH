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

    public void createReporteInquilino(Context ctx) {
        try {
            ReporteInquilino reporteInquilino = objectMapper.readValue(ctx.body(), ReporteInquilino.class);
            ReporteInquilino savedReporteInquilino = reporteInquilinoService.createReporteInquilino(reporteInquilino);
            ctx.status(HttpStatus.CREATED)
                    .json(createSafeResponse(savedReporteInquilino));
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al crear el reporte del inquilino: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al crear el reporte del inquilino: " + e.getMessage());
        }
    }

    public void getAllReportesInquilinos(Context ctx) {
        try {
            List<ReporteInquilino> reportesInquilinos = reporteInquilinoService.getAllReportesInquilinos();
            List<ReporteInquilinoResponse> safeResponses = reportesInquilinos.stream()
                    .map(this::createSafeResponse)
                    .collect(Collectors.toList());
            ctx.json(safeResponses);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los reportes de inquilinos: " + e.getMessage());
        }
    }

    public void getReporteInquilinoById(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Optional<ReporteInquilino> reporteInquilino = reporteInquilinoService.getReporteInquilinoById(id);

            if (reporteInquilino.isPresent()) {
                ctx.json(createSafeResponse(reporteInquilino.get()));
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

    public void updateReporteInquilino(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            ReporteInquilino reporteActualizado = objectMapper.readValue(ctx.body(), ReporteInquilino.class);

            ReporteInquilino updatedReporteInquilino = reporteInquilinoService.updateReporteInquilino(id, reporteActualizado);
            ctx.json(createSafeResponse(updatedReporteInquilino));
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

    public void deleteReporteInquilino(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
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

    public void getReportesByInquilino(Context ctx) {
        try {
            Integer idInquilino = Integer.parseInt(ctx.pathParam("idInquilino"));
            List<ReporteInquilino> reportes = reporteInquilinoService.getReportesByInquilino(idInquilino);
            List<ReporteInquilinoResponse> safeResponses = reportes.stream()
                    .map(this::createSafeResponse)
                    .collect(Collectors.toList());
            ctx.json(safeResponses);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de inquilino inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener reportes del inquilino: " + e.getMessage());
        }
    }

    public void getReportesByCuarto(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("idCuarto"));
            List<ReporteInquilino> reportes = reporteInquilinoService.getReportesByCuarto(idCuarto);
            List<ReporteInquilinoResponse> safeResponses = reportes.stream()
                    .map(this::createSafeResponse)
                    .collect(Collectors.toList());
            ctx.json(safeResponses);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener reportes del cuarto: " + e.getMessage());
        }
    }

    // NUEVO MÉTODO: Endpoint para estadísticas de gráfica de barras
    public void getEstadisticasTiposReportes(Context ctx) {
        try {
            Map<String, Integer> estadisticas = reporteInquilinoService.getEstadisticasTiposReportes();

            // Crear respuesta estructurada para la gráfica
            EstadisticasResponse response = new EstadisticasResponse(estadisticas);

            ctx.json(response);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener estadísticas de tipos de reportes: " + e.getMessage());
        }
    }

    // Método auxiliar para crear respuestas seguras
    private ReporteInquilinoResponse createSafeResponse(ReporteInquilino reporte) {
        return new ReporteInquilinoResponse(reporte);
    }

    // Clase DTO para respuestas seguras
    public static class ReporteInquilinoResponse {
        public Integer idReporte;
        public Integer idInquilino;
        public String nombre;
        public String tipo;
        public String descripcion;
        public String fecha;
        public Integer idCuarto;
        public String estadoReporte;

        public ReporteInquilinoResponse(ReporteInquilino reporte) {
            this.idReporte = reporte.getIdReporte();
            this.idInquilino = reporte.getIdInquilino();
            this.nombre = reporte.getNombre();
            this.tipo = reporte.getTipo();
            this.descripcion = reporte.getDescripcion();
            this.fecha = reporte.getFecha() != null ? reporte.getFecha().toString() : null;
            this.idCuarto = reporte.getIdCuarto();
            this.estadoReporte = reporte.getEstadoReporte();
        }
    }

    // NUEVA CLASE: DTO para respuesta de estadísticas
    public static class EstadisticasResponse {
        public Map<String, Integer> datos;
        public String mensaje;
        public int total;

        public EstadisticasResponse(Map<String, Integer> estadisticas) {
            this.datos = estadisticas;
            this.total = estadisticas.values().stream().mapToInt(Integer::intValue).sum();
            this.mensaje = "Estadísticas de tipos de reportes obtenidas correctamente";
        }
    }
}