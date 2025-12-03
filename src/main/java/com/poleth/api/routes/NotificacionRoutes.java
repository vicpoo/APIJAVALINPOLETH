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

        // Ruta para marcar como leída
        app.patch("/api/notificaciones/{id}/leer", notificacionController::marcarComoLeida);

        // Rutas de búsqueda específicas
        app.get("/api/notificaciones/inquilino/{idInquilino}",
                notificacionController::getNotificacionesByInquilino);
        app.get("/api/notificaciones/contrato/{idContrato}",
                notificacionController::getNotificacionesByContrato);
        app.get("/api/notificaciones/fecha/{fecha}",
                notificacionController::getNotificacionesByFechaUtilizacion);

        // Rutas para notificaciones no leídas
        app.get("/api/notificaciones/inquilino/{idInquilino}/no-leidas",
                notificacionController::getNotificacionesNoLeidasByInquilino);
        app.get("/api/notificaciones/inquilino/{idInquilino}/contador-no-leidas",
                notificacionController::getContadorNoLeidasByInquilino);

        // Rutas adicionales
        app.get("/api/notificaciones/recientes",
                notificacionController::getNotificacionesRecientes);
        app.get("/api/notificaciones/{id}/existe",
                notificacionController::existeNotificacion);
    }
}