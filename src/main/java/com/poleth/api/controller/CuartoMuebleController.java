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

    // POST: Crear nuevo cuarto mueble
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

    // GET: Obtener todos los cuarto muebles
    public void getAllCuartoMuebles(Context ctx) {
        try {
            ctx.json(cuartoMuebleService.getAllCuartoMuebles());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los cuarto muebles: " + e.getMessage());
        }
    }

    // GET: Obtener cuarto mueble por ID
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

    // GET: Obtener cuarto muebles por cuarto
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

    // GET: Obtener cuarto muebles por catálogo
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

    // GET: Obtener cuarto mueble específico por cuarto y catálogo
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

    // GET: Obtener cuarto muebles con stock
    public void getCuartoMueblesWithStock(Context ctx) {
        try {
            List<CuartoMueble> cuartoMuebles = cuartoMuebleService.getCuartoMueblesWithStock();
            ctx.json(cuartoMuebles);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los cuarto muebles con stock: " + e.getMessage());
        }
    }

    // GET: Obtener cuarto muebles sin stock
    public void getCuartoMueblesWithoutStock(Context ctx) {
        try {
            List<CuartoMueble> cuartoMuebles = cuartoMuebleService.getCuartoMueblesWithoutStock();
            ctx.json(cuartoMuebles);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los cuarto muebles sin stock: " + e.getMessage());
        }
    }

    // PUT: Actualizar cuarto mueble completo
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

    // PUT: Actualizar cantidad específica
    public void updateCantidad(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            CantidadRequest request = objectMapper.readValue(ctx.body(), CantidadRequest.class);

            if (request.getCantidad() == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo 'cantidad' es requerido");
                return;
            }

            boolean success = cuartoMuebleService.updateCantidad(id, request.getCantidad());

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

    // DELETE: Eliminar cuarto mueble
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

    // Clase interna para request de cantidad
    private static class CantidadRequest {
        private Integer cantidad;

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }
    }
}