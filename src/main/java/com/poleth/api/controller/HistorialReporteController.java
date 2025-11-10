// HistorialReporteController.java
package com.poleth.api.controller;

import com.poleth.api.model.HistorialReporte;
import com.poleth.api.service.HistorialReporteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.poleth.api.service.ReporteInquilinoService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HistorialReporteController {
    private final HistorialReporteService historialReporteService;
    private final ObjectMapper objectMapper;

    public HistorialReporteController(HistorialReporteService historialReporteService, ReporteInquilinoService reporteInquilinoService) {
        this.historialReporteService = historialReporteService;
        this.objectMapper = createConfiguredObjectMapper();
    }

    private ObjectMapper createConfiguredObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    public void createHistorialReporte(Context ctx) {
        try {
            HistorialReporte historialReporte = objectMapper.readValue(ctx.body(), HistorialReporte.class);
            HistorialReporte savedHistorialReporte = historialReporteService.createHistorialReporte(historialReporte);
            ctx.status(HttpStatus.CREATED)
                    .json(createSafeResponse(savedHistorialReporte));
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al crear el historial del reporte: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al crear el historial del reporte: " + e.getMessage());
        }
    }

    public void getAllHistorialReportes(Context ctx) {
        try {
            List<HistorialReporte> historialesReportes = historialReporteService.getAllHistorialReportes();
            List<HistorialReporteResponse> safeResponses = historialesReportes.stream()
                    .map(this::createSafeResponse)
                    .collect(Collectors.toList());
            ctx.json(safeResponses);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los historiales de reportes: " + e.getMessage());
        }
    }

    public void getHistorialReporteById(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Optional<HistorialReporte> historialReporte = historialReporteService.getHistorialReporteById(id);

            if (historialReporte.isPresent()) {
                ctx.json(createSafeResponse(historialReporte.get()));
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Historial no encontrado con ID: " + id);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de historial inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el historial del reporte: " + e.getMessage());
        }
    }

    public void updateHistorialReporte(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            HistorialReporte historialActualizado = objectMapper.readValue(ctx.body(), HistorialReporte.class);

            HistorialReporte updatedHistorialReporte = historialReporteService.updateHistorialReporte(id, historialActualizado);
            ctx.json(createSafeResponse(updatedHistorialReporte));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de historial inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al actualizar el historial del reporte: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al actualizar el historial del reporte: " + e.getMessage());
        }
    }

    public void deleteHistorialReporte(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            historialReporteService.deleteHistorialReporte(id);
            ctx.status(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de historial inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al eliminar el historial del reporte: " + e.getMessage());
        }
    }

    // Métodos específicos para HistorialReporte

    public void getHistorialesByReporte(Context ctx) {
        try {
            Integer idReporte = Integer.parseInt(ctx.pathParam("idReporte"));
            List<HistorialReporte> historiales = historialReporteService.getHistorialesByReporte(idReporte);
            List<HistorialReporteResponse> safeResponses = historiales.stream()
                    .map(this::createSafeResponse)
                    .collect(Collectors.toList());
            ctx.json(safeResponses);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de reporte inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener historiales del reporte: " + e.getMessage());
        }
    }

    public void getHistorialesByTipo(Context ctx) {
        try {
            String tipo = ctx.queryParam("tipo");
            if (tipo == null || tipo.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro tipo es requerido");
                return;
            }

            List<HistorialReporte> historiales = historialReporteService.getHistorialesByTipo(tipo);
            List<HistorialReporteResponse> safeResponses = historiales.stream()
                    .map(this::createSafeResponse)
                    .collect(Collectors.toList());
            ctx.json(safeResponses);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar historiales por tipo: " + e.getMessage());
        }
    }

    public void getHistorialesByUsuario(Context ctx) {
        try {
            String usuario = ctx.queryParam("usuario");
            if (usuario == null || usuario.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro usuario es requerido");
                return;
            }

            List<HistorialReporte> historiales = historialReporteService.getHistorialesByUsuario(usuario);
            List<HistorialReporteResponse> safeResponses = historiales.stream()
                    .map(this::createSafeResponse)
                    .collect(Collectors.toList());
            ctx.json(safeResponses);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar historiales por usuario: " + e.getMessage());
        }
    }

    public void getUltimoHistorialByReporte(Context ctx) {
        try {
            Integer idReporte = Integer.parseInt(ctx.pathParam("idReporte"));
            Optional<HistorialReporte> historial = historialReporteService.getUltimoHistorialByReporte(idReporte);

            if (historial.isPresent()) {
                ctx.json(createSafeResponse(historial.get()));
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("No se encontró historial para el reporte con ID: " + idReporte);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de reporte inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el último historial del reporte: " + e.getMessage());
        }
    }

    public void getAllHistorialesOrderByFechaDesc(Context ctx) {
        try {
            List<HistorialReporte> historiales = historialReporteService.getAllHistorialesOrderByFechaDesc();
            List<HistorialReporteResponse> safeResponses = historiales.stream()
                    .map(this::createSafeResponse)
                    .collect(Collectors.toList());
            ctx.json(safeResponses);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener historiales ordenados: " + e.getMessage());
        }
    }

    public void getHistorialesByReporteOrderByFechaDesc(Context ctx) {
        try {
            Integer idReporte = Integer.parseInt(ctx.pathParam("idReporte"));
            List<HistorialReporte> historiales = historialReporteService.getHistorialesByReporteOrderByFechaDesc(idReporte);
            List<HistorialReporteResponse> safeResponses = historiales.stream()
                    .map(this::createSafeResponse)
                    .collect(Collectors.toList());
            ctx.json(safeResponses);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de reporte inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener historiales del reporte ordenados: " + e.getMessage());
        }
    }

    public void getHistorialesByNombre(Context ctx) {
        try {
            String texto = ctx.queryParam("texto");
            if (texto == null || texto.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro texto es requerido");
                return;
            }

            List<HistorialReporte> historiales = historialReporteService.getHistorialesByNombreContaining(texto);
            List<HistorialReporteResponse> safeResponses = historiales.stream()
                    .map(this::createSafeResponse)
                    .collect(Collectors.toList());
            ctx.json(safeResponses);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar historiales por nombre: " + e.getMessage());
        }
    }

    // Método auxiliar para crear respuestas seguras
    private HistorialReporteResponse createSafeResponse(HistorialReporte historial) {
        return new HistorialReporteResponse(historial);
    }

    // Clase DTO para respuestas seguras
    public static class HistorialReporteResponse {
        public Integer idHistorial;
        public Integer idReporte;
        public String nombreReporteHist;
        public String tipoReporteHist;
        public String descripcionHist;
        public String fechaRegistro;
        public String usuarioRegistro;

        public HistorialReporteResponse(HistorialReporte historial) {
            this.idHistorial = historial.getIdHistorial();
            this.idReporte = historial.getIdReporte();
            this.nombreReporteHist = historial.getNombreReporteHist();
            this.tipoReporteHist = historial.getTipoReporteHist();
            this.descripcionHist = historial.getDescripcionHist();
            this.fechaRegistro = historial.getFechaRegistro() != null ? historial.getFechaRegistro().toString() : null;
            this.usuarioRegistro = historial.getUsuarioRegistro();
        }
    }
}