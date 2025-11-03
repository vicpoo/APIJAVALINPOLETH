// ContratoRoutes.java
package com.poleth.api.routes;

import com.poleth.api.controller.ContratoController;
import io.javalin.Javalin;

public class ContratoRoutes {
    private final ContratoController contratoController;

    public ContratoRoutes(ContratoController contratoController) {
        this.contratoController = contratoController;
    }

    public void configureRoutes(Javalin app) {
        // Rutas CRUD básicas
        app.post("/api/contratos", contratoController::createContrato);
        app.get("/api/contratos", contratoController::getAllContratos);
        app.get("/api/contratos/{id}", contratoController::getContratoById);
        app.put("/api/contratos/{id}", contratoController::updateContrato);
        app.delete("/api/contratos/{id}", contratoController::deleteContrato);

        // Rutas de búsqueda y consultas específicas
        app.get("/api/contratos/cuarto/{idCuarto}", contratoController::getContratosByCuarto);
        app.get("/api/contratos/inquilino/{idInquilino}", contratoController::getContratosByInquilino);
        app.get("/api/contratos/estado", contratoController::getContratosByEstado);
        app.get("/api/contratos/activos", contratoController::getContratosActivos);
        
        // Rutas con relaciones
        app.get("/api/contratos/relaciones/todos", contratoController::getContratosWithRelations);
        app.get("/api/contratos/{id}/relaciones", contratoController::getContratoByIdWithRelations);

        // Rutas de gestión
        app.patch("/api/contratos/{id}/finalizar", contratoController::finalizarContrato);
        app.patch("/api/contratos/{id}/estado", contratoController::actualizarEstadoContrato);
        app.patch("/api/contratos/{id}/monto", contratoController::actualizarMontoRenta);

        // Rutas de verificación
        app.get("/api/contratos/cuarto/{idCuarto}/activo/exists", contratoController::existsContratoActivoByCuarto);
        app.get("/api/contratos/inquilino/{idInquilino}/activo/exists", contratoController::existsContratoActivoByInquilino);
        app.get("/api/contratos/{id}/activo", contratoController::isContratoActivo);

        // Rutas de conteo
        app.get("/api/contratos/count", contratoController::countContratos);
        app.get("/api/contratos/activos/count", contratoController::countContratosActivos);
        app.get("/api/contratos/estado/count", contratoController::countContratosByEstado);
    }
}