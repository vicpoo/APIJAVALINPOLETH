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

    public HistorialReporteController(HistorialReporteService historialReporteService,
                                      ReporteInquilinoService reporteInquilinoService) {
        this.historialReporteService = historialReporteService;
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

    // POST: Crear nuevo historial de reporte
    public void createHistorialReporte(Context ctx) {
        try {
            String requestBody = ctx.body();
            HistorialReporte historialReporte = objectMapper.readValue(requestBody, HistorialReporte.class);

            // Validar campos requeridos
            if (historialReporte.getIdReporte() == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo idReporte es requerido");
                return;
            }

            if (historialReporte.getNombreReporteHist() == null ||
                    historialReporte.getNombreReporteHist().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo nombreReporteHist es requerido");
                return;
            }

            if (historialReporte.getDescripcionHist() == null ||
                    historialReporte.getDescripcionHist().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo descripcionHist es requerido");
                return;
            }

            // Validar longitud del nombre
            if (historialReporte.getNombreReporteHist().length() > 100) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El nombre no puede exceder 100 caracteres");
                return;
            }

            // Validar longitud del tipo
            if (historialReporte.getTipoReporteHist() != null &&
                    historialReporte.getTipoReporteHist().length() > 50) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El tipo no puede exceder 50 caracteres");
                return;
            }

            // Validar longitud del usuario
            if (historialReporte.getUsuarioRegistro() != null &&
                    historialReporte.getUsuarioRegistro().length() > 50) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El usuario de registro no puede exceder 50 caracteres");
                return;
            }

            // Verificar que el reporte exista
            Optional<ReporteInquilino> reporteOpt = reporteInquilinoService.getReporteInquilinoById(
                    historialReporte.getIdReporte()
            );
            if (reporteOpt.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("No existe un reporte con ID: " + historialReporte.getIdReporte());
                return;
            }

            HistorialReporte savedHistorialReporte = historialReporteService.createHistorialReporte(historialReporte);
            ctx.status(HttpStatus.CREATED)
                    .json(savedHistorialReporte);
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al crear el historial del reporte: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al crear el historial del reporte: " + e.getMessage());
        }
    }

    // POST: Crear historial a partir de reporte
    public void createHistorialFromReporte(Context ctx) {
        try {
            Integer idReporte = Integer.parseInt(ctx.pathParam("idReporte"));
            String requestBody = ctx.body();
            CrearHistorialRequest request = objectMapper.readValue(requestBody, CrearHistorialRequest.class);

            // Validar usuario
            if (request.getUsuarioRegistro() == null || request.getUsuarioRegistro().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo usuarioRegistro es requerido");
                return;
            }

            // Verificar que el reporte exista
            Optional<ReporteInquilino> reporteOpt = reporteInquilinoService.getReporteInquilinoById(idReporte);
            if (reporteOpt.isEmpty()) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Reporte no encontrado con ID: " + idReporte);
                return;
            }

            ReporteInquilino reporte = reporteOpt.get();

            // Crear historial desde reporte
            HistorialReporte historial;
            if (request.getAccion() != null && !request.getAccion().trim().isEmpty()) {
                historial = HistorialReporte.crearConAccion(
                        reporte,
                        request.getAccion(),
                        request.getDetalles(),
                        request.getUsuarioRegistro()
                );
            } else {
                historial = HistorialReporte.crearDesdeReporte(reporte, request.getUsuarioRegistro());
            }

            // Si se proporciona descripción personalizada, usarla
            if (request.getDescripcionPersonalizada() != null &&
                    !request.getDescripcionPersonalizada().trim().isEmpty()) {
                historial.setDescripcionHist(request.getDescripcionPersonalizada());
            }

            // Si se proporciona nombre personalizado, usarlo
            if (request.getNombrePersonalizado() != null &&
                    !request.getNombrePersonalizado().trim().isEmpty()) {
                historial.setNombreReporteHist(request.getNombrePersonalizado());
            }

            HistorialReporte savedHistorial = historialReporteService.createHistorialReporte(historial);
            ctx.status(HttpStatus.CREATED)
                    .json(savedHistorial);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de reporte inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al crear historial: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al crear historial: " + e.getMessage());
        }
    }

    // GET: Obtener todos los historiales de reportes
    public void getAllHistorialReportes(Context ctx) {
        try {
            List<HistorialReporte> historialesReportes = historialReporteService.getAllHistorialReportes();
            ctx.json(historialesReportes);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los historiales de reportes: " + e.getMessage());
        }
    }

    // GET: Obtener historial por ID
    public void getHistorialReporteById(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Optional<HistorialReporte> historialReporte = historialReporteService.getHistorialReporteById(id);

            if (historialReporte.isPresent()) {
                ctx.json(historialReporte.get());
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

    // PUT: Actualizar historial completo
    public void updateHistorialReporte(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            String requestBody = ctx.body();
            HistorialReporte historialActualizado = objectMapper.readValue(requestBody, HistorialReporte.class);

            // Validar campos requeridos
            if (historialActualizado.getNombreReporteHist() == null ||
                    historialActualizado.getNombreReporteHist().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo nombreReporteHist es requerido");
                return;
            }

            if (historialActualizado.getDescripcionHist() == null ||
                    historialActualizado.getDescripcionHist().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo descripcionHist es requerido");
                return;
            }

            // Validar longitud del nombre
            if (historialActualizado.getNombreReporteHist().length() > 100) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El nombre no puede exceder 100 caracteres");
                return;
            }

            // Validar longitud del tipo
            if (historialActualizado.getTipoReporteHist() != null &&
                    historialActualizado.getTipoReporteHist().length() > 50) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El tipo no puede exceder 50 caracteres");
                return;
            }

            // Validar longitud del usuario
            if (historialActualizado.getUsuarioRegistro() != null &&
                    historialActualizado.getUsuarioRegistro().length() > 50) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El usuario de registro no puede exceder 50 caracteres");
                return;
            }

            HistorialReporte updatedHistorialReporte = historialReporteService.updateHistorialReporte(id, historialActualizado);
            ctx.json(updatedHistorialReporte);
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

    // DELETE: Eliminar historial
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

    // GET: Obtener historiales por reporte
    public void getHistorialesByReporte(Context ctx) {
        try {
            Integer idReporte = Integer.parseInt(ctx.pathParam("idReporte"));
            List<HistorialReporte> historiales = historialReporteService.getHistorialesByReporte(idReporte);
            ctx.json(historiales);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de reporte inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener historiales del reporte: " + e.getMessage());
        }
    }

    // GET: Obtener historiales por tipo
    public void getHistorialesByTipo(Context ctx) {
        try {
            String tipo = ctx.queryParam("tipo");
            if (tipo == null || tipo.trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro tipo es requerido");
                return;
            }

            List<HistorialReporte> historiales = historialReporteService.getHistorialesByTipo(tipo);
            ctx.json(historiales);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar historiales por tipo: " + e.getMessage());
        }
    }

    // GET: Obtener historiales por usuario
    public void getHistorialesByUsuario(Context ctx) {
        try {
            String usuario = ctx.queryParam("usuario");
            if (usuario == null || usuario.trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro usuario es requerido");
                return;
            }

            List<HistorialReporte> historiales = historialReporteService.getHistorialesByUsuario(usuario);
            ctx.json(historiales);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar historiales por usuario: " + e.getMessage());
        }
    }

    // GET: Obtener último historial por reporte
    public void getUltimoHistorialByReporte(Context ctx) {
        try {
            Integer idReporte = Integer.parseInt(ctx.pathParam("idReporte"));
            Optional<HistorialReporte> historial = historialReporteService.getUltimoHistorialByReporte(idReporte);

            if (historial.isPresent()) {
                ctx.json(historial.get());
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

    // GET: Obtener historiales ordenados por fecha descendente
    public void getAllHistorialesOrderByFechaDesc(Context ctx) {
        try {
            List<HistorialReporte> historiales = historialReporteService.getAllHistorialesOrderByFechaDesc();
            ctx.json(historiales);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener historiales ordenados: " + e.getMessage());
        }
    }

    // GET: Obtener historiales por reporte ordenados por fecha descendente
    public void getHistorialesByReporteOrderByFechaDesc(Context ctx) {
        try {
            Integer idReporte = Integer.parseInt(ctx.pathParam("idReporte"));
            List<HistorialReporte> historiales = historialReporteService.getHistorialesByReporteOrderByFechaDesc(idReporte);
            ctx.json(historiales);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de reporte inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener historiales del reporte ordenados: " + e.getMessage());
        }
    }

    // GET: Buscar historiales por texto en nombre
    public void getHistorialesByNombre(Context ctx) {
        try {
            String texto = ctx.queryParam("texto");
            if (texto == null || texto.trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro texto es requerido");
                return;
            }

            List<HistorialReporte> historiales = historialReporteService.getHistorialesByNombreContaining(texto);
            ctx.json(historiales);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar historiales por nombre: " + e.getMessage());
        }
    }

    // GET: Buscar historiales por texto en descripción
    public void getHistorialesByDescripcion(Context ctx) {
        try {
            String texto = ctx.queryParam("texto");
            if (texto == null || texto.trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro texto es requerido");
                return;
            }

            List<HistorialReporte> historiales = historialReporteService.getHistorialesByDescripcionContaining(texto);
            ctx.json(historiales);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar historiales por descripción: " + e.getMessage());
        }
    }

    // GET: Obtener historiales recientes (últimos 7 días)
    public void getHistorialesRecientes(Context ctx) {
        try {
            List<HistorialReporte> historiales = historialReporteService.getHistorialesRecientes();
            ctx.json(historiales);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener historiales recientes: " + e.getMessage());
        }
    }

    // GET: Obtener historiales del día actual
    public void getHistorialesDelDia(Context ctx) {
        try {
            List<HistorialReporte> historiales = historialReporteService.getHistorialesDelDia();
            ctx.json(historiales);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener historiales del día: " + e.getMessage());
        }
    }

    // GET: Obtener estadísticas de historiales
    public void getEstadisticasHistoriales(Context ctx) {
        try {
            var estadisticas = historialReporteService.getEstadisticasHistoriales();
            ctx.json(estadisticas);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener estadísticas: " + e.getMessage());
        }
    }

    // Clases internas para requests
    private static class CrearHistorialRequest {
        private String usuarioRegistro;
        private String accion;
        private String detalles;
        private String descripcionPersonalizada;
        private String nombrePersonalizado;

        public String getUsuarioRegistro() {
            return usuarioRegistro;
        }

        public void setUsuarioRegistro(String usuarioRegistro) {
            this.usuarioRegistro = usuarioRegistro;
        }

        public String getAccion() {
            return accion;
        }

        public void setAccion(String accion) {
            this.accion = accion;
        }

        public String getDetalles() {
            return detalles;
        }

        public void setDetalles(String detalles) {
            this.detalles = detalles;
        }

        public String getDescripcionPersonalizada() {
            return descripcionPersonalizada;
        }

        public void setDescripcionPersonalizada(String descripcionPersonalizada) {
            this.descripcionPersonalizada = descripcionPersonalizada;
        }

        public String getNombrePersonalizado() {
            return nombrePersonalizado;
        }

        public void setNombrePersonalizado(String nombrePersonalizado) {
            this.nombrePersonalizado = nombrePersonalizado;
        }
    }
}