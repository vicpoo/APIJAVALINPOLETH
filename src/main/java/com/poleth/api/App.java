// App.java
package com.poleth.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.poleth.api.controller.*;
import com.poleth.api.repository.*;
import com.poleth.api.routes.*;
import com.poleth.api.service.*;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.bundled.CorsPluginConfig;

public class App {
    public static void main(String[] args) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // ✅ Configuración del servidor Javalin con CORS (Javalin 6)
        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";

            // Configuración de CORS
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(rule -> {
                    rule.allowCredentials = true;
                    rule.anyHost(); // Permitir cualquier dominio
                });
            });

            // Solo agregar archivos estáticos si la carpeta existe
            // config.staticFiles.add("/public", Location.CLASSPATH);

        }).start(8000);

        // === INICIALIZACIÓN DE MÓDULOS ===

        // Repositorios
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        RolRepository rolRepository = new RolRepository();
        CuartoRepository cuartoRepository = new CuartoRepository();
        CatalogoMuebleRepository catalogoMuebleRepository = new CatalogoMuebleRepository();
        ContratoRepository contratoRepository = new ContratoRepository();
        MantenimientoRepository mantenimientoRepository = new MantenimientoRepository();
        ReporteInquilinoRepository reporteInquilinoRepository = new ReporteInquilinoRepository();
        HistorialReporteRepository historialReporteRepository = new HistorialReporteRepository();
        CuartoMuebleRepository cuartoMuebleRepository = new CuartoMuebleRepository();
        PagoRepository pagoRepository = new PagoRepository();
        NotificacionRepository notificacionRepository = new NotificacionRepository();

        // Servicios (con dependencias corregidas)
        UsuarioService usuarioService = new UsuarioService(usuarioRepository, rolRepository);
        RolService rolService = new RolService(rolRepository);
        CuartoService cuartoService = new CuartoService(cuartoRepository);
        CatalogoMuebleService catalogoMuebleService = new CatalogoMuebleService(catalogoMuebleRepository);
        ContratoService contratoService = new ContratoService(contratoRepository);
        MantenimientoService mantenimientoService = new MantenimientoService(mantenimientoRepository);
        ReporteInquilinoService reporteInquilinoService = new ReporteInquilinoService(reporteInquilinoRepository);
        HistorialReporteService historialReporteService = new HistorialReporteService(historialReporteRepository);
        CuartoMuebleService cuartoMuebleService = new CuartoMuebleService(cuartoMuebleRepository);
        PagoService pagoService = new PagoService(pagoRepository);
        NotificacionService notificacionService = new NotificacionService(notificacionRepository, usuarioRepository);

        // Controladores (con dependencias corregidas)
        UsuarioController usuarioController = new UsuarioController(usuarioService);
        RolController rolController = new RolController(rolService);
        CuartoController cuartoController = new CuartoController(cuartoService);
        CatalogoMuebleController catalogoMuebleController = new CatalogoMuebleController(catalogoMuebleService);
        ContratoController contratoController = new ContratoController(contratoService);
        MantenimientoController mantenimientoController = new MantenimientoController(mantenimientoService);
        ReporteInquilinoController reporteInquilinoController = new ReporteInquilinoController(reporteInquilinoService);
        HistorialReporteController historialReporteController = new HistorialReporteController(historialReporteService, reporteInquilinoService);
        CuartoMuebleController cuartoMuebleController = new CuartoMuebleController(cuartoMuebleService);
        PagoController pagoController = new PagoController(pagoService);
        NotificacionController notificacionController = new NotificacionController(notificacionService);

        // Rutas
        UsuarioRoutes usuarioRoutes = new UsuarioRoutes(usuarioController);
        RolRoutes rolRoutes = new RolRoutes(rolController);
        CuartoRoutes cuartoRoutes = new CuartoRoutes(cuartoController);
        CatalogoMuebleRoutes catalogoMuebleRoutes = new CatalogoMuebleRoutes(catalogoMuebleController);
        ContratoRoutes contratoRoutes = new ContratoRoutes(contratoController);
        MantenimientoRoutes mantenimientoRoutes = new MantenimientoRoutes(mantenimientoController);
        ReporteInquilinoRoutes reporteInquilinoRoutes = new ReporteInquilinoRoutes(reporteInquilinoController);
        HistorialReporteRoutes historialReporteRoutes = new HistorialReporteRoutes(historialReporteController);
        CuartoMuebleRoutes cuartoMuebleRoutes = new CuartoMuebleRoutes(cuartoMuebleController);
        PagoRoutes pagoRoutes = new PagoRoutes(pagoController);
        NotificacionRoutes notificacionRoutes = new NotificacionRoutes(notificacionController);

        // Registrar rutas (agregando UsuarioRoutes que faltaba)
        usuarioRoutes.configureRoutes(app);
        rolRoutes.configureRoutes(app);
        cuartoRoutes.configureRoutes(app);
        catalogoMuebleRoutes.configureRoutes(app);
        contratoRoutes.configureRoutes(app);
        mantenimientoRoutes.configureRoutes(app);
        reporteInquilinoRoutes.configureRoutes(app);
        historialReporteRoutes.configureRoutes(app);
        cuartoMuebleRoutes.configureRoutes(app);
        pagoRoutes.configureRoutes(app);
        notificacionRoutes.configureRoutes(app);

        // Endpoint de prueba
        app.get("/", ctx -> ctx.result("🚀 API Poleth funcionando correctamente con CORS habilitado"));

        // Health check endpoint
        app.get("/api/health", ctx -> {
            ctx.json("{\"status\": \"ok\", \"message\": \"API funcionando correctamente\"}");
        });

        // Manejo de excepciones global
        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            ctx.status(500).json("{\"error\": \"Error interno del servidor: " + e.getMessage() + "\"}");
        });

        app.error(404, ctx -> {
            ctx.json("{\"error\": \"Endpoint no encontrado: " + ctx.path() + "\"}");
        });

        System.out.println("==================================================");
        System.out.println("🚀 Servidor Poleth iniciado correctamente!");
        System.out.println("📍 URL: http://localhost:8000");
        System.out.println("📊 Endpoints disponibles:");
        System.out.println("==================================================");
    }
}