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
        // Rutas CRUD básicas - SOLO LAS QUE NECESITAS
        app.post("/api/reportes-inquilinos", reporteInquilinoController::createReporteInquilino);
        app.get("/api/reportes-inquilinos", reporteInquilinoController::getAllReportesInquilinos);
        app.get("/api/reportes-inquilinos/{id}", reporteInquilinoController::getReporteInquilinoById);
        app.put("/api/reportes-inquilinos/{id}", reporteInquilinoController::updateReporteInquilino);
        app.delete("/api/reportes-inquilinos/{id}", reporteInquilinoController::deleteReporteInquilino);

        // Rutas específicas que mencionaste
        app.get("/api/reportes-inquilinos/inquilino/{idInquilino}", reporteInquilinoController::getReportesByInquilino);
        app.get("/api/reportes-inquilinos/cuarto/{idCuarto}", reporteInquilinoController::getReportesByCuarto);

        // NUEVA RUTA: Estadísticas para gráfica de barras
        app.get("/api/reportes-inquilinos/estadisticas/tipos", reporteInquilinoController::getEstadisticasTiposReportes);
    }
}