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

        // Rutas de búsqueda por cuarto
        app.get("/api/cuarto-muebles/cuarto/{idCuarto}", cuartoMuebleController::getCuartoMueblesByCuarto);
        
        // Rutas de búsqueda por catálogo
        app.get("/api/cuarto-muebles/catalogo/{idCatalogoMueble}", cuartoMuebleController::getCuartoMueblesByCatalogo);
        
        // Rutas de búsqueda por combinación
        app.get("/api/cuarto-muebles/cuarto/{idCuarto}/catalogo/{idCatalogoMueble}", 
                cuartoMuebleController::getCuartoMuebleByCuartoAndCatalogo);

        // Rutas de búsqueda por stock
        app.get("/api/cuarto-muebles/stock/con-stock", cuartoMuebleController::getCuartoMueblesWithStock);
        app.get("/api/cuarto-muebles/stock/sin-stock", cuartoMuebleController::getCuartoMueblesWithoutStock);
        app.get("/api/cuarto-muebles/cuarto/{idCuarto}/con-stock", 
                cuartoMuebleController::getCuartoMueblesWithStockByCuarto);
        app.get("/api/cuarto-muebles/cuarto/{idCuarto}/sin-stock", 
                cuartoMuebleController::getCuartoMueblesWithoutStockByCuarto);

        // Rutas de operaciones de inventario
        app.put("/api/cuarto-muebles/{id}/cantidad", cuartoMuebleController::updateCantidad);
        app.put("/api/cuarto-muebles/{id}/incrementar", cuartoMuebleController::incrementarCantidad);
        app.put("/api/cuarto-muebles/{id}/decrementar", cuartoMuebleController::decrementarCantidad);
        app.post("/api/cuarto-muebles/agregar", cuartoMuebleController::agregarMuebleACuarto);

        // Rutas de operaciones por lote
        app.delete("/api/cuarto-muebles/cuarto/{idCuarto}", cuartoMuebleController::deleteCuartoMueblesByCuarto);

        // Rutas de verificación
        app.get("/api/cuarto-muebles/exists/cuarto/{idCuarto}/catalogo/{idCatalogoMueble}", 
                cuartoMuebleController::existsByCuartoAndCatalogo);

        // Rutas de utilidad
        app.get("/api/cuarto-muebles/stats", cuartoMuebleController::getStats);
        app.get("/api/cuarto-muebles/stats/cuarto/{idCuarto}", cuartoMuebleController::getStatsByCuarto);
        app.get("/api/cuarto-muebles/count", cuartoMuebleController::countCuartoMuebles);
    }
}