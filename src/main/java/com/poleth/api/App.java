// App.java
package com.poleth.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.poleth.api.controller.CatalogoMuebleController;
import com.poleth.api.controller.ContratoController;
import com.poleth.api.controller.CuartoController;
import com.poleth.api.controller.CuartoMuebleController;
import com.poleth.api.controller.HistorialReporteController;
import com.poleth.api.controller.ImagenCuartoPublicaController;
import com.poleth.api.controller.InquilinoController;
import com.poleth.api.controller.InvitadoController;
import com.poleth.api.controller.LoginController;
import com.poleth.api.controller.MantenimientoController;
import com.poleth.api.controller.NotificacionController;
import com.poleth.api.controller.PagoController;
import com.poleth.api.controller.PropietarioController;
import com.poleth.api.controller.ReporteInquilinoController;
import com.poleth.api.controller.RolController;
import com.poleth.api.repository.CatalogoMuebleRepository;
import com.poleth.api.repository.ContratoRepository;
import com.poleth.api.repository.CuartoMuebleRepository;
import com.poleth.api.repository.CuartoRepository;
import com.poleth.api.repository.HistorialReporteRepository;
import com.poleth.api.repository.ImagenCuartoPublicaRepository;
import com.poleth.api.repository.InquilinoRepository;
import com.poleth.api.repository.InvitadoRepository;
import com.poleth.api.repository.LoginRepository;
import com.poleth.api.repository.MantenimientoRepository;
import com.poleth.api.repository.NotificacionRepository;
import com.poleth.api.repository.PagoRepository;
import com.poleth.api.repository.PropietarioRepository;
import com.poleth.api.repository.ReporteInquilinoRepository;
import com.poleth.api.repository.RolRepository;
import com.poleth.api.routes.CatalogoMuebleRoutes;
import com.poleth.api.routes.ContratoRoutes;
import com.poleth.api.routes.CuartoMuebleRoutes;
import com.poleth.api.routes.CuartoRoutes;
import com.poleth.api.routes.HistorialReporteRoutes;
import com.poleth.api.routes.ImagenCuartoPublicaRoutes;
import com.poleth.api.routes.InquilinoRoutes;
import com.poleth.api.routes.InvitadoRoutes;
import com.poleth.api.routes.LoginRoutes;
import com.poleth.api.routes.MantenimientoRoutes;
import com.poleth.api.routes.NotificacionRoutes;
import com.poleth.api.routes.PagoRoutes;
import com.poleth.api.routes.PropietarioRoutes;
import com.poleth.api.routes.ReporteInquilinoRoutes;
import com.poleth.api.routes.RolRoutes;
import com.poleth.api.service.CatalogoMuebleService;
import com.poleth.api.service.ContratoService;
import com.poleth.api.service.CuartoMuebleService;
import com.poleth.api.service.CuartoService;
import com.poleth.api.service.HistorialReporteService;
import com.poleth.api.service.ImagenCuartoPublicaService;
import com.poleth.api.service.InquilinoService;
import com.poleth.api.service.InvitadoService;
import com.poleth.api.service.LoginService;
import com.poleth.api.service.MantenimientoService;
import com.poleth.api.service.NotificacionService;
import com.poleth.api.service.PagoService;
import com.poleth.api.service.PropietarioService;
import com.poleth.api.service.ReporteInquilinoService;
import com.poleth.api.service.RolService;
import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        // Configurar ObjectMapper con soporte para Java 8 Date/Time
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        // Crear instancia del servidor Javalin con Jackson configurado
        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
        }).start(8000);

        // Inicializar componentes de Rol
        RolRepository rolRepository = new RolRepository();
        RolService rolService = new RolService(rolRepository);
        RolController rolController = new RolController(rolService);
        RolRoutes rolRoutes = new RolRoutes(rolController);

        // Inicializar componentes de Propietario
        PropietarioRepository propietarioRepository = new PropietarioRepository();
        PropietarioService propietarioService = new PropietarioService(propietarioRepository);
        PropietarioController propietarioController = new PropietarioController(propietarioService);
        PropietarioRoutes propietarioRoutes = new PropietarioRoutes(propietarioController);

        // Inicializar componentes de Inquilino
        InquilinoRepository inquilinoRepository = new InquilinoRepository();
        InquilinoService inquilinoService = new InquilinoService(inquilinoRepository);
        InquilinoController inquilinoController = new InquilinoController(inquilinoService);
        InquilinoRoutes inquilinoRoutes = new InquilinoRoutes(inquilinoController);

        // Inicializar componentes de Invitado
        InvitadoRepository invitadoRepository = new InvitadoRepository();
        InvitadoService invitadoService = new InvitadoService(invitadoRepository);
        InvitadoController invitadoController = new InvitadoController(invitadoService);
        InvitadoRoutes invitadoRoutes = new InvitadoRoutes(invitadoController);

        // Inicializar componentes de Cuarto
        CuartoRepository cuartoRepository = new CuartoRepository();
        CuartoService cuartoService = new CuartoService(cuartoRepository);
        CuartoController cuartoController = new CuartoController(cuartoService);
        CuartoRoutes cuartoRoutes = new CuartoRoutes(cuartoController);

        // Inicializar componentes de CatalogoMueble
        CatalogoMuebleRepository catalogoMuebleRepository = new CatalogoMuebleRepository();
        CatalogoMuebleService catalogoMuebleService = new CatalogoMuebleService(catalogoMuebleRepository);
        CatalogoMuebleController catalogoMuebleController = new CatalogoMuebleController(catalogoMuebleService);
        CatalogoMuebleRoutes catalogoMuebleRoutes = new CatalogoMuebleRoutes(catalogoMuebleController);

        // Inicializar componentes de Contrato
        ContratoRepository contratoRepository = new ContratoRepository();
        ContratoService contratoService = new ContratoService(contratoRepository);
        ContratoController contratoController = new ContratoController(contratoService);
        ContratoRoutes contratoRoutes = new ContratoRoutes(contratoController);

        // Inicializar componentes de Mantenimiento
        MantenimientoRepository mantenimientoRepository = new MantenimientoRepository();
        MantenimientoService mantenimientoService = new MantenimientoService(mantenimientoRepository);
        MantenimientoController mantenimientoController = new MantenimientoController(mantenimientoService);
        MantenimientoRoutes mantenimientoRoutes = new MantenimientoRoutes(mantenimientoController);

        // Inicializar componentes de ReporteInquilino
        ReporteInquilinoRepository reporteInquilinoRepository = new ReporteInquilinoRepository();
        ReporteInquilinoService reporteInquilinoService = new ReporteInquilinoService(reporteInquilinoRepository);
        ReporteInquilinoController reporteInquilinoController = new ReporteInquilinoController(reporteInquilinoService);
        ReporteInquilinoRoutes reporteInquilinoRoutes = new ReporteInquilinoRoutes(reporteInquilinoController);

        // Inicializar componentes de HistorialReporte
        HistorialReporteRepository historialReporteRepository = new HistorialReporteRepository();
        HistorialReporteService historialReporteService = new HistorialReporteService(historialReporteRepository);
        HistorialReporteController historialReporteController = new HistorialReporteController(
            historialReporteService, 
            reporteInquilinoService
        );
        HistorialReporteRoutes historialReporteRoutes = new HistorialReporteRoutes(historialReporteController);

        // Inicializar componentes de Login
        LoginRepository loginRepository = new LoginRepository();
        LoginService loginService = new LoginService(loginRepository, rolRepository, propietarioRepository, inquilinoRepository);
        LoginController loginController = new LoginController(loginService);
        LoginRoutes loginRoutes = new LoginRoutes(loginController);

        // Inicializar componentes de ImagenCuartoPublica
        ImagenCuartoPublicaRepository imagenRepository = new ImagenCuartoPublicaRepository();
        ImagenCuartoPublicaService imagenService = new ImagenCuartoPublicaService(imagenRepository);
        ImagenCuartoPublicaController imagenController = new ImagenCuartoPublicaController(imagenService);
        ImagenCuartoPublicaRoutes imagenRoutes = new ImagenCuartoPublicaRoutes(imagenController);

        // Inicializar componentes de CuartoMueble
        CuartoMuebleRepository cuartoMuebleRepository = new CuartoMuebleRepository();
        CuartoMuebleService cuartoMuebleService = new CuartoMuebleService(cuartoMuebleRepository);
        CuartoMuebleController cuartoMuebleController = new CuartoMuebleController(cuartoMuebleService);
        CuartoMuebleRoutes cuartoMuebleRoutes = new CuartoMuebleRoutes(cuartoMuebleController);

        // Inicializar componentes de Pago
        PagoRepository pagoRepository = new PagoRepository();
        PagoService pagoService = new PagoService(pagoRepository);
        PagoController pagoController = new PagoController(pagoService);
        PagoRoutes pagoRoutes = new PagoRoutes(pagoController);

        // Inicializar componentes de Notificacion
        NotificacionRepository notificacionRepo = new NotificacionRepository();
        NotificacionService notificacionService = new NotificacionService(notificacionRepo);
        NotificacionController notificacionController = new NotificacionController(notificacionService);
        NotificacionRoutes notificacionRoutes = new NotificacionRoutes(notificacionController);

        // Configurar rutas
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

        System.out.println("Servidor iniciado en http://localhost:8000");
    }
}