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

        // Rutas de búsqueda y consultas específicas
        app.get("/api/catalogo-muebles/buscar/nombre", catalogoMuebleController::getCatalogoMuebleByNombre);
        app.get("/api/catalogo-muebles/buscar/nombre-contiene", catalogoMuebleController::getCatalogoMueblesByNombreContaining);
        app.get("/api/catalogo-muebles/buscar/descripcion", catalogoMuebleController::getCatalogoMueblesByDescripcionContaining);
        
        // Rutas de filtros especiales
        app.get("/api/catalogo-muebles/con-descripcion", catalogoMuebleController::getCatalogoMueblesWithDescripcion);
        app.get("/api/catalogo-muebles/sin-descripcion", catalogoMuebleController::getCatalogoMueblesWithoutDescripcion);
        app.get("/api/catalogo-muebles/ordenados", catalogoMuebleController::getCatalogoMueblesOrderByNombre);

        // Rutas de gestión de descripción
        app.patch("/api/catalogo-muebles/{id}/descripcion", catalogoMuebleController::actualizarDescripcionMueble);
        app.delete("/api/catalogo-muebles/{id}/descripcion", catalogoMuebleController::eliminarDescripcionMueble);

        // Rutas de conteo y verificación
        app.get("/api/catalogo-muebles/count", catalogoMuebleController::countCatalogoMuebles);
        app.get("/api/catalogo-muebles/exists", catalogoMuebleController::existsByNombreMueble);
    }
}