// PagoController.java
package com.poleth.api.controller;

import com.poleth.api.model.Pago;
import com.poleth.api.service.PagoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PagoController {
    private final PagoService pagoService;
    private final ObjectMapper objectMapper;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
        this.objectMapper = new ObjectMapper();

        // Registrar el módulo para manejar LocalDate, LocalDateTime, etc.
        this.objectMapper.registerModule(new JavaTimeModule());

        // Esto es importante para que no serialice fechas como timestamps
        this.objectMapper.findAndRegisterModules();
    }

    // POST: Crear nuevo pago
    public void createPago(Context ctx) {
        try {
            Pago pago = objectMapper.readValue(ctx.body(), Pago.class);

            Pago savedPago = pagoService.createPago(pago);
            ctx.status(HttpStatus.CREATED)
                    .json(savedPago);
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al crear el pago: " + e.getMessage());
        }
    }

    // GET: Obtener todos los pagos
    public void getAllPagos(Context ctx) {
        try {
            ctx.json(pagoService.getAllPagos());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los pagos: " + e.getMessage());
        }
    }

    // GET: Obtener pago por ID
    public void getPagoById(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Pago> pago = pagoService.getPagoById(id);

            if (pago.isPresent()) {
                ctx.json(pago.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Pago no encontrado con ID: " + id);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de pago inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el pago: " + e.getMessage());
        }
    }

    // GET: Obtener pagos por contrato
    public void getPagosByContrato(Context ctx) {
        try {
            Integer idContrato = Integer.parseInt(ctx.pathParam("idContrato"));
            List<Pago> pagos = pagoService.getPagosByContrato(idContrato);
            ctx.json(pagos);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de contrato inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los pagos: " + e.getMessage());
        }
    }

    // GET: Obtener pagos por inquilino
    public void getPagosByInquilino(Context ctx) {
        try {
            Integer idInquilino = Integer.parseInt(ctx.pathParam("idInquilino"));
            List<Pago> pagos = pagoService.getPagosByInquilino(idInquilino);
            ctx.json(pagos);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de inquilino inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los pagos: " + e.getMessage());
        }
    }

    // GET: Obtener pagos por fecha específica
    public void getPagosByFecha(Context ctx) {
        try {
            String fechaStr = ctx.pathParam("fecha");
            LocalDate fecha = LocalDate.parse(fechaStr);
            List<Pago> pagos = pagoService.getPagosByFecha(fecha);
            ctx.json(pagos);
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Formato de fecha inválido. Use YYYY-MM-DD");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los pagos: " + e.getMessage());
        }
    }

    // GET: Obtener pagos por estado
    public void getPagosByEstado(Context ctx) {
        try {
            String estado = ctx.pathParam("estado");
            List<Pago> pagos = pagoService.getPagosByEstado(estado);
            ctx.json(pagos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los pagos: " + e.getMessage());
        }
    }

    // GET: Obtener pagos por método de pago
    public void getPagosByMetodoPago(Context ctx) {
        try {
            String metodoPago = ctx.pathParam("metodoPago");
            List<Pago> pagos = pagoService.getPagosByMetodoPago(metodoPago);
            ctx.json(pagos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los pagos: " + e.getMessage());
        }
    }

    // GET: Obtener pagos por monto mayor o igual
    public void getPagosByMontoMayorIgual(Context ctx) {
        try {
            String montoStr = ctx.queryParam("monto");
            if (montoStr == null || montoStr.trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro 'monto' es requerido");
                return;
            }

            BigDecimal monto = new BigDecimal(montoStr);
            List<Pago> pagos = pagoService.getPagosByMontoMayorIgual(monto);
            ctx.json(pagos);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Monto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los pagos: " + e.getMessage());
        }
    }

    // GET: Obtener pagos por monto menor o igual
    public void getPagosByMontoMenorIgual(Context ctx) {
        try {
            String montoStr = ctx.queryParam("monto");
            if (montoStr == null || montoStr.trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro 'monto' es requerido");
                return;
            }

            BigDecimal monto = new BigDecimal(montoStr);
            List<Pago> pagos = pagoService.getPagosByMontoMenorIgual(monto);
            ctx.json(pagos);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Monto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los pagos: " + e.getMessage());
        }
    }

    // GET: Obtener pago más reciente por contrato
    public void getPagoMasRecienteByContrato(Context ctx) {
        try {
            Integer idContrato = Integer.parseInt(ctx.pathParam("idContrato"));
            Optional<Pago> pago = pagoService.getPagoMasRecienteByContrato(idContrato);

            if (pago.isPresent()) {
                ctx.json(pago.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("No se encontraron pagos para el contrato con ID: " + idContrato);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de contrato inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el pago: " + e.getMessage());
        }
    }

    // GET: Obtener pago más antiguo por contrato
    public void getPagoMasAntiguoByContrato(Context ctx) {
        try {
            Integer idContrato = Integer.parseInt(ctx.pathParam("idContrato"));
            Optional<Pago> pago = pagoService.getPagoMasAntiguoByContrato(idContrato);

            if (pago.isPresent()) {
                ctx.json(pago.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("No se encontraron pagos para el contrato con ID: " + idContrato);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de contrato inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el pago: " + e.getMessage());
        }
    }

    // PUT: Actualizar pago completo
    public void updatePago(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Pago pagoActualizado = objectMapper.readValue(ctx.body(), Pago.class);

            Pago updatedPago = pagoService.updatePago(id, pagoActualizado);
            ctx.json(updatedPago);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de pago inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al actualizar el pago: " + e.getMessage());
        }
    }

    // PATCH: Cambiar estado del pago
    public void cambiarEstadoPago(Context ctx) {
        try {
            Integer idPago = Integer.parseInt(ctx.pathParam("id"));
            String body = ctx.body();

            // Extraer nuevoEstado del cuerpo JSON
            String nuevoEstado = objectMapper.readTree(body).get("estado_pago").asText();

            Pago pago = pagoService.cambiarEstadoPago(idPago, nuevoEstado);
            ctx.json(pago);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de pago inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al cambiar estado: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al cambiar estado: " + e.getMessage());
        }
    }

    // DELETE: Eliminar pago
    public void deletePago(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));

            // Verificar si el pago existe antes de eliminarlo
            Optional<Pago> pago = pagoService.getPagoById(id);
            if (pago.isEmpty()) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Pago no encontrado con ID: " + id);
                return;
            }

            pagoService.deletePago(id);
            ctx.status(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de pago inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al eliminar el pago: " + e.getMessage());
        }
    }
}