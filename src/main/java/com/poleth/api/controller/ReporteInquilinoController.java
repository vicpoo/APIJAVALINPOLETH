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

public class ReporteInquilinoController {
    private final ReporteInquilinoService reporteInquilinoService;
    private final ObjectMapper objectMapper;

    public ReporteInquilinoController(ReporteInquilinoService reporteInquilinoService) {
        this.reporteInquilinoService = reporteInquilinoService;
        this.objectMapper = createConfiguredObjectMapper();
    }

    private ObjectMapper createConfiguredObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Registrar módulo para Java 8 Date/Time
        mapper.registerModule(new JavaTimeModule());
        // Deshabilitar serialización como timestamp
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // Ignorar propiedades nulas
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // Configurar para ignorar propiedades Hibernate
        mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return mapper;
    }

    public void createReporteInquilino(Context ctx) {
        try {
            ReporteInquilino reporteInquilino = objectMapper.readValue(ctx.body(), ReporteInquilino.class);

            // Usar el método createReporteInquilino que incluye validaciones
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
            // Crear respuestas seguras sin problemas de lazy loading
            List<ReporteInquilinoResponse> safeResponses = reportesInquilinos.stream()
                    .map(this::createSafeResponse)
                    .toList();
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

            // Usar el método updateReporteInquilino que incluye validaciones
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
            
            // Verificar si el reporte existe antes de eliminar
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

    // Métodos específicos para ReporteInquilino

    public void getReportesByInquilino(Context ctx) {
        try {
            Integer idInquilino = Integer.parseInt(ctx.pathParam("idInquilino"));
            List<ReporteInquilino> reportes = reporteInquilinoService.getReportesByInquilino(idInquilino);
            List<ReporteInquilinoResponse> safeResponses = reportes.stream()
                    .map(this::createSafeResponse)
                    .toList();
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
                    .toList();
            ctx.json(safeResponses);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener reportes del cuarto: " + e.getMessage());
        }
    }

    public void getReportesByTipo(Context ctx) {
        try {
            String tipo = ctx.queryParam("tipo");
            if (tipo == null || tipo.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro tipo es requerido");
                return;
            }

            List<ReporteInquilino> reportes = reporteInquilinoService.getReportesByTipo(tipo);
            List<ReporteInquilinoResponse> safeResponses = reportes.stream()
                    .map(this::createSafeResponse)
                    .toList();
            ctx.json(safeResponses);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar reportes por tipo: " + e.getMessage());
        }
    }

    public void getReportesByEstado(Context ctx) {
        try {
            String estado = ctx.queryParam("estado");
            if (estado == null || estado.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro estado es requerido");
                return;
            }

            List<ReporteInquilino> reportes = reporteInquilinoService.getReportesByEstado(estado);
            List<ReporteInquilinoResponse> safeResponses = reportes.stream()
                    .map(this::createSafeResponse)
                    .toList();
            ctx.json(safeResponses);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar reportes por estado: " + e.getMessage());
        }
    }

    public void getReportesAbiertos(Context ctx) {
        try {
            List<ReporteInquilino> reportes = reporteInquilinoService.getReportesAbiertos();
            List<ReporteInquilinoResponse> safeResponses = reportes.stream()
                    .map(this::createSafeResponse)
                    .toList();
            ctx.json(safeResponses);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener reportes abiertos: " + e.getMessage());
        }
    }

    public void getReportesCerrados(Context ctx) {
        try {
            List<ReporteInquilino> reportes = reporteInquilinoService.getReportesCerrados();
            List<ReporteInquilinoResponse> safeResponses = reportes.stream()
                    .map(this::createSafeResponse)
                    .toList();
            ctx.json(safeResponses);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener reportes cerrados: " + e.getMessage());
        }
    }

    public void getReportesAbiertosByInquilino(Context ctx) {
        try {
            Integer idInquilino = Integer.parseInt(ctx.pathParam("idInquilino"));
            List<ReporteInquilino> reportes = reporteInquilinoService.getReportesAbiertosByInquilino(idInquilino);
            List<ReporteInquilinoResponse> safeResponses = reportes.stream()
                    .map(this::createSafeResponse)
                    .toList();
            ctx.json(safeResponses);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de inquilino inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener reportes abiertos del inquilino: " + e.getMessage());
        }
    }

    public void getReportesAbiertosByCuarto(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("idCuarto"));
            List<ReporteInquilino> reportes = reporteInquilinoService.getReportesAbiertosByCuarto(idCuarto);
            List<ReporteInquilinoResponse> safeResponses = reportes.stream()
                    .map(this::createSafeResponse)
                    .toList();
            ctx.json(safeResponses);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener reportes abiertos del cuarto: " + e.getMessage());
        }
    }

    public void getReportesByDescripcion(Context ctx) {
        try {
            String texto = ctx.queryParam("texto");
            if (texto == null || texto.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro texto es requerido");
                return;
            }

            List<ReporteInquilino> reportes = reporteInquilinoService.getReportesByDescripcionContaining(texto);
            List<ReporteInquilinoResponse> safeResponses = reportes.stream()
                    .map(this::createSafeResponse)
                    .toList();
            ctx.json(safeResponses);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar reportes por descripción: " + e.getMessage());
        }
    }

    public void getReportesByAccionesTomadas(Context ctx) {
        try {
            String texto = ctx.queryParam("texto");
            if (texto == null || texto.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro texto es requerido");
                return;
            }

            List<ReporteInquilino> reportes = reporteInquilinoService.getReportesByAccionesTomadasContaining(texto);
            List<ReporteInquilinoResponse> safeResponses = reportes.stream()
                    .map(this::createSafeResponse)
                    .toList();
            ctx.json(safeResponses);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar reportes por acciones tomadas: " + e.getMessage());
        }
    }

    public void getReportesByNombre(Context ctx) {
        try {
            String texto = ctx.queryParam("texto");
            if (texto == null || texto.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro texto es requerido");
                return;
            }

            List<ReporteInquilino> reportes = reporteInquilinoService.getReportesByNombreContaining(texto);
            List<ReporteInquilinoResponse> safeResponses = reportes.stream()
                    .map(this::createSafeResponse)
                    .toList();
            ctx.json(safeResponses);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar reportes por nombre: " + e.getMessage());
        }
    }

    public void getReportesRecientes(Context ctx) {
        try {
            List<ReporteInquilino> reportes = reporteInquilinoService.getReportesRecientes();
            List<ReporteInquilinoResponse> safeResponses = reportes.stream()
                    .map(this::createSafeResponse)
                    .toList();
            ctx.json(safeResponses);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener reportes recientes: " + e.getMessage());
        }
    }

    public void getReportesUltimoMes(Context ctx) {
        try {
            List<ReporteInquilino> reportes = reporteInquilinoService.getReportesUltimoMes();
            List<ReporteInquilinoResponse> safeResponses = reportes.stream()
                    .map(this::createSafeResponse)
                    .toList();
            ctx.json(safeResponses);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener reportes del último mes: " + e.getMessage());
        }
    }

    public void getReportesByTipoAndEstado(Context ctx) {
        try {
            String tipo = ctx.queryParam("tipo");
            String estado = ctx.queryParam("estado");
            
            if (tipo == null || tipo.isEmpty() || estado == null || estado.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("Los parámetros tipo y estado son requeridos");
                return;
            }

            List<ReporteInquilino> reportes = reporteInquilinoService.getReportesByTipoAndEstado(tipo, estado);
            List<ReporteInquilinoResponse> safeResponses = reportes.stream()
                    .map(this::createSafeResponse)
                    .toList();
            ctx.json(safeResponses);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar reportes por tipo y estado: " + e.getMessage());
        }
    }

    public void getReportesWithRelations(Context ctx) {
        try {
            List<ReporteInquilino> reportes = reporteInquilinoService.getReportesWithRelations();
            List<ReporteInquilinoResponse> safeResponses = reportes.stream()
                    .map(this::createSafeResponse)
                    .toList();
            ctx.json(safeResponses);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener reportes con relaciones: " + e.getMessage());
        }
    }

    public void getReporteByIdWithRelations(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Optional<ReporteInquilino> reporte = reporteInquilinoService.getReporteByIdWithRelations(id);

            if (reporte.isPresent()) {
                ctx.json(createSafeResponse(reporte.get()));
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Reporte no encontrado con ID: " + id);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de reporte inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el reporte con relaciones: " + e.getMessage());
        }
    }

    // Métodos de gestión

    public void cerrarReporte(Context ctx) {
        try {
            Integer idReporte = Integer.parseInt(ctx.pathParam("id"));
            
            // Usar el ObjectMapper configurado para leer el JSON
            CerrarReporteRequest request = objectMapper.readValue(ctx.body(), CerrarReporteRequest.class);
            
            ReporteInquilino reporte = reporteInquilinoService.cerrarReporte(
                idReporte, 
                request.fechaCierre, 
                request.accionesTomadas, 
                request.estadoFinal
            );
            ctx.json(createSafeResponse(reporte));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de reporte inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al cerrar reporte: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al cerrar reporte: " + e.getMessage());
        }
    }

    public void actualizarEstadoReporte(Context ctx) {
        try {
            Integer idReporte = Integer.parseInt(ctx.pathParam("id"));
            
            // Usar el ObjectMapper configurado para leer el JSON
            ActualizarEstadoRequest request = objectMapper.readValue(ctx.body(), ActualizarEstadoRequest.class);

            ReporteInquilino reporte = reporteInquilinoService.actualizarEstadoReporte(idReporte, request.estado);
            ctx.json(createSafeResponse(reporte));
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

    public void actualizarAccionesTomadas(Context ctx) {
        try {
            Integer idReporte = Integer.parseInt(ctx.pathParam("id"));
            
            // Usar el ObjectMapper configurado para leer el JSON
            ActualizarAccionesRequest request = objectMapper.readValue(ctx.body(), ActualizarAccionesRequest.class);

            ReporteInquilino reporte = reporteInquilinoService.actualizarAccionesTomadas(idReporte, request.acciones);
            ctx.json(createSafeResponse(reporte));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de reporte inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al actualizar acciones: " + e.getMessage());
        }
    }

    // Métodos de verificación

    public void existsByInquilino(Context ctx) {
        try {
            Integer idInquilino = Integer.parseInt(ctx.pathParam("idInquilino"));
            boolean exists = reporteInquilinoService.existsByInquilino(idInquilino);
            ctx.json(exists);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de inquilino inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar reportes del inquilino: " + e.getMessage());
        }
    }

    public void existsAbiertosByInquilino(Context ctx) {
        try {
            Integer idInquilino = Integer.parseInt(ctx.pathParam("idInquilino"));
            boolean exists = reporteInquilinoService.existsAbiertosByInquilino(idInquilino);
            ctx.json(exists);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de inquilino inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar reportes abiertos del inquilino: " + e.getMessage());
        }
    }

    public void isReporteAbierto(Context ctx) {
        try {
            Integer idReporte = Integer.parseInt(ctx.pathParam("id"));
            boolean isAbierto = reporteInquilinoService.isReporteAbierto(idReporte);
            ctx.json(isAbierto);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de reporte inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar estado del reporte: " + e.getMessage());
        }
    }

    // Métodos de conteo

    public void countReportesInquilinos(Context ctx) {
        try {
            Long count = reporteInquilinoService.countReportesInquilinos();
            ctx.json(count);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar reportes: " + e.getMessage());
        }
    }

    public void countReportesAbiertos(Context ctx) {
        try {
            Long count = reporteInquilinoService.countReportesAbiertos();
            ctx.json(count);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar reportes abiertos: " + e.getMessage());
        }
    }

    public void countReportesCerrados(Context ctx) {
        try {
            Long count = reporteInquilinoService.countReportesCerrados();
            ctx.json(count);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar reportes cerrados: " + e.getMessage());
        }
    }

    public void countReportesByEstado(Context ctx) {
        try {
            String estado = ctx.queryParam("estado");
            if (estado == null || estado.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro estado es requerido");
                return;
            }

            Long count = reporteInquilinoService.countReportesByEstado(estado);
            ctx.json(count);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar reportes por estado: " + e.getMessage());
        }
    }

    public void countReportesByTipo(Context ctx) {
        try {
            String tipo = ctx.queryParam("tipo");
            if (tipo == null || tipo.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro tipo es requerido");
                return;
            }

            Long count = reporteInquilinoService.countReportesByTipo(tipo);
            ctx.json(count);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar reportes por tipo: " + e.getMessage());
        }
    }

    // Método para obtener estadísticas
    public void getEstadisticasReportes(Context ctx) {
        try {
            String estadisticas = reporteInquilinoService.obtenerEstadisticasReportes();
            ctx.json(estadisticas);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener estadísticas de reportes: " + e.getMessage());
        }
    }

    // Método auxiliar para crear respuestas seguras sin problemas de lazy loading
    private ReporteInquilinoResponse createSafeResponse(ReporteInquilino reporte) {
        return new ReporteInquilinoResponse(reporte);
    }

    // Clases internas para deserialización
    private static class CerrarReporteRequest {
        public LocalDate fechaCierre;
        public String accionesTomadas;
        public String estadoFinal;
    }

    private static class ActualizarEstadoRequest {
        public String estado;
    }

    private static class ActualizarAccionesRequest {
        public String acciones;
    }

    // Clase DTO para respuestas seguras
    public static class ReporteInquilinoResponse {
        public Integer idReporte;
        public Integer idInquilino;
        public String nombre;
        public String tipo;
        public String descripcion;
        public LocalDate fecha;
        public Integer idCuarto;
        public String estadoReporte;
        public LocalDate fechaCierre;
        public String accionesTomadas;
        public String nombreInquilino;
        public String nombreCuarto; // Cambiado de numeroCuarto a nombreCuarto

        public ReporteInquilinoResponse(ReporteInquilino reporte) {
            this.idReporte = reporte.getIdReporte();
            this.idInquilino = reporte.getIdInquilino();
            this.nombre = reporte.getNombre();
            this.tipo = reporte.getTipo();
            this.descripcion = reporte.getDescripcion();
            this.fecha = reporte.getFecha();
            this.idCuarto = reporte.getIdCuarto();
            this.estadoReporte = reporte.getEstadoReporte();
            this.fechaCierre = reporte.getFechaCierre();
            this.accionesTomadas = reporte.getAccionesTomadas();
            
            // Manejar relaciones de forma segura
            if (reporte.getInquilino() != null) {
                this.nombreInquilino = reporte.getInquilino().getNombreInquilino();
            }
            if (reporte.getCuarto() != null) {
                // Usar getNombreCuarto() que es el método correcto según tu clase Cuarto
                this.nombreCuarto = reporte.getCuarto().getNombreCuarto(); // CORREGIDO AQUÍ
            }
        }
    }
}