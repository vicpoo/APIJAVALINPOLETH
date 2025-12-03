// RolRoutes.java
package com.poleth.api.routes;

import com.poleth.api.controller.RolController;
import io.javalin.Javalin;

public class RolRoutes {
    private final RolController rolController;

    public RolRoutes(RolController rolController) {
        this.rolController = rolController;
    }

    public void configureRoutes(Javalin app) {
        // Rutas CRUD básicas
        app.post("/api/roles", rolController::createRol);
        app.get("/api/roles", rolController::getAllRoles);
        app.get("/api/roles/{id}", rolController::getRolById);
        app.put("/api/roles/{id}", rolController::updateRol);
        app.delete("/api/roles/{id}", rolController::deleteRol);

        // Rutas adicionales para funcionalidades específicas
        app.get("/api/roles/titulo/{titulo}", rolController::getRolByTitulo);
        app.get("/api/roles/exists/{titulo}", rolController::existsByTitulo);
    }
}