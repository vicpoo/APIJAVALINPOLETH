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

        // Rutas de búsqueda y consultas específicas
        app.get("/api/invitados/email", invitadoController::getInvitadoByEmail);
        app.get("/api/invitados/nombre", invitadoController::getInvitadosByNombre);
        app.get("/api/invitados/count", invitadoController::countInvitados);

        // Rutas para gestión de cuartos
        app.get("/api/invitados/cuarto/{idCuarto}", invitadoController::getInvitadosByCuarto);
        app.get("/api/invitados/sin-cuarto", invitadoController::getInvitadosWithoutCuarto);
        app.post("/api/invitados/{id}/asignar-cuarto", invitadoController::asignarCuarto);
        app.post("/api/invitados/{id}/remover-cuarto", invitadoController::removerCuarto);

        // Rutas para gestión de imágenes
        app.post("/api/invitados/{id}/asignar-imagen", invitadoController::asignarImagen);
        app.post("/api/invitados/{id}/remover-imagen", invitadoController::removerImagen);
    }
}