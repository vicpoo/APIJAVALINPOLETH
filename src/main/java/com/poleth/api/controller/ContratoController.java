package com.poleth.api.controller;

import com.poleth.api.model.Contrato;
import com.poleth.api.service.ContratoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    public void createContrato(Context ctx) {
        try {
            Contrato contrato = objectMapper.readValue(ctx.body(), Contrato.class);

            // Usar el método createContrato que incluye validaciones
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

    public void getAllContratos(Context ctx) {
        try {
            List<Contrato> contratos = contratoService.getAllContratos();
            ctx.json(contratos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los contratos: " + e.getMessage());
        }
    }

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

    public void updateContrato(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Contrato contratoActualizado = objectMapper.readValue(ctx.body(), Contrato.class);

            // Usar el método updateContrato que incluye validaciones
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

    public void deleteContrato(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            
            // Verificar si el contrato existe antes de eliminar
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

    // Métodos específicos para Contrato

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

    public void getContratosByEstado(Context ctx) {
        try {
            String estado = ctx.queryParam("estado");
            if (estado == null || estado.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro estado es requerido");
                return;
            }

            List<Contrato> contratos = contratoService.getContratosByEstado(estado);
            ctx.json(contratos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar contratos por estado: " + e.getMessage());
        }
    }

    public void getContratosActivos(Context ctx) {
        try {
            List<Contrato> contratos = contratoService.getContratosActivos();
            ctx.json(contratos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener contratos activos: " + e.getMessage());
        }
    }

    public void getContratosWithRelations(Context ctx) {
        try {
            List<Contrato> contratos = contratoService.getContratosWithRelations();
            ctx.json(contratos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener contratos con relaciones: " + e.getMessage());
        }
    }

    public void getContratoByIdWithRelations(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Contrato> contrato = contratoService.getContratoByIdWithRelations(id);

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
                    .json("Error al obtener el contrato con relaciones: " + e.getMessage());
        }
    }

    // Métodos de gestión

    public void finalizarContrato(Context ctx) {
        try {
            Integer idContrato = Integer.parseInt(ctx.pathParam("id"));
            String body = ctx.body();
            
            // Extraer fechaFinalizacion del cuerpo JSON
            String fechaFinalizacionStr = objectMapper.readTree(body).get("fechaFinalizacion").asText();
            LocalDate fechaFinalizacion = LocalDate.parse(fechaFinalizacionStr);

            Contrato contrato = contratoService.finalizarContrato(idContrato, fechaFinalizacion);
            ctx.json(contrato);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de contrato inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al finalizar contrato: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al finalizar contrato: " + e.getMessage());
        }
    }

    public void actualizarEstadoContrato(Context ctx) {
        try {
            Integer idContrato = Integer.parseInt(ctx.pathParam("id"));
            String body = ctx.body();
            
            // Extraer nuevoEstado del cuerpo JSON
            String nuevoEstado = objectMapper.readTree(body).get("estado").asText();

            Contrato contrato = contratoService.actualizarEstadoContrato(idContrato, nuevoEstado);
            ctx.json(contrato);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de contrato inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al actualizar estado: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al actualizar estado: " + e.getMessage());
        }
    }

    public void actualizarMontoRenta(Context ctx) {
        try {
            Integer idContrato = Integer.parseInt(ctx.pathParam("id"));
            String body = ctx.body();
            
            // Extraer nuevoMonto del cuerpo JSON
            BigDecimal nuevoMonto = new BigDecimal(objectMapper.readTree(body).get("monto").asText());

            Contrato contrato = contratoService.actualizarMontoRenta(idContrato, nuevoMonto);
            ctx.json(contrato);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de contrato inválido o formato de monto incorrecto");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al actualizar monto: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al actualizar monto: " + e.getMessage());
        }
    }

    // Métodos de verificación

    public void existsContratoActivoByCuarto(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("idCuarto"));
            boolean exists = contratoService.existsContratoActivoByCuarto(idCuarto);
            ctx.json(exists);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar contrato activo del cuarto: " + e.getMessage());
        }
    }

    public void existsContratoActivoByInquilino(Context ctx) {
        try {
            Integer idInquilino = Integer.parseInt(ctx.pathParam("idInquilino"));
            boolean exists = contratoService.existsContratoActivoByInquilino(idInquilino);
            ctx.json(exists);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de inquilino inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar contrato activo del inquilino: " + e.getMessage());
        }
    }

    public void isContratoActivo(Context ctx) {
        try {
            Integer idContrato = Integer.parseInt(ctx.pathParam("id"));
            boolean isActive = contratoService.isContratoActivo(idContrato);
            ctx.json(isActive);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de contrato inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar estado del contrato: " + e.getMessage());
        }
    }

    // Métodos de conteo

    public void countContratos(Context ctx) {
        try {
            Long count = contratoService.countContratos();
            ctx.json(count);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar contratos: " + e.getMessage());
        }
    }

    public void countContratosActivos(Context ctx) {
        try {
            Long count = contratoService.countContratosActivos();
            ctx.json(count);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar contratos activos: " + e.getMessage());
        }
    }

    public void countContratosByEstado(Context ctx) {
        try {
            String estado = ctx.queryParam("estado");
            if (estado == null || estado.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro estado es requerido");
                return;
            }

            Long count = contratoService.countContratosByEstado(estado);
            ctx.json(count);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar contratos por estado: " + e.getMessage());
        }
    }
}