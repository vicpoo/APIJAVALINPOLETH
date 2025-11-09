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

        // Rutas de búsqueda específicas
        app.get("/api/contratos/cuarto/{idCuarto}", contratoController::getContratosByCuarto);
        app.get("/api/contratos/inquilino/{idInquilino}", contratoController::getContratosByInquilino);
    }
}