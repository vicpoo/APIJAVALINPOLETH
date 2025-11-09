// CuartoRoutes.java
package com.poleth.api.routes;

import com.poleth.api.controller.CuartoController;
import io.javalin.Javalin;

public class CuartoRoutes {
    private final CuartoController cuartoController;

    public CuartoRoutes(CuartoController cuartoController) {
        this.cuartoController = cuartoController;
    }

    public void configureRoutes(Javalin app) {
        // Rutas CRUD básicas
        app.post("/api/cuartos", cuartoController::createCuarto);
        app.get("/api/cuartos", cuartoController::getAllCuartos);
        app.get("/api/cuartos/{id}", cuartoController::getCuartoById);
        app.put("/api/cuartos/{id}", cuartoController::updateCuarto);
        app.delete("/api/cuartos/{id}", cuartoController::deleteCuarto);

        // Rutas específicas según tus requerimientos
        app.get("/api/cuartos/propietario/{idPropietario}", cuartoController::getCuartosByPropietario);

        // Rutas de gestión
        app.patch("/api/cuartos/{id}/estado", cuartoController::cambiarEstadoCuarto);
        app.patch("/api/cuartos/{id}/precio", cuartoController::actualizarPrecioCuarto);
    }
}