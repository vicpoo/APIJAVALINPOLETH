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

        // Rutas de búsqueda por contrato
        app.get("/api/pagos/contrato/{idContrato}", pagoController::getPagosByContrato);
        
        // Rutas de búsqueda por fecha
        app.get("/api/pagos/fecha/{fecha}", pagoController::getPagosByFecha);
        app.get("/api/pagos/rango-fechas", pagoController::getPagosByRangoFechas);
        
        // Rutas de búsqueda por concepto
        app.get("/api/pagos/concepto", pagoController::getPagosByConcepto);
        app.get("/api/pagos/con-concepto", pagoController::getPagosWithConcepto);
        app.get("/api/pagos/sin-concepto", pagoController::getPagosWithoutConcepto);
        
        // Rutas de búsqueda por monto
        app.get("/api/pagos/monto/mayor-igual", pagoController::getPagosByMontoMayorIgual);
        app.get("/api/pagos/monto/menor-igual", pagoController::getPagosByMontoMenorIgual);
        
        // Rutas de búsqueda especializadas
        app.get("/api/pagos/contrato/{idContrato}/mas-reciente", pagoController::getPagoMasRecienteByContrato);
        app.get("/api/pagos/contrato/{idContrato}/mas-antiguo", pagoController::getPagoMasAntiguoByContrato);
        
        // Rutas de operaciones específicas
        app.put("/api/pagos/{id}/monto", pagoController::updateMonto);
        app.put("/api/pagos/{id}/concepto", pagoController::updateConcepto);
        app.post("/api/pagos/rapido", pagoController::registrarPagoRapido);
        
        // Rutas de operaciones por lote
        app.delete("/api/pagos/contrato/{idContrato}", pagoController::deletePagosByContrato);
        
        // Rutas de estadísticas y cálculos
        app.get("/api/pagos/stats", pagoController::getStats);
        app.get("/api/pagos/stats/contrato/{idContrato}", pagoController::getStatsByContrato);
        app.get("/api/pagos/stats/rango-fechas", pagoController::getStatsByRangoFechas);
        app.get("/api/pagos/suma/contrato/{idContrato}", pagoController::sumMontoByContrato);
        
        // Rutas de conteo
        app.get("/api/pagos/count", pagoController::countPagos);
        app.get("/api/pagos/count/contrato/{idContrato}", pagoController::countPagosByContrato);
        
        // Rutas de verificación
        app.get("/api/pagos/exists/{id}", pagoController::existsById);
        app.get("/api/pagos/exists/contrato/{idContrato}", pagoController::existsByContrato);

    }
}