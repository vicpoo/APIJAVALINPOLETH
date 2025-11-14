//ContratoController.java
package com.poleth.api.controller;

import com.poleth.api.model.Contrato;
import com.poleth.api.service.ContratoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.List;
import java.util.Optional;

public class ContratoController {
    private final ContratoService contratoService;
    private final ObjectMapper objectMapper;

    public ContratoController(ContratoService contratoService) {
        this.contratoService = contratoService;
        this.objectMapper = new ObjectMapper();

        // Registrar el módulo para manejar LocalDate, LocalDateTime, etc.
        this.objectMapper.registerModule(new JavaTimeModule());

        // Esto es importante para que no serialice fechas como timestamps
        this.objectMapper.findAndRegisterModules();
    }

    // POST: Crear nuevo contrato
    public void createContrato(Context ctx) {
        try {
            Contrato contrato = objectMapper.readValue(ctx.body(), Contrato.class);

            Contrato savedContrato = contratoService.createContrato(contrato);
            ctx.status(HttpStatus.CREATED)
                    .json(savedContrato);
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al crear el contrato: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al crear el contrato: " + e.getMessage());
        }
    }

    // GET: Obtener todos los contratos
    public void getAllContratos(Context ctx) {
        try {
            List<Contrato> contratos = contratoService.getAllContratos();
            ctx.json(contratos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los contratos: " + e.getMessage());
        }
    }

    // GET: Obtener contrato por ID
    public void getContratoById(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Contrato> contrato = contratoService.getContratoById(id);

            if (contrato.isPresent()) {
                ctx.json(contrato.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Contrato no encontrado con ID: " + id);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de contrato inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el contrato: " + e.getMessage());
        }
    }

    // GET: Obtener contratos por cuarto
    public void getContratosByCuarto(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("idCuarto"));
            List<Contrato> contratos = contratoService.getContratosByCuarto(idCuarto);
            ctx.json(contratos);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener contratos del cuarto: " + e.getMessage());
        }
    }

    // GET: Obtener contratos por inquilino
    public void getContratosByInquilino(Context ctx) {
        try {
            Integer idInquilino = Integer.parseInt(ctx.pathParam("idInquilino"));
            List<Contrato> contratos = contratoService.getContratosByInquilino(idInquilino);
            ctx.json(contratos);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de inquilino inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener contratos del inquilino: " + e.getMessage());
        }
    }

    // PUT: Actualizar contrato completo
    public void updateContrato(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Contrato contratoActualizado = objectMapper.readValue(ctx.body(), Contrato.class);

            Contrato updatedContrato = contratoService.updateContrato(id, contratoActualizado);
            ctx.json(updatedContrato);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de contrato inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al actualizar el contrato: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al actualizar el contrato: " + e.getMessage());
        }
    }

    // DELETE: Eliminar contrato
    public void deleteContrato(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));

            if (!contratoService.existsById(id)) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Contrato no encontrado con ID: " + id);
                return;
            }

            contratoService.deleteContrato(id);
            ctx.status(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de contrato inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al eliminar el contrato: " + e.getMessage());
        }
    }
}