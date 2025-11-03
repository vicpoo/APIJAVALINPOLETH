// LoginRoutes.java
package com.poleth.api.routes;

import com.poleth.api.controller.LoginController;
import io.javalin.Javalin;

public class LoginRoutes {
    private final LoginController loginController;

    public LoginRoutes(LoginController loginController) {
        this.loginController = loginController;
    }

    public void configureRoutes(Javalin app) {
        // Rutas CRUD básicas
        app.post("/api/logins", loginController::createLogin);
        app.get("/api/logins", loginController::getAllLogins);
        app.get("/api/logins/{id}", loginController::getLoginById);
        app.put("/api/logins/{id}", loginController::updateLogin);
        app.delete("/api/logins/{id}", loginController::deleteLogin);

        // Rutas de autenticación
        app.post("/api/logins/autenticar", loginController::autenticar);
        app.put("/api/logins/{id}/contrasena", loginController::cambiarContrasena);

        // Rutas de búsqueda específicas
        app.get("/api/logins/usuario/{usuario}", loginController::getLoginByUsuario);
        app.get("/api/logins/rol/{idRol}", loginController::getLoginsByRol);
        app.get("/api/logins/tipo/{tipo}", loginController::getLoginsByTipoUsuario);
        app.get("/api/logins/propietario/{idPropietario}", loginController::getLoginByPropietario);
        app.get("/api/logins/inquilino/{idInquilino}", loginController::getLoginByInquilino);

        // Rutas de verificación y utilidad
        app.get("/api/logins/exists/{usuario}", loginController::existsByUsuario);
        app.get("/api/logins/stats", loginController::getStats);
        app.get("/api/logins/count", loginController::countLogins);
    }
}