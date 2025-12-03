// ReporteInquilinoRoutes.java
package com.poleth.api.routes;

import com.poleth.api.controller.ReporteInquilinoController;
import io.javalin.Javalin;

public class ReporteInquilinoRoutes {
    private final ReporteInquilinoController reporteInquilinoController;

    public ReporteInquilinoRoutes(ReporteInquilinoController reporteInquilinoController) {
        this.reporteInquilinoController = reporteInquilinoController;
    }

    public void configureRoutes(Javalin app) {
        // Rutas CRUD básicas
        app.post("/api/reportes-inquilinos", reporteInquilinoController::createReporteInquilino);
        app.get("/api/reportes-inquilinos", reporteInquilinoController::getAllReportesInquilinos);
        app.get("/api/reportes-inquilinos/{id}", reporteInquilinoController::getReporteInquilinoById);
        app.put("/api/reportes-inquilinos/{id}", reporteInquilinoController::updateReporteInquilino);
        app.delete("/api/reportes-inquilinos/{id}", reporteInquilinoController::deleteReporteInquilino);

        // Rutas de gestión específicas
        app.patch("/api/reportes-inquilinos/{id}/cerrar", reporteInquilinoController::cerrarReporte);
        app.patch("/api/reportes-inquilinos/{id}/estado", reporteInquilinoController::actualizarEstadoReporte);

        // Rutas de búsqueda
        app.get("/api/reportes-inquilinos/inquilino/{idInquilino}", reporteInquilinoController::getReportesByInquilino);
        app.get("/api/reportes-inquilinos/cuarto/{idCuarto}", reporteInquilinoController::getReportesByCuarto);
        app.get("/api/reportes-inquilinos/estado/{estado}", reporteInquilinoController::getReportesByEstado);
        app.get("/api/reportes-inquilinos/abiertos", reporteInquilinoController::getReportesAbiertos);
        app.get("/api/reportes-inquilinos/cerrados", reporteInquilinoController::getReportesCerrados);
        app.get("/api/reportes-inquilinos/fechas", reporteInquilinoController::getReportesByFechaRange);
        app.get("/api/reportes-inquilinos/buscar", reporteInquilinoController::buscarReportesPorTexto);

        // Rutas de estadísticas
        app.get("/api/reportes-inquilinos/estadisticas/tipos", reporteInquilinoController::getEstadisticasTiposReportes);
        app.get("/api/reportes-inquilinos/estadisticas/completas", reporteInquilinoController::getEstadisticasCompletas);
    }
}