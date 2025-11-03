// HistorialReporteController.java
package com.poleth.api.controller;

import com.poleth.api.model.HistorialReporte;
import com.poleth.api.model.ReporteInquilino;
import com.poleth.api.service.HistorialReporteService;
import com.poleth.api.service.ReporteInquilinoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class HistorialReporteController {
    private final HistorialReporteService historialReporteService;
    private final ReporteInquilinoService reporteInquilinoService;
    private final ObjectMapper objectMapper;

    public HistorialReporteController(HistorialReporteService historialReporteService, ReporteInquilinoService reporteInquilinoService) {
        this.historialReporteService = historialReporteService;
        this.reporteInquilinoService = reporteInquilinoService;
        this.objectMapper = createConfiguredObjectMapper();
    }

    private ObjectMapper createConfiguredObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
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
                    .toList();
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
            
            if (!historialReporteService.existsById(id)) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Historial no encontrado con ID: " + id);
                return;
            }
            
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
                    .toList();
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
                    .toList();
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
                    .toList();
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
                    .toList();
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
                    .toList();
            ctx.json(safeResponses);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de reporte inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener historiales del reporte ordenados: " + e.getMessage());
        }
    }

    public void getHistorialesRecientes(Context ctx) {
        try {
            String diasParam = ctx.queryParam("dias");
            int dias = Integer.parseInt(diasParam == null || diasParam.isEmpty() ? "30" : diasParam);
            List<HistorialReporte> historiales = historialReporteService.getHistorialesRecientes(dias);
            List<HistorialReporteResponse> safeResponses = historiales.stream()
                    .map(this::createSafeResponse)
                    .toList();
            ctx.json(safeResponses);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Parámetro días inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener historiales recientes: " + e.getMessage());
        }
    }

    public void getHistorialesByDescripcion(Context ctx) {
        try {
            String texto = ctx.queryParam("texto");
            if (texto == null || texto.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro texto es requerido");
                return;
            }

            List<HistorialReporte> historiales = historialReporteService.getHistorialesByDescripcionContaining(texto);
            List<HistorialReporteResponse> safeResponses = historiales.stream()
                    .map(this::createSafeResponse)
                    .toList();
            ctx.json(safeResponses);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar historiales por descripción: " + e.getMessage());
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
                    .toList();
            ctx.json(safeResponses);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar historiales por nombre: " + e.getMessage());
        }
    }

    public void getHistorialesWithRelations(Context ctx) {
        try {
            List<HistorialReporte> historiales = historialReporteService.getHistorialesWithRelations();
            List<HistorialReporteResponse> safeResponses = historiales.stream()
                    .map(this::createSafeResponse)
                    .toList();
            ctx.json(safeResponses);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener historiales con relaciones: " + e.getMessage());
        }
    }

    public void getHistorialByIdWithRelations(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Optional<HistorialReporte> historial = historialReporteService.getHistorialByIdWithRelations(id);

            if (historial.isPresent()) {
                ctx.json(createSafeResponse(historial.get()));
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Historial no encontrado con ID: " + id);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de historial inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el historial con relaciones: " + e.getMessage());
        }
    }

    public void getHistorialesByReporteWithRelations(Context ctx) {
        try {
            Integer idReporte = Integer.parseInt(ctx.pathParam("idReporte"));
            List<HistorialReporte> historiales = historialReporteService.getHistorialesByReporteWithRelations(idReporte);
            List<HistorialReporteResponse> safeResponses = historiales.stream()
                    .map(this::createSafeResponse)
                    .toList();
            ctx.json(safeResponses);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de reporte inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener historiales del reporte con relaciones: " + e.getMessage());
        }
    }

    // Métodos de gestión específicos

    public void registrarHistorialDesdeReporte(Context ctx) {
        try {
            Integer idReporte = Integer.parseInt(ctx.pathParam("idReporte"));
            String body = ctx.body();
            
            String usuarioRegistro = objectMapper.readTree(body).get("usuarioRegistro").asText();

            Optional<ReporteInquilino> reporteOpt = reporteInquilinoService.getReporteInquilinoById(idReporte);
            if (reporteOpt.isEmpty()) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Reporte no encontrado con ID: " + idReporte);
                return;
            }

            HistorialReporte historial = historialReporteService.registrarHistorialDesdeReporte(reporteOpt.get(), usuarioRegistro);
            ctx.status(HttpStatus.CREATED)
                    .json(createSafeResponse(historial));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de reporte inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al registrar historial desde reporte: " + e.getMessage());
        }
    }

    public void registrarHistorialConAccion(Context ctx) {
        try {
            Integer idReporte = Integer.parseInt(ctx.pathParam("idReporte"));
            String body = ctx.body();
            
            String accion = objectMapper.readTree(body).get("accion").asText();
            String usuarioRegistro = objectMapper.readTree(body).get("usuarioRegistro").asText();

            Optional<ReporteInquilino> reporteOpt = reporteInquilinoService.getReporteInquilinoById(idReporte);
            if (reporteOpt.isEmpty()) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Reporte no encontrado con ID: " + idReporte);
                return;
            }

            HistorialReporte historial = historialReporteService.registrarHistorialConAccion(reporteOpt.get(), accion, usuarioRegistro);
            ctx.status(HttpStatus.CREATED)
                    .json(createSafeResponse(historial));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de reporte inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al registrar historial con acción: " + e.getMessage());
        }
    }

    public void registrarCambioEstado(Context ctx) {
        try {
            Integer idReporte = Integer.parseInt(ctx.pathParam("idReporte"));
            String body = ctx.body();
            
            String estadoAnterior = objectMapper.readTree(body).get("estadoAnterior").asText();
            String estadoNuevo = objectMapper.readTree(body).get("estadoNuevo").asText();
            String usuarioRegistro = objectMapper.readTree(body).get("usuarioRegistro").asText();

            Optional<ReporteInquilino> reporteOpt = reporteInquilinoService.getReporteInquilinoById(idReporte);
            if (reporteOpt.isEmpty()) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Reporte no encontrado con ID: " + idReporte);
                return;
            }

            HistorialReporte historial = historialReporteService.registrarCambioEstado(reporteOpt.get(), estadoAnterior, estadoNuevo, usuarioRegistro);
            ctx.status(HttpStatus.CREATED)
                    .json(createSafeResponse(historial));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de reporte inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al registrar cambio de estado: " + e.getMessage());
        }
    }

    public void registrarCierreReporte(Context ctx) {
        try {
            Integer idReporte = Integer.parseInt(ctx.pathParam("idReporte"));
            String body = ctx.body();
            
            String accionesTomadas = objectMapper.readTree(body).get("accionesTomadas").asText();
            String usuarioRegistro = objectMapper.readTree(body).get("usuarioRegistro").asText();

            Optional<ReporteInquilino> reporteOpt = reporteInquilinoService.getReporteInquilinoById(idReporte);
            if (reporteOpt.isEmpty()) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Reporte no encontrado con ID: " + idReporte);
                return;
            }

            HistorialReporte historial = historialReporteService.registrarCierreReporte(reporteOpt.get(), accionesTomadas, usuarioRegistro);
            ctx.status(HttpStatus.CREATED)
                    .json(createSafeResponse(historial));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de reporte inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al registrar cierre de reporte: " + e.getMessage());
        }
    }

    // Métodos de verificación

    public void existsByReporte(Context ctx) {
        try {
            Integer idReporte = Integer.parseInt(ctx.pathParam("idReporte"));
            boolean exists = historialReporteService.existsByReporte(idReporte);
            ctx.json(exists);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de reporte inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar historiales del reporte: " + e.getMessage());
        }
    }

    public void tieneHistorialReciente(Context ctx) {
        try {
            Integer idReporte = Integer.parseInt(ctx.pathParam("idReporte"));
            String horasParam = ctx.queryParam("horas");
            int horas = Integer.parseInt(horasParam == null || horasParam.isEmpty() ? "24" : horasParam);
            boolean tieneReciente = historialReporteService.tieneHistorialReciente(idReporte, horas);
            ctx.json(tieneReciente);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de reporte o parámetro horas inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar historial reciente: " + e.getMessage());
        }
    }

    // Métodos de conteo

    public void countHistorialReportes(Context ctx) {
        try {
            Long count = historialReporteService.countHistorialReportes();
            ctx.json(count);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar historiales: " + e.getMessage());
        }
    }

    public void countHistorialesByReporte(Context ctx) {
        try {
            Integer idReporte = Integer.parseInt(ctx.pathParam("idReporte"));
            Long count = historialReporteService.countHistorialesByReporte(idReporte);
            ctx.json(count);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de reporte inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar historiales del reporte: " + e.getMessage());
        }
    }

    public void countHistorialesByUsuario(Context ctx) {
        try {
            String usuario = ctx.queryParam("usuario");
            if (usuario == null || usuario.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro usuario es requerido");
                return;
            }

            Long count = historialReporteService.countHistorialesByUsuario(usuario);
            ctx.json(count);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar historiales por usuario: " + e.getMessage());
        }
    }

    public void getLineaTiempoReporte(Context ctx) {
        try {
            Integer idReporte = Integer.parseInt(ctx.pathParam("idReporte"));
            List<HistorialReporte> historiales = historialReporteService.getLineaTiempoReporte(idReporte);
            List<HistorialReporteResponse> safeResponses = historiales.stream()
                    .map(this::createSafeResponse)
                    .toList();
            ctx.json(safeResponses);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de reporte inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener línea de tiempo del reporte: " + e.getMessage());
        }
    }

    public void getActividadRecienteUsuario(Context ctx) {
        try {
            String usuario = ctx.queryParam("usuario");
            if (usuario == null || usuario.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro usuario es requerido");
                return;
            }

            String limiteParam = ctx.queryParam("limite");
            int limite = Integer.parseInt(limiteParam == null || limiteParam.isEmpty() ? "10" : limiteParam);
            List<HistorialReporte> historiales = historialReporteService.getActividadRecienteUsuario(usuario, limite);
            List<HistorialReporteResponse> safeResponses = historiales.stream()
                    .map(this::createSafeResponse)
                    .toList();
            ctx.json(safeResponses);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Parámetro límite inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener actividad reciente del usuario: " + e.getMessage());
        }
    }

    public void getEstadisticasActividad(Context ctx) {
        try {
            String estadisticas = historialReporteService.obtenerEstadisticasActividad();
            ctx.json(estadisticas);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener estadísticas de actividad: " + e.getMessage());
        }
    }

    public void limpiarHistorialesAntiguos(Context ctx) {
        try {
            String diasParam = ctx.queryParam("dias");
            int dias = Integer.parseInt(diasParam == null || diasParam.isEmpty() ? "365" : diasParam);
            int eliminados = historialReporteService.limpiarHistorialesAntiguos(dias);
            ctx.json("Se eliminaron " + eliminados + " historiales antiguos (más de " + dias + " días)");
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Parámetro días inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al limpiar historiales antiguos: " + e.getMessage());
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
        public LocalDateTime fechaRegistro;
        public String usuarioRegistro;
        public String nombreReporteOriginal;
        public String tipoReporteOriginal;

        public HistorialReporteResponse(HistorialReporte historial) {
            this.idHistorial = historial.getIdHistorial();
            this.idReporte = historial.getIdReporte();
            this.nombreReporteHist = historial.getNombreReporteHist();
            this.tipoReporteHist = historial.getTipoReporteHist();
            this.descripcionHist = historial.getDescripcionHist();
            this.fechaRegistro = historial.getFechaRegistro();
            this.usuarioRegistro = historial.getUsuarioRegistro();
            
            // Manejar relaciones de forma segura
            if (historial.getReporte() != null) {
                this.nombreReporteOriginal = historial.getReporte().getNombre();
                this.tipoReporteOriginal = historial.getReporte().getTipo();
            }
        }
    }
}