// PagoController.java
package com.poleth.api.controller;

import com.poleth.api.model.Pago;
import com.poleth.api.service.PagoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class PagoController {
    private final PagoService pagoService;
    private final ObjectMapper objectMapper;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
        this.objectMapper = new ObjectMapper();
    }

    // Crear un nuevo pago
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

    // Obtener todos los pagos
    public void getAllPagos(Context ctx) {
        try {
            ctx.json(pagoService.getAllPagos());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los pagos: " + e.getMessage());
        }
    }

    // Obtener pago por ID
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

    // Obtener pagos por contrato
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

    // Obtener pagos por fecha
    public void getPagosByFecha(Context ctx) {
        try {
            String fechaStr = ctx.pathParam("fecha");
            Date fecha = Date.valueOf(fechaStr);
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

    // Obtener pagos por rango de fechas
    public void getPagosByRangoFechas(Context ctx) {
        try {
            String fechaInicioStr = ctx.queryParam("fechaInicio");
            String fechaFinStr = ctx.queryParam("fechaFin");
            
            if (fechaInicioStr == null || fechaFinStr == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("Los parámetros 'fechaInicio' y 'fechaFin' son requeridos");
                return;
            }

            Date fechaInicio = Date.valueOf(fechaInicioStr);
            Date fechaFin = Date.valueOf(fechaFinStr);
            
            List<Pago> pagos = pagoService.getPagosByRangoFechas(fechaInicio, fechaFin);
            ctx.json(pagos);
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Formato de fecha inválido. Use YYYY-MM-DD");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los pagos: " + e.getMessage());
        }
    }

    // Obtener pagos por concepto
    public void getPagosByConcepto(Context ctx) {
        try {
            String concepto = ctx.queryParam("concepto");
            if (concepto == null || concepto.trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro 'concepto' es requerido");
                return;
            }

            List<Pago> pagos = pagoService.getPagosByConcepto(concepto);
            ctx.json(pagos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los pagos: " + e.getMessage());
        }
    }

    // Obtener pagos por monto mayor o igual
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

    // Obtener pagos por monto menor o igual
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

    // Obtener pagos con concepto
    public void getPagosWithConcepto(Context ctx) {
        try {
            List<Pago> pagos = pagoService.getPagosWithConcepto();
            ctx.json(pagos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los pagos: " + e.getMessage());
        }
    }

    // Obtener pagos sin concepto
    public void getPagosWithoutConcepto(Context ctx) {
        try {
            List<Pago> pagos = pagoService.getPagosWithoutConcepto();
            ctx.json(pagos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los pagos: " + e.getMessage());
        }
    }

    // Obtener pago más reciente por contrato
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

    // Obtener pago más antiguo por contrato
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

    // Actualizar pago
    public void updatePago(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Pago pago = objectMapper.readValue(ctx.body(), Pago.class);

            Pago updatedPago = pagoService.updatePago(id, pago);
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

    // Eliminar pago
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

    // Actualizar monto de un pago
    public void updateMonto(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            String body = ctx.body();
            String montoStr = extractJsonValue(body, "monto");
            
            if (montoStr == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo 'monto' es requerido");
                return;
            }

            BigDecimal monto = new BigDecimal(montoStr);
            boolean success = pagoService.updateMonto(id, monto);

            if (success) {
                ctx.status(HttpStatus.OK)
                        .json("Monto actualizado correctamente");
            } else {
                ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .json("Error al actualizar el monto");
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID o monto inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al actualizar el monto: " + e.getMessage());
        }
    }

    // Actualizar concepto de un pago
    public void updateConcepto(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            String body = ctx.body();
            String concepto = extractJsonValue(body, "concepto");

            boolean success = pagoService.updateConcepto(id, concepto);

            if (success) {
                ctx.status(HttpStatus.OK)
                        .json("Concepto actualizado correctamente");
            } else {
                ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .json("Error al actualizar el concepto");
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de pago inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al actualizar el concepto: " + e.getMessage());
        }
    }

    // Registrar pago rápido
    public void registrarPagoRapido(Context ctx) {
        try {
            String body = ctx.body();
            String idContratoStr = extractJsonValue(body, "idContrato");
            String montoStr = extractJsonValue(body, "monto");
            String concepto = extractJsonValue(body, "concepto");
            
            if (idContratoStr == null || montoStr == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("Los campos 'idContrato' y 'monto' son requeridos");
                return;
            }

            Integer idContrato = Integer.parseInt(idContratoStr);
            BigDecimal monto = new BigDecimal(montoStr);

            Pago pago = pagoService.registrarPagoRapido(idContrato, monto, concepto);
            ctx.status(HttpStatus.CREATED)
                    .json(pago);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de contrato o monto inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al registrar el pago: " + e.getMessage());
        }
    }

    // Eliminar todos los pagos de un contrato
    public void deletePagosByContrato(Context ctx) {
        try {
            Integer idContrato = Integer.parseInt(ctx.pathParam("idContrato"));
            int deletedCount = pagoService.deletePagosByContrato(idContrato);
            ctx.json(new DeleteResponse(deletedCount, "Pagos eliminados: " + deletedCount));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de contrato inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al eliminar los pagos: " + e.getMessage());
        }
    }

    // Obtener estadísticas de pagos
    public void getStats(Context ctx) {
        try {
            PagoService.PagoStats stats = pagoService.getStats();
            ctx.json(stats);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener estadísticas: " + e.getMessage());
        }
    }

    // Obtener estadísticas por contrato
    public void getStatsByContrato(Context ctx) {
        try {
            Integer idContrato = Integer.parseInt(ctx.pathParam("idContrato"));
            PagoService.PagoStats stats = pagoService.getStatsByContrato(idContrato);
            ctx.json(stats);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de contrato inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener estadísticas: " + e.getMessage());
        }
    }

    // Obtener estadísticas por rango de fechas
    public void getStatsByRangoFechas(Context ctx) {
        try {
            String fechaInicioStr = ctx.queryParam("fechaInicio");
            String fechaFinStr = ctx.queryParam("fechaFin");
            
            if (fechaInicioStr == null || fechaFinStr == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("Los parámetros 'fechaInicio' y 'fechaFin' son requeridos");
                return;
            }

            Date fechaInicio = Date.valueOf(fechaInicioStr);
            Date fechaFin = Date.valueOf(fechaFinStr);
            
            PagoService.PagoStats stats = pagoService.getStatsByRangoFechas(fechaInicio, fechaFin);
            ctx.json(stats);
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Formato de fecha inválido. Use YYYY-MM-DD");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener estadísticas: " + e.getMessage());
        }
    }

    // Sumar montos por contrato
    public void sumMontoByContrato(Context ctx) {
        try {
            Integer idContrato = Integer.parseInt(ctx.pathParam("idContrato"));
            BigDecimal suma = pagoService.sumMontoByContrato(idContrato);
            ctx.json(new SumResponse(suma));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de contrato inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al calcular la suma: " + e.getMessage());
        }
    }

    // Contar pagos
    public void countPagos(Context ctx) {
        try {
            Long count = pagoService.countPagos();
            ctx.json(new CountResponse(count));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar los pagos: " + e.getMessage());
        }
    }

    // Contar pagos por contrato
    public void countPagosByContrato(Context ctx) {
        try {
            Integer idContrato = Integer.parseInt(ctx.pathParam("idContrato"));
            Long count = pagoService.countByContrato(idContrato);
            ctx.json(new CountResponse(count));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de contrato inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar los pagos: " + e.getMessage());
        }
    }

    // Verificar si existe pago por ID
    public void existsById(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            boolean exists = pagoService.existsById(id);
            ctx.json(new ExistsResponse(exists));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de pago inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar el pago: " + e.getMessage());
        }
    }

    // Verificar si existen pagos por contrato
    public void existsByContrato(Context ctx) {
        try {
            Integer idContrato = Integer.parseInt(ctx.pathParam("idContrato"));
            boolean exists = pagoService.existsByContrato(idContrato);
            ctx.json(new ExistsResponse(exists));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de contrato inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar los pagos: " + e.getMessage());
        }
    }

    // Método auxiliar para extraer valores del JSON
    private String extractJsonValue(String json, String key) {
        try {
            String[] parts = json.split("\"" + key + "\"");
            if (parts.length > 1) {
                String valuePart = parts[1].split("\"")[1];
                return valuePart;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    // Clases internas para respuestas JSON
    private static class CountResponse {
        private final Long count;
        
        public CountResponse(Long count) {
            this.count = count;
        }
        
        public Long getCount() {
            return count;
        }
    }

    private static class SumResponse {
        private final BigDecimal suma;
        
        public SumResponse(BigDecimal suma) {
            this.suma = suma;
        }
        
        public BigDecimal getSuma() {
            return suma;
        }
    }

    private static class ExistsResponse {
        private final boolean exists;
        
        public ExistsResponse(boolean exists) {
            this.exists = exists;
        }
        
        public boolean isExists() {
            return exists;
        }
    }

    private static class DeleteResponse {
        private final int deletedCount;
        private final String message;
        
        public DeleteResponse(int deletedCount, String message) {
            this.deletedCount = deletedCount;
            this.message = message;
        }
        
        public int getDeletedCount() {
            return deletedCount;
        }
        
        public String getMessage() {
            return message;
        }
    }
}