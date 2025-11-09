// NotificacionRoutes.java
package com.poleth.api.routes;

import com.poleth.api.controller.NotificacionController;
import io.javalin.Javalin;

public class NotificacionRoutes {
    private final NotificacionController notificacionController;

    public NotificacionRoutes(NotificacionController notificacionController) {
        this.notificacionController = notificacionController;
    }

    public void configureRoutes(Javalin app) {
        // Rutas CRUD básicas
        app.post("/api/notificaciones", notificacionController::createNotificacion);
        app.get("/api/notificaciones", notificacionController::getAllNotificaciones);
        app.get("/api/notificaciones/{id}", notificacionController::getNotificacionById);
        app.put("/api/notificaciones/{id}", notificacionController::updateNotificacion);
        app.delete("/api/notificaciones/{id}", notificacionController::deleteNotificacion);

        // Rutas de búsqueda específicas
        app.get("/api/notificaciones/inquilino/{idInquilino}", notificacionController::getNotificacionesByInquilino);
        app.get("/api/notificaciones/fecha/{fecha}", notificacionController::getNotificacionesByFechaUtilizacion);
    }
}