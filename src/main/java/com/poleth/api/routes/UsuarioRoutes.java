//UsuarioRoutes.java
package com.poleth.api.routes;

import com.poleth.api.controller.UsuarioController;
import io.javalin.Javalin;

public class UsuarioRoutes {
    private final UsuarioController usuarioController;

    public UsuarioRoutes(UsuarioController usuarioController) {
        this.usuarioController = usuarioController;
    }

    public void configureRoutes(Javalin app) {
        // Ruta de login
        app.post("/api/auth/login", usuarioController::login);

        // Rutas CRUD básicas
        app.post("/api/usuarios", usuarioController::createUsuario);
        app.get("/api/usuarios", usuarioController::getAllUsuarios);
        app.get("/api/usuarios/{id}", usuarioController::getUsuarioById);
        app.put("/api/usuarios/{id}", usuarioController::updateUsuario);
        app.delete("/api/usuarios/{id}", usuarioController::deleteUsuario);

        // Rutas adicionales para funcionalidades específicas
        app.get("/api/usuarios/username/{username}", usuarioController::getUsuarioByUsername);
        app.get("/api/usuarios/email/{email}", usuarioController::getUsuarioByEmail);
        app.get("/api/usuarios/exists/username/{username}", usuarioController::existsByUsername);
        app.get("/api/usuarios/exists/email/{email}", usuarioController::existsByEmail);
        app.patch("/api/usuarios/{id}/estado", usuarioController::cambiarEstadoUsuario);
        app.get("/api/usuarios/estado/{estado}", usuarioController::getUsuariosByEstado);
        app.get("/api/usuarios/rol/{rolId}", usuarioController::getUsuariosByRol);
    }
}