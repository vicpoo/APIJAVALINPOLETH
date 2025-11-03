// ImagenCuartoPublicaRoutes.java
package com.poleth.api.routes;

import com.poleth.api.controller.ImagenCuartoPublicaController;
import io.javalin.Javalin;

public class ImagenCuartoPublicaRoutes {
    private final ImagenCuartoPublicaController imagenController;

    public ImagenCuartoPublicaRoutes(ImagenCuartoPublicaController imagenController) {
        this.imagenController = imagenController;
    }

    public void configureRoutes(Javalin app) {
        // Ruta para servir archivos de imágenes (debe estar antes de las otras rutas)
        app.get("/api/images/{fileName}", imagenController::serveImagen);

        // Rutas para subida y gestión de imágenes
        app.post("/api/imagenes/upload", imagenController::uploadImagen);
        
        // Rutas CRUD básicas
        app.get("/api/imagenes", imagenController::getAllImagenes);
        app.get("/api/imagenes/{id}", imagenController::getImagenById);
        app.put("/api/imagenes/{id}", imagenController::updateImagen);
        app.delete("/api/imagenes/{id}", imagenController::deleteImagen);

        // Rutas de búsqueda por cuarto
        app.get("/api/imagenes/cuarto/{idCuarto}", imagenController::getImagenesByCuarto);
        app.get("/api/imagenes/cuarto/{idCuarto}/publicas", imagenController::getImagenesPublicasByCuarto);
        app.get("/api/imagenes/cuarto/{idCuarto}/privadas", imagenController::getImagenesPrivadasByCuarto);

        // Rutas de operaciones por lote
        app.delete("/api/imagenes/cuarto/{idCuarto}", imagenController::deleteImagenesByCuarto);
        app.put("/api/imagenes/cuarto/{idCuarto}/visibilidad", imagenController::updateVisibilidadByCuarto);

        // Rutas de verificación y utilidad
        app.get("/api/imagenes/exists/{url}", imagenController::existsByUrl);
        app.get("/api/imagenes/stats", imagenController::getStats);
        app.get("/api/imagenes/stats/cuarto/{idCuarto}", imagenController::getStatsByCuarto);
        app.get("/api/imagenes/count", imagenController::countImagenes);
    }
}