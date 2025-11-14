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
        RolRepository rolRepository = new RolRepository();
        RolService rolService = new RolService(rolRepository);
        RolController rolController = new RolController(rolService);
        RolRoutes rolRoutes = new RolRoutes(rolController);

        PropietarioRepository propietarioRepository = new PropietarioRepository();
        PropietarioService propietarioService = new PropietarioService(propietarioRepository);
        PropietarioController propietarioController = new PropietarioController(propietarioService);
        PropietarioRoutes propietarioRoutes = new PropietarioRoutes(propietarioController);

        InquilinoRepository inquilinoRepository = new InquilinoRepository();
        InquilinoService inquilinoService = new InquilinoService(inquilinoRepository);
        InquilinoController inquilinoController = new InquilinoController(inquilinoService);
        InquilinoRoutes inquilinoRoutes = new InquilinoRoutes(inquilinoController);

        InvitadoRepository invitadoRepository = new InvitadoRepository();
        InvitadoService invitadoService = new InvitadoService(invitadoRepository);
        InvitadoController invitadoController = new InvitadoController(invitadoService);
        InvitadoRoutes invitadoRoutes = new InvitadoRoutes(invitadoController);

        CuartoRepository cuartoRepository = new CuartoRepository();
        CuartoService cuartoService = new CuartoService(cuartoRepository);
        CuartoController cuartoController = new CuartoController(cuartoService);
        CuartoRoutes cuartoRoutes = new CuartoRoutes(cuartoController);

        CatalogoMuebleRepository catalogoMuebleRepository = new CatalogoMuebleRepository();
        CatalogoMuebleService catalogoMuebleService = new CatalogoMuebleService(catalogoMuebleRepository);
        CatalogoMuebleController catalogoMuebleController = new CatalogoMuebleController(catalogoMuebleService);
        CatalogoMuebleRoutes catalogoMuebleRoutes = new CatalogoMuebleRoutes(catalogoMuebleController);

        ContratoRepository contratoRepository = new ContratoRepository();
        ContratoService contratoService = new ContratoService(contratoRepository);
        ContratoController contratoController = new ContratoController(contratoService);
        ContratoRoutes contratoRoutes = new ContratoRoutes(contratoController);

        MantenimientoRepository mantenimientoRepository = new MantenimientoRepository();
        MantenimientoService mantenimientoService = new MantenimientoService(mantenimientoRepository);
        MantenimientoController mantenimientoController = new MantenimientoController(mantenimientoService);
        MantenimientoRoutes mantenimientoRoutes = new MantenimientoRoutes(mantenimientoController);

        ReporteInquilinoRepository reporteInquilinoRepository = new ReporteInquilinoRepository();
        ReporteInquilinoService reporteInquilinoService = new ReporteInquilinoService(reporteInquilinoRepository);
        ReporteInquilinoController reporteInquilinoController = new ReporteInquilinoController(reporteInquilinoService);
        ReporteInquilinoRoutes reporteInquilinoRoutes = new ReporteInquilinoRoutes(reporteInquilinoController);

        HistorialReporteRepository historialReporteRepository = new HistorialReporteRepository();
        HistorialReporteService historialReporteService = new HistorialReporteService(historialReporteRepository);
        HistorialReporteController historialReporteController =
                new HistorialReporteController(historialReporteService, reporteInquilinoService);
        HistorialReporteRoutes historialReporteRoutes = new HistorialReporteRoutes(historialReporteController);

        LoginRepository loginRepository = new LoginRepository();
        LoginService loginService = new LoginService(loginRepository, rolRepository, propietarioRepository, inquilinoRepository);
        LoginController loginController = new LoginController(loginService);
        LoginRoutes loginRoutes = new LoginRoutes(loginController);

        ImagenCuartoPublicaRepository imagenRepository = new ImagenCuartoPublicaRepository();
        ImagenCuartoPublicaService imagenService = new ImagenCuartoPublicaService(imagenRepository);
        ImagenCuartoPublicaController imagenController = new ImagenCuartoPublicaController(imagenService);
        ImagenCuartoPublicaRoutes imagenRoutes = new ImagenCuartoPublicaRoutes(imagenController);

        CuartoMuebleRepository cuartoMuebleRepository = new CuartoMuebleRepository();
        CuartoMuebleService cuartoMuebleService = new CuartoMuebleService(cuartoMuebleRepository);
        CuartoMuebleController cuartoMuebleController = new CuartoMuebleController(cuartoMuebleService);
        CuartoMuebleRoutes cuartoMuebleRoutes = new CuartoMuebleRoutes(cuartoMuebleController);

        PagoRepository pagoRepository = new PagoRepository();
        PagoService pagoService = new PagoService(pagoRepository);
        PagoController pagoController = new PagoController(pagoService);
        PagoRoutes pagoRoutes = new PagoRoutes(pagoController);

        NotificacionRepository notificacionRepo = new NotificacionRepository();
        NotificacionService notificacionService = new NotificacionService(notificacionRepo);
        NotificacionController notificacionController = new NotificacionController(notificacionService,inquilinoService);
        NotificacionRoutes notificacionRoutes = new NotificacionRoutes(notificacionController);

        // Registrar rutas
        rolRoutes.configureRoutes(app);
        propietarioRoutes.configureRoutes(app);
        inquilinoRoutes.configureRoutes(app);
        invitadoRoutes.configureRoutes(app);
        cuartoRoutes.configureRoutes(app);
        catalogoMuebleRoutes.configureRoutes(app);
        contratoRoutes.configureRoutes(app);
        mantenimientoRoutes.configureRoutes(app);
        reporteInquilinoRoutes.configureRoutes(app);
        historialReporteRoutes.configureRoutes(app);
        loginRoutes.configureRoutes(app);
        imagenRoutes.configureRoutes(app);
        cuartoMuebleRoutes.configureRoutes(app);
        pagoRoutes.configureRoutes(app);
        notificacionRoutes.configureRoutes(app);

        // Endpoint de prueba
        app.get("/", ctx -> ctx.result("🚀 API Poleth funcionando correctamente con CORS habilitado"));

        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            ctx.status(500).json("Error interno: " + e.getMessage());
        });

        app.error(404, ctx -> ctx.json("Endpoint no encontrado: " + ctx.path()));

        System.out.println("==================================================");
        System.out.println("🚀 Servidor Poleth iniciado correctamente!");
        System.out.println("📍 URL: http://localhost:8000");
        System.out.println("🔧 CORS habilitado ✅ Javalin 5 OK");
        System.out.println("==================================================");
    }
}