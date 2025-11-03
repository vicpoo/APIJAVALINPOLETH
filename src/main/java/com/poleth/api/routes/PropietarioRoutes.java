// PropietarioRoutes.java
package com.poleth.api.routes;

import com.poleth.api.controller.PropietarioController;
import io.javalin.Javalin;

public class PropietarioRoutes {
    private final PropietarioController propietarioController;

    public PropietarioRoutes(PropietarioController propietarioController) {
        this.propietarioController = propietarioController;
    }

    public void configureRoutes(Javalin app) {
        // Rutas CRUD básicas
        app.post("/api/propietarios", propietarioController::createPropietario);
        app.get("/api/propietarios", propietarioController::getAllPropietarios);
        app.get("/api/propietarios/{id}", propietarioController::getPropietarioById);
        app.put("/api/propietarios/{id}", propietarioController::updatePropietario);
        app.delete("/api/propietarios/{id}", propietarioController::deletePropietario);

        // Rutas de búsqueda específicas
        app.get("/api/propietarios/gmail/{gmail}", propietarioController::getPropietarioByGmail);
        app.get("/api/propietarios/buscar/nombre", propietarioController::getPropietariosByNombre);
        app.get("/api/propietarios/buscar", propietarioController::searchPropietarios);

        // Rutas de verificación y utilidad
        app.get("/api/propietarios/count", propietarioController::countPropietarios);
        app.get("/api/propietarios/exists/gmail/{gmail}", propietarioController::existsByGmail);
        app.get("/api/propietarios/exists/nombre/{nombre}", propietarioController::existsByNombre);
    }
}