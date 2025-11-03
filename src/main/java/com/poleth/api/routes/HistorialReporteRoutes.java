// HistorialReporteRoutes.java
package com.poleth.api.routes;

import com.poleth.api.controller.HistorialReporteController;
import io.javalin.Javalin;

public class HistorialReporteRoutes {
    private final HistorialReporteController historialReporteController;

    public HistorialReporteRoutes(HistorialReporteController historialReporteController) {
        this.historialReporteController = historialReporteController;
    }

    public void configureRoutes(Javalin app) {
        
        // Rutas CRUD básicas
        app.post("/api/historial-reportes", historialReporteController::createHistorialReporte);
        app.get("/api/historial-reportes", historialReporteController::getAllHistorialReportes);
        app.get("/api/historial-reportes/{id}", historialReporteController::getHistorialReporteById);
        app.put("/api/historial-reportes/{id}", historialReporteController::updateHistorialReporte);
        app.delete("/api/historial-reportes/{id}", historialReporteController::deleteHistorialReporte);

        // Rutas específicas para HistorialReporte
        app.get("/api/historial-reportes/reporte/{idReporte}", historialReporteController::getHistorialesByReporte);
        app.get("/api/historial-reportes/buscar/tipo", historialReporteController::getHistorialesByTipo);
        app.get("/api/historial-reportes/buscar/usuario", historialReporteController::getHistorialesByUsuario);
        app.get("/api/historial-reportes/reporte/{idReporte}/ultimo", historialReporteController::getUltimoHistorialByReporte);
        app.get("/api/historial-reportes/ordenados/fecha", historialReporteController::getAllHistorialesOrderByFechaDesc);
        app.get("/api/historial-reportes/reporte/{idReporte}/ordenados", historialReporteController::getHistorialesByReporteOrderByFechaDesc);
        app.get("/api/historial-reportes/recientes", historialReporteController::getHistorialesRecientes);
        app.get("/api/historial-reportes/buscar/descripcion", historialReporteController::getHistorialesByDescripcion);
        app.get("/api/historial-reportes/buscar/nombre", historialReporteController::getHistorialesByNombre);

        // Rutas con relaciones
        app.get("/api/historial-reportes/relaciones/todos", historialReporteController::getHistorialesWithRelations);
        app.get("/api/historial-reportes/{id}/relaciones", historialReporteController::getHistorialByIdWithRelations);
        app.get("/api/historial-reportes/reporte/{idReporte}/relaciones", historialReporteController::getHistorialesByReporteWithRelations);

        // Rutas de gestión específicas
        app.post("/api/reportes/{idReporte}/registrar-historial", historialReporteController::registrarHistorialDesdeReporte);
        app.post("/api/reportes/{idReporte}/registrar-accion", historialReporteController::registrarHistorialConAccion);
        app.post("/api/reportes/{idReporte}/cambio-estado", historialReporteController::registrarCambioEstado);
        app.post("/api/reportes/{idReporte}/cierre", historialReporteController::registrarCierreReporte);

        // Rutas de verificación
        app.get("/api/historial-reportes/reporte/{idReporte}/existe", historialReporteController::existsByReporte);
        app.get("/api/historial-reportes/reporte/{idReporte}/reciente", historialReporteController::tieneHistorialReciente);

        // Rutas de conteo
        app.get("/api/historial-reportes/contar/total", historialReporteController::countHistorialReportes);
        app.get("/api/historial-reportes/reporte/{idReporte}/contar", historialReporteController::countHistorialesByReporte);
        app.get("/api/historial-reportes/contar/usuario", historialReporteController::countHistorialesByUsuario);

        // Rutas de línea de tiempo y actividad
        app.get("/api/historial-reportes/reporte/{idReporte}/linea-tiempo", historialReporteController::getLineaTiempoReporte);
        app.get("/api/historial-reportes/actividad/usuario", historialReporteController::getActividadRecienteUsuario);

        // Rutas de estadísticas y mantenimiento
        app.get("/api/historial-reportes/estadisticas", historialReporteController::getEstadisticasActividad);
        app.delete("/api/historial-reportes/limpiar/antiguos", historialReporteController::limpiarHistorialesAntiguos);
    }
}