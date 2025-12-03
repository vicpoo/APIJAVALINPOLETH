// PagoRoutes.java
package com.poleth.api.routes;

import com.poleth.api.controller.PagoController;
import io.javalin.Javalin;

public class PagoRoutes {
    private final PagoController pagoController;

    public PagoRoutes(PagoController pagoController) {
        this.pagoController = pagoController;
    }

    public void configureRoutes(Javalin app) {
        // Rutas CRUD básicas
        app.post("/api/pagos", pagoController::createPago);
        app.get("/api/pagos", pagoController::getAllPagos);
        app.get("/api/pagos/{id}", pagoController::getPagoById);
        app.put("/api/pagos/{id}", pagoController::updatePago);
        app.delete("/api/pagos/{id}", pagoController::deletePago);

        // Rutas de búsqueda específicas
        app.get("/api/pagos/contrato/{idContrato}", pagoController::getPagosByContrato);
        app.get("/api/pagos/inquilino/{idInquilino}", pagoController::getPagosByInquilino);
        app.get("/api/pagos/fecha/{fecha}", pagoController::getPagosByFecha);
        app.get("/api/pagos/estado/{estado}", pagoController::getPagosByEstado);
        app.get("/api/pagos/metodo/{metodoPago}", pagoController::getPagosByMetodoPago);

        // Rutas de búsqueda por monto
        app.get("/api/pagos/monto/mayorigual", pagoController::getPagosByMontoMayorIgual);
        app.get("/api/pagos/monto/menorigual", pagoController::getPagosByMontoMenorIgual);

        // Rutas de búsqueda especializadas
        app.get("/api/pagos/contrato/{idContrato}/mas-reciente", pagoController::getPagoMasRecienteByContrato);
        app.get("/api/pagos/contrato/{idContrato}/masantiguo", pagoController::getPagoMasAntiguoByContrato);

        // Ruta para cambiar estado
        app.patch("/api/pagos/{id}/estado", pagoController::cambiarEstadoPago);
    }
}