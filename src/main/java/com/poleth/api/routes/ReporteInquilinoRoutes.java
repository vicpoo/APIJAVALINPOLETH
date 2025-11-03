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

        // Rutas de búsqueda y consultas específicas
        app.get("/api/reportes-inquilinos/inquilino/{idInquilino}", reporteInquilinoController::getReportesByInquilino);
        app.get("/api/reportes-inquilinos/cuarto/{idCuarto}", reporteInquilinoController::getReportesByCuarto);
        app.get("/api/reportes-inquilinos/tipo", reporteInquilinoController::getReportesByTipo);
        app.get("/api/reportes-inquilinos/estado", reporteInquilinoController::getReportesByEstado);
        app.get("/api/reportes-inquilinos/abiertos", reporteInquilinoController::getReportesAbiertos);
        app.get("/api/reportes-inquilinos/cerrados", reporteInquilinoController::getReportesCerrados);
        app.get("/api/reportes-inquilinos/inquilino/{idInquilino}/abiertos", reporteInquilinoController::getReportesAbiertosByInquilino);
        app.get("/api/reportes-inquilinos/cuarto/{idCuarto}/abiertos", reporteInquilinoController::getReportesAbiertosByCuarto);
        app.get("/api/reportes-inquilinos/buscar/descripcion", reporteInquilinoController::getReportesByDescripcion);
        app.get("/api/reportes-inquilinos/buscar/acciones", reporteInquilinoController::getReportesByAccionesTomadas);
        app.get("/api/reportes-inquilinos/buscar/nombre", reporteInquilinoController::getReportesByNombre);
        app.get("/api/reportes-inquilinos/recientes", reporteInquilinoController::getReportesRecientes);
        app.get("/api/reportes-inquilinos/ultimo-mes", reporteInquilinoController::getReportesUltimoMes);
        app.get("/api/reportes-inquilinos/filtro", reporteInquilinoController::getReportesByTipoAndEstado);
        
        // Rutas con relaciones
        app.get("/api/reportes-inquilinos/relaciones/todos", reporteInquilinoController::getReportesWithRelations);
        app.get("/api/reportes-inquilinos/{id}/relaciones", reporteInquilinoController::getReporteByIdWithRelations);

        // Rutas de gestión
        app.patch("/api/reportes-inquilinos/{id}/cerrar", reporteInquilinoController::cerrarReporte);
        app.patch("/api/reportes-inquilinos/{id}/estado", reporteInquilinoController::actualizarEstadoReporte);
        app.patch("/api/reportes-inquilinos/{id}/acciones", reporteInquilinoController::actualizarAccionesTomadas);

        // Rutas de verificación
        app.get("/api/reportes-inquilinos/inquilino/{idInquilino}/exists", reporteInquilinoController::existsByInquilino);
        app.get("/api/reportes-inquilinos/inquilino/{idInquilino}/abiertos/exists", reporteInquilinoController::existsAbiertosByInquilino);
        app.get("/api/reportes-inquilinos/{id}/abierto", reporteInquilinoController::isReporteAbierto);

        // Rutas de conteo
        app.get("/api/reportes-inquilinos/count", reporteInquilinoController::countReportesInquilinos);
        app.get("/api/reportes-inquilinos/abiertos/count", reporteInquilinoController::countReportesAbiertos);
        app.get("/api/reportes-inquilinos/cerrados/count", reporteInquilinoController::countReportesCerrados);
        app.get("/api/reportes-inquilinos/estado/count", reporteInquilinoController::countReportesByEstado);
        app.get("/api/reportes-inquilinos/tipo/count", reporteInquilinoController::countReportesByTipo);

        // Ruta de estadísticas
        app.get("/api/reportes-inquilinos/estadisticas", reporteInquilinoController::getEstadisticasReportes);
    }
}