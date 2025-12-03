// MantenimientoRoutes.java
package com.poleth.api.routes;

import com.poleth.api.controller.MantenimientoController;
import io.javalin.Javalin;

public class MantenimientoRoutes {
    private final MantenimientoController mantenimientoController;

    public MantenimientoRoutes(MantenimientoController mantenimientoController) {
        this.mantenimientoController = mantenimientoController;
    }

    public void configureRoutes(Javalin app) {
        // Rutas CRUD básicas
        app.post("/api/mantenimientos", mantenimientoController::createMantenimiento);
        app.get("/api/mantenimientos", mantenimientoController::getAllMantenimientos);
        app.get("/api/mantenimientos/{id}", mantenimientoController::getMantenimientoById);
        app.put("/api/mantenimientos/{id}", mantenimientoController::updateMantenimiento);
        app.delete("/api/mantenimientos/{id}", mantenimientoController::deleteMantenimiento);

        // Rutas de búsqueda
        app.get("/api/mantenimientos/cuarto/{idCuarto}", mantenimientoController::getMantenimientosByCuarto);
        app.get("/api/mantenimientos/estado/{estado}", mantenimientoController::getMantenimientosByEstado);
        app.get("/api/mantenimientos/pendientes", mantenimientoController::getMantenimientosPendientes);
        app.get("/api/mantenimientos/completados", mantenimientoController::getMantenimientosCompletados);
        app.get("/api/mantenimientos/fechas", mantenimientoController::getMantenimientosByFechaRange);

        // Rutas de gestión específicas
        app.patch("/api/mantenimientos/{id}/atender", mantenimientoController::atenderMantenimiento);
        app.patch("/api/mantenimientos/{id}/estado", mantenimientoController::actualizarEstadoMantenimiento);
    }
}