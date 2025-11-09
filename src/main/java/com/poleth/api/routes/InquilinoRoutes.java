// InquilinoRoutes.java
package com.poleth.api.routes;

import com.poleth.api.controller.InquilinoController;
import io.javalin.Javalin;

public class InquilinoRoutes {
    private final InquilinoController inquilinoController;

    public InquilinoRoutes(InquilinoController inquilinoController) {
        this.inquilinoController = inquilinoController;
    }

    public void configureRoutes(Javalin app) {
        // Rutas CRUD básicas
        app.post("/api/inquilinos", inquilinoController::createInquilino);
        app.get("/api/inquilinos", inquilinoController::getAllInquilinos);
        app.get("/api/inquilinos/{id}", inquilinoController::getInquilinoById);
        app.put("/api/inquilinos/{id}", inquilinoController::updateInquilino);
        app.delete("/api/inquilinos/{id}", inquilinoController::deleteInquilino);

        // Rutas de búsqueda específicas
        app.get("/api/inquilinos/email/{email}", inquilinoController::getInquilinoByEmail);
        app.get("/api/inquilinos/ine/{ine}", inquilinoController::getInquilinoByIne);
        app.get("/api/inquilinos/buscar/nombre", inquilinoController::getInquilinosByNombre);
        app.get("/api/inquilinos/buscar/telefono", inquilinoController::getInquilinosByTelefono);

        // Rutas de verificación
        app.get("/api/inquilinos/exists/email/{email}", inquilinoController::existsByEmail);
        app.get("/api/inquilinos/exists/ine/{ine}", inquilinoController::existsByIne);
        app.get("/api/inquilinos/exists/nombre/{nombre}", inquilinoController::existsByNombre);
    }
}