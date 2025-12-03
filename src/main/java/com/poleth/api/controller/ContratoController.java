// ContratoController.java
package com.poleth.api.controller;

import com.poleth.api.model.Contrato;
import com.poleth.api.service.ContratoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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

        // CORRECCIÓN: Configurar ObjectMapper para manejar lazy loading
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        // Esto es importante para que no serialice fechas como timestamps
        this.objectMapper.findAndRegisterModules();
    }

    // POST: Crear nuevo contrato
    public void createContrato(Context ctx) {
        try {
            Contrato contrato = objectMapper.readValue(ctx.body(), Contrato.class);

            Contrato savedContrato = contratoService.createContrato(contrato);

            // CORRECCIÓN: Serializar manualmente para control total
            String jsonResponse = objectMapper.writeValueAsString(savedContrato);
            ctx.status(HttpStatus.CREATED)
                    .contentType("application/json")
                    .result(jsonResponse);
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .contentType("application/json")
                    .result("{\"error\": \"" + e.getMessage().replace("\"", "\\\"") + "\"}");
        } catch (Exception e) {
            e.printStackTrace(); // Para debugging
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType("application/json")
                    .result("{\"error\": \"Error interno al crear el contrato: " + e.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    // GET: Obtener todos los contratos
    public void getAllContratos(Context ctx) {
        try {
            List<Contrato> contratos = contratoService.getAllContratos();

            // CORRECCIÓN: Serializar manualmente
            String jsonResponse = objectMapper.writeValueAsString(contratos);
            ctx.contentType("application/json")
                    .result(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace(); // Para debugging
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType("application/json")
                    .result("{\"error\": \"Error al obtener los contratos: " + e.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    // GET: Obtener contrato por ID
    public void getContratoById(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Contrato> contrato = contratoService.getContratoById(id);

            if (contrato.isPresent()) {
                String jsonResponse = objectMapper.writeValueAsString(contrato.get());
                ctx.contentType("application/json")
                        .result(jsonResponse);
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .contentType("application/json")
                        .result("{\"error\": \"Contrato no encontrado con ID: " + id + "\"}");
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .contentType("application/json")
                    .result("{\"error\": \"ID de contrato inválido\"}");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType("application/json")
                    .result("{\"error\": \"Error al obtener el contrato: " + e.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    // GET: Obtener contratos por cuarto
    public void getContratosByCuarto(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("idCuarto"));
            List<Contrato> contratos = contratoService.getContratosByCuarto(idCuarto);

            String jsonResponse = objectMapper.writeValueAsString(contratos);
            ctx.contentType("application/json")
                    .result(jsonResponse);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .contentType("application/json")
                    .result("{\"error\": \"ID de cuarto inválido\"}");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType("application/json")
                    .result("{\"error\": \"Error al obtener contratos del cuarto: " + e.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    // GET: Obtener contratos por inquilino
    public void getContratosByInquilino(Context ctx) {
        try {
            Integer idInquilino = Integer.parseInt(ctx.pathParam("idInquilino"));
            List<Contrato> contratos = contratoService.getContratosByInquilino(idInquilino);

            String jsonResponse = objectMapper.writeValueAsString(contratos);
            ctx.contentType("application/json")
                    .result(jsonResponse);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .contentType("application/json")
                    .result("{\"error\": \"ID de inquilino inválido\"}");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType("application/json")
                    .result("{\"error\": \"Error al obtener contratos del inquilino: " + e.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    // PUT: Actualizar contrato completo
    public void updateContrato(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Contrato contratoActualizado = objectMapper.readValue(ctx.body(), Contrato.class);

            Contrato updatedContrato = contratoService.updateContrato(id, contratoActualizado);

            String jsonResponse = objectMapper.writeValueAsString(updatedContrato);
            ctx.contentType("application/json")
                    .result(jsonResponse);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .contentType("application/json")
                    .result("{\"error\": \"ID de contrato inválido\"}");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .contentType("application/json")
                    .result("{\"error\": \"" + e.getMessage().replace("\"", "\\\"") + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType("application/json")
                    .result("{\"error\": \"Error interno al actualizar el contrato: " + e.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    // DELETE: Eliminar contrato
    public void deleteContrato(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));

            if (!contratoService.existsById(id)) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .contentType("application/json")
                        .result("{\"error\": \"Contrato no encontrado con ID: " + id + "\"}");
                return;
            }

            contratoService.deleteContrato(id);
            ctx.status(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .contentType("application/json")
                    .result("{\"error\": \"ID de contrato inválido\"}");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType("application/json")
                    .result("{\"error\": \"Error al eliminar el contrato: " + e.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }
}