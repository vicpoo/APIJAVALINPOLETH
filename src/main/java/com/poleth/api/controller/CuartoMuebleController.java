// CuartoMuebleController.java
package com.poleth.api.controller;

import com.poleth.api.model.CuartoMueble;
import com.poleth.api.service.CuartoMuebleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.List;
import java.util.Optional;

public class CuartoMuebleController {
    private final CuartoMuebleService cuartoMuebleService;
    private final ObjectMapper objectMapper;

    public CuartoMuebleController(CuartoMuebleService cuartoMuebleService) {
        this.cuartoMuebleService = cuartoMuebleService;
        this.objectMapper = new ObjectMapper();
    }

    // Crear un nuevo cuarto mueble
    public void createCuartoMueble(Context ctx) {
        try {
            CuartoMueble cuartoMueble = objectMapper.readValue(ctx.body(), CuartoMueble.class);

            CuartoMueble savedCuartoMueble = cuartoMuebleService.createCuartoMueble(cuartoMueble);
            ctx.status(HttpStatus.CREATED)
                    .json(savedCuartoMueble);
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al crear el cuarto mueble: " + e.getMessage());
        }
    }

    // Obtener todos los cuarto muebles
    public void getAllCuartoMuebles(Context ctx) {
        try {
            ctx.json(cuartoMuebleService.getAllCuartoMuebles());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los cuarto muebles: " + e.getMessage());
        }
    }

    // Obtener cuarto mueble por ID
    public void getCuartoMuebleById(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Optional<CuartoMueble> cuartoMueble = cuartoMuebleService.getCuartoMuebleById(id);

            if (cuartoMueble.isPresent()) {
                ctx.json(cuartoMueble.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Cuarto mueble no encontrado con ID: " + id);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto mueble inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el cuarto mueble: " + e.getMessage());
        }
    }

    // Obtener cuarto muebles por cuarto
    public void getCuartoMueblesByCuarto(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("idCuarto"));
            List<CuartoMueble> cuartoMuebles = cuartoMuebleService.getCuartoMueblesByCuarto(idCuarto);
            ctx.json(cuartoMuebles);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los cuarto muebles: " + e.getMessage());
        }
    }

    // Obtener cuarto muebles por catálogo
    public void getCuartoMueblesByCatalogo(Context ctx) {
        try {
            Integer idCatalogoMueble = Integer.parseInt(ctx.pathParam("idCatalogoMueble"));
            List<CuartoMueble> cuartoMuebles = cuartoMuebleService.getCuartoMueblesByCatalogo(idCatalogoMueble);
            ctx.json(cuartoMuebles);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de catálogo mueble inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los cuarto muebles: " + e.getMessage());
        }
    }

    // Obtener cuarto mueble específico por cuarto y catálogo
    public void getCuartoMuebleByCuartoAndCatalogo(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("idCuarto"));
            Integer idCatalogoMueble = Integer.parseInt(ctx.pathParam("idCatalogoMueble"));
            
            Optional<CuartoMueble> cuartoMueble = cuartoMuebleService.getCuartoMuebleByCuartoAndCatalogo(idCuarto, idCatalogoMueble);

            if (cuartoMueble.isPresent()) {
                ctx.json(cuartoMueble.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Cuarto mueble no encontrado para el cuarto " + idCuarto + " y catálogo " + idCatalogoMueble);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto o catálogo inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el cuarto mueble: " + e.getMessage());
        }
    }

    // Obtener cuarto muebles con stock
    public void getCuartoMueblesWithStock(Context ctx) {
        try {
            List<CuartoMueble> cuartoMuebles = cuartoMuebleService.getCuartoMueblesWithStock();
            ctx.json(cuartoMuebles);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los cuarto muebles con stock: " + e.getMessage());
        }
    }

    // Obtener cuarto muebles sin stock
    public void getCuartoMueblesWithoutStock(Context ctx) {
        try {
            List<CuartoMueble> cuartoMuebles = cuartoMuebleService.getCuartoMueblesWithoutStock();
            ctx.json(cuartoMuebles);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los cuarto muebles sin stock: " + e.getMessage());
        }
    }

    // Obtener cuarto muebles con stock por cuarto
    public void getCuartoMueblesWithStockByCuarto(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("idCuarto"));
            List<CuartoMueble> cuartoMuebles = cuartoMuebleService.getCuartoMueblesWithStockByCuarto(idCuarto);
            ctx.json(cuartoMuebles);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los cuarto muebles con stock: " + e.getMessage());
        }
    }

    // Obtener cuarto muebles sin stock por cuarto
    public void getCuartoMueblesWithoutStockByCuarto(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("idCuarto"));
            List<CuartoMueble> cuartoMuebles = cuartoMuebleService.getCuartoMueblesWithoutStockByCuarto(idCuarto);
            ctx.json(cuartoMuebles);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los cuarto muebles sin stock: " + e.getMessage());
        }
    }

    // Actualizar cuarto mueble
    public void updateCuartoMueble(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            CuartoMueble cuartoMueble = objectMapper.readValue(ctx.body(), CuartoMueble.class);

            CuartoMueble updatedCuartoMueble = cuartoMuebleService.updateCuartoMueble(id, cuartoMueble);
            ctx.json(updatedCuartoMueble);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto mueble inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al actualizar el cuarto mueble: " + e.getMessage());
        }
    }

    // Eliminar cuarto mueble
    public void deleteCuartoMueble(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            
            // Verificar si el cuarto mueble existe antes de eliminarlo
            Optional<CuartoMueble> cuartoMueble = cuartoMuebleService.getCuartoMuebleById(id);
            if (cuartoMueble.isEmpty()) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Cuarto mueble no encontrado con ID: " + id);
                return;
            }

            cuartoMuebleService.deleteCuartoMueble(id);
            ctx.status(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto mueble inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al eliminar el cuarto mueble: " + e.getMessage());
        }
    }

    // Actualizar cantidad de un cuarto mueble
    public void updateCantidad(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            String body = ctx.body();
            String cantidadStr = extractJsonValue(body, "cantidad");
            
            if (cantidadStr == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo 'cantidad' es requerido");
                return;
            }

            Integer cantidad = Integer.parseInt(cantidadStr);
            boolean success = cuartoMuebleService.updateCantidad(id, cantidad);

            if (success) {
                ctx.status(HttpStatus.OK)
                        .json("Cantidad actualizada correctamente");
            } else {
                ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .json("Error al actualizar la cantidad");
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID o cantidad inválida");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al actualizar la cantidad: " + e.getMessage());
        }
    }

    // Incrementar cantidad
    public void incrementarCantidad(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            String body = ctx.body();
            String incrementoStr = extractJsonValue(body, "incremento");
            
            if (incrementoStr == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo 'incremento' es requerido");
                return;
            }

            Integer incremento = Integer.parseInt(incrementoStr);
            boolean success = cuartoMuebleService.incrementarCantidad(id, incremento);

            if (success) {
                ctx.status(HttpStatus.OK)
                        .json("Cantidad incrementada correctamente");
            } else {
                ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .json("Error al incrementar la cantidad");
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID o incremento inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al incrementar la cantidad: " + e.getMessage());
        }
    }

    // Decrementar cantidad
    public void decrementarCantidad(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            String body = ctx.body();
            String decrementoStr = extractJsonValue(body, "decremento");
            
            if (decrementoStr == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo 'decremento' es requerido");
                return;
            }

            Integer decremento = Integer.parseInt(decrementoStr);
            boolean success = cuartoMuebleService.decrementarCantidad(id, decremento);

            if (success) {
                ctx.status(HttpStatus.OK)
                        .json("Cantidad decrementada correctamente");
            } else {
                ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .json("Error al decrementar la cantidad");
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID o decremento inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al decrementar la cantidad: " + e.getMessage());
        }
    }

    // Agregar o actualizar mueble en cuarto (upsert)
    public void agregarMuebleACuarto(Context ctx) {
        try {
            String body = ctx.body();
            String idCuartoStr = extractJsonValue(body, "idCuarto");
            String idCatalogoMuebleStr = extractJsonValue(body, "idCatalogoMueble");
            String cantidadStr = extractJsonValue(body, "cantidad");
            String estado = extractJsonValue(body, "estado");
            
            if (idCuartoStr == null || idCatalogoMuebleStr == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("Los campos 'idCuarto' e 'idCatalogoMueble' son requeridos");
                return;
            }

            Integer idCuarto = Integer.parseInt(idCuartoStr);
            Integer idCatalogoMueble = Integer.parseInt(idCatalogoMuebleStr);
            Integer cantidad = cantidadStr != null ? Integer.parseInt(cantidadStr) : null;

            CuartoMueble cuartoMueble = cuartoMuebleService.agregarMuebleACuarto(idCuarto, idCatalogoMueble, cantidad, estado);
            ctx.json(cuartoMueble);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto, catálogo o cantidad inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al agregar mueble al cuarto: " + e.getMessage());
        }
    }

    // Eliminar todos los cuarto muebles de un cuarto
    public void deleteCuartoMueblesByCuarto(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("idCuarto"));
            int deletedCount = cuartoMuebleService.deleteByCuarto(idCuarto);
            ctx.json(new DeleteResponse(deletedCount, "Cuarto muebles eliminados: " + deletedCount));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al eliminar los cuarto muebles: " + e.getMessage());
        }
    }

    // Verificar si existe cuarto mueble por cuarto y catálogo
    public void existsByCuartoAndCatalogo(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("idCuarto"));
            Integer idCatalogoMueble = Integer.parseInt(ctx.pathParam("idCatalogoMueble"));
            boolean exists = cuartoMuebleService.existsByCuartoAndCatalogo(idCuarto, idCatalogoMueble);
            ctx.json(new ExistsResponse(exists));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto o catálogo inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar el cuarto mueble: " + e.getMessage());
        }
    }

    // Obtener estadísticas de cuarto muebles
    public void getStats(Context ctx) {
        try {
            CuartoMuebleService.CuartoMuebleStats stats = cuartoMuebleService.getStats();
            ctx.json(stats);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener estadísticas: " + e.getMessage());
        }
    }

    // Obtener estadísticas por cuarto
    public void getStatsByCuarto(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("idCuarto"));
            CuartoMuebleService.CuartoMuebleStats stats = cuartoMuebleService.getStatsByCuarto(idCuarto);
            ctx.json(stats);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener estadísticas: " + e.getMessage());
        }
    }

    // Contar cuarto muebles
    public void countCuartoMuebles(Context ctx) {
        try {
            Long count = cuartoMuebleService.countCuartoMuebles();
            ctx.json(new CountResponse(count));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar los cuarto muebles: " + e.getMessage());
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