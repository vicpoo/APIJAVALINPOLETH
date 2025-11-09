// InvitadoRoutes.java
package com.poleth.api.routes;

import com.poleth.api.controller.InvitadoController;
import io.javalin.Javalin;

public class InvitadoRoutes {
    private final InvitadoController invitadoController;

    public InvitadoRoutes(InvitadoController invitadoController) {
        this.invitadoController = invitadoController;
    }

    public void configureRoutes(Javalin app) {
        // Rutas CRUD básicas
        app.post("/api/invitados", invitadoController::createInvitado);
        app.get("/api/invitados", invitadoController::getAllInvitados);
        app.get("/api/invitados/{id}", invitadoController::getInvitadoById);
        app.put("/api/invitados/{id}", invitadoController::updateInvitado);
        app.delete("/api/invitados/{id}", invitadoController::deleteInvitado);

        // Ruta específica para obtener invitados por cuarto
        app.get("/api/invitados/cuarto/{idCuarto}", invitadoController::getInvitadosByCuarto);
    }
}