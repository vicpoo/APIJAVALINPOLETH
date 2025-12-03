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

        // Ruta para crear historial desde reporte
        app.post("/api/historial-reportes/desde-reporte/{idReporte}",
                historialReporteController::createHistorialFromReporte);

        // Rutas de búsqueda específicas
        app.get("/api/historial-reportes/reporte/{idReporte}",
                historialReporteController::getHistorialesByReporte);
        app.get("/api/historial-reportes/buscar/tipo",
                historialReporteController::getHistorialesByTipo);
        app.get("/api/historial-reportes/buscar/usuario",
                historialReporteController::getHistorialesByUsuario);
        app.get("/api/historial-reportes/reporte/{idReporte}/ultimo",
                historialReporteController::getUltimoHistorialByReporte);
        app.get("/api/historial-reportes/ordenados/fecha",
                historialReporteController::getAllHistorialesOrderByFechaDesc);
        app.get("/api/historial-reportes/reporte/{idReporte}/ordenados",
                historialReporteController::getHistorialesByReporteOrderByFechaDesc);
        app.get("/api/historial-reportes/buscar/nombre",
                historialReporteController::getHistorialesByNombre);
        app.get("/api/historial-reportes/buscar/descripcion",
                historialReporteController::getHistorialesByDescripcion);

        // Rutas adicionales
        app.get("/api/historial-reportes/recientes",
                historialReporteController::getHistorialesRecientes);
        app.get("/api/historial-reportes/hoy",
                historialReporteController::getHistorialesDelDia);
        app.get("/api/historial-reportes/estadisticas",
                historialReporteController::getEstadisticasHistoriales);
    }
}