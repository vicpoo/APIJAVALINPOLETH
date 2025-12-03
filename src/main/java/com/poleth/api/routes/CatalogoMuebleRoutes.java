// CatalogoMuebleRoutes.java
package com.poleth.api.routes;

import com.poleth.api.controller.CatalogoMuebleController;
import io.javalin.Javalin;

public class CatalogoMuebleRoutes {
    private final CatalogoMuebleController catalogoMuebleController;

    public CatalogoMuebleRoutes(CatalogoMuebleController catalogoMuebleController) {
        this.catalogoMuebleController = catalogoMuebleController;
    }

    public void configureRoutes(Javalin app) {
        // Rutas CRUD básicas
        app.post("/api/catalogo-muebles", catalogoMuebleController::createCatalogoMueble);
        app.get("/api/catalogo-muebles", catalogoMuebleController::getAllCatalogoMuebles);
        app.get("/api/catalogo-muebles/{id}", catalogoMuebleController::getCatalogoMuebleById);
        app.put("/api/catalogo-muebles/{id}", catalogoMuebleController::updateCatalogoMueble);
        app.delete("/api/catalogo-muebles/{id}", catalogoMuebleController::deleteCatalogoMueble);

        // Rutas de búsqueda específicas
        app.get("/api/catalogo-muebles/buscar/nombre", catalogoMuebleController::getCatalogoMuebleByNombre);
        app.get("/api/catalogo-muebles/buscar/nombre-contiene", catalogoMuebleController::getCatalogoMueblesByNombreContaining);
        app.get("/api/catalogo-muebles/buscar/descripcion", catalogoMuebleController::getCatalogoMueblesByDescripcionContaining);

        // Ruta para eliminar solo la descripción
        app.delete("/api/catalogo-muebles/{id}/descripcion", catalogoMuebleController::eliminarDescripcionMueble);

        // Rutas para estado
        app.patch("/api/catalogo-muebles/{id}/estado", catalogoMuebleController::cambiarEstadoMueble);
        app.get("/api/catalogo-muebles/estado/{estado}", catalogoMuebleController::getCatalogoMueblesByEstado);
        app.get("/api/catalogo-muebles/activos", catalogoMuebleController::getCatalogoMueblesActivos);
    }
}