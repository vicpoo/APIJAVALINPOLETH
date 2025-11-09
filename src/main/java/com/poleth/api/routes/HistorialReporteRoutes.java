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

        // Rutas específicas que mencionaste
        app.get("/api/historial-reportes/reporte/{idReporte}", historialReporteController::getHistorialesByReporte);
        app.get("/api/historial-reportes/buscar/tipo", historialReporteController::getHistorialesByTipo);
        app.get("/api/historial-reportes/buscar/usuario", historialReporteController::getHistorialesByUsuario);
        app.get("/api/historial-reportes/reporte/{idReporte}/ultimo", historialReporteController::getUltimoHistorialByReporte);
        app.get("/api/historial-reportes/ordenados/fecha", historialReporteController::getAllHistorialesOrderByFechaDesc);
        app.get("/api/historial-reportes/reporte/{idReporte}/ordenados", historialReporteController::getHistorialesByReporteOrderByFechaDesc);
        app.get("/api/historial-reportes/buscar/nombre", historialReporteController::getHistorialesByNombre);
    }
}