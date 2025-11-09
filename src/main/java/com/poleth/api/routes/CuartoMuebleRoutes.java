// CuartoMuebleRoutes.java
package com.poleth.api.routes;

import com.poleth.api.controller.CuartoMuebleController;
import io.javalin.Javalin;

public class CuartoMuebleRoutes {
    private final CuartoMuebleController cuartoMuebleController;

    public CuartoMuebleRoutes(CuartoMuebleController cuartoMuebleController) {
        this.cuartoMuebleController = cuartoMuebleController;
    }

    public void configureRoutes(Javalin app) {
        // Rutas CRUD básicas
        app.post("/api/cuarto-muebles", cuartoMuebleController::createCuartoMueble);
        app.get("/api/cuarto-muebles", cuartoMuebleController::getAllCuartoMuebles);
        app.get("/api/cuarto-muebles/{id}", cuartoMuebleController::getCuartoMuebleById);
        app.put("/api/cuarto-muebles/{id}", cuartoMuebleController::updateCuartoMueble);
        app.delete("/api/cuarto-muebles/{id}", cuartoMuebleController::deleteCuartoMueble);

        // Rutas de búsqueda específicas
        app.get("/api/cuarto-muebles/cuarto/{idCuarto}", cuartoMuebleController::getCuartoMueblesByCuarto);
        app.get("/api/cuarto-muebles/catalogo/{idCatalogoMueble}", cuartoMuebleController::getCuartoMueblesByCatalogo);
        app.get("/api/cuarto-muebles/cuarto/{idCuarto}/catalogo/{idCatalogoMueble}",
                cuartoMuebleController::getCuartoMuebleByCuartoAndCatalogo);

        // Rutas de stock
        app.get("/api/cuarto-muebles/stock/con-stock", cuartoMuebleController::getCuartoMueblesWithStock);
        app.get("/api/cuarto-muebles/stock/sin-stock", cuartoMuebleController::getCuartoMueblesWithoutStock);

        // Ruta para actualizar cantidad específica
        app.put("/api/cuarto-muebles/{id}/cantidad", cuartoMuebleController::updateCantidad);
    }
}