// CuartoController.java
package com.poleth.api.controller;

import com.poleth.api.model.Cuarto;
import com.poleth.api.service.CuartoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class CuartoController {
    private final CuartoService cuartoService;
    private final ObjectMapper objectMapper;

    public CuartoController(CuartoService cuartoService) {
        this.cuartoService = cuartoService;
        this.objectMapper = new ObjectMapper();
    }

    // POST: Crear un nuevo cuarto
    public void createCuarto(Context ctx) {
        try {
            Cuarto cuarto = objectMapper.readValue(ctx.body(), Cuarto.class);

            // Usar el método createCuarto que incluye validaciones
            Cuarto savedCuarto = cuartoService.createCuarto(cuarto);
            ctx.status(HttpStatus.CREATED)
                    .json(savedCuarto);
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al crear el cuarto: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al crear el cuarto: " + e.getMessage());
        }
    }

    // GET: Obtener todos los cuartos
    public void getAllCuartos(Context ctx) {
        try {
            List<Cuarto> cuartos = cuartoService.getAllCuartos();
            ctx.json(cuartos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los cuartos: " + e.getMessage());
        }
    }

    // GET: Obtener cuarto por ID
    public void getCuartoById(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Cuarto> cuarto = cuartoService.getCuartoById(id);

            if (cuarto.isPresent()) {
                ctx.json(cuarto.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Cuarto no encontrado con ID: " + id);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el cuarto: " + e.getMessage());
        }
    }

    // GET: Obtener cuartos por propietario
    public void getCuartosByPropietario(Context ctx) {
        try {
            Integer idPropietario = Integer.parseInt(ctx.pathParam("idPropietario"));
            List<Cuarto> cuartos = cuartoService.getCuartosByPropietario(idPropietario);
            ctx.json(cuartos);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de propietario inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener cuartos del propietario: " + e.getMessage());
        }
    }

    // PUT: Actualizar cuarto completo
    public void updateCuarto(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Cuarto cuartoActualizado = objectMapper.readValue(ctx.body(), Cuarto.class);

            // Usar el método updateCuarto que incluye validaciones
            Cuarto updatedCuarto = cuartoService.updateCuarto(id, cuartoActualizado);
            ctx.json(updatedCuarto);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al actualizar el cuarto: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al actualizar el cuarto: " + e.getMessage());
        }
    }

    // PATCH: Cambiar estado del cuarto
    public void cambiarEstadoCuarto(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("id"));
            String body = ctx.body();

            // Extraer nuevoEstado del cuerpo JSON
            String nuevoEstado = objectMapper.readTree(body).get("estado_cuarto").asText();

            Cuarto cuarto = cuartoService.cambiarEstadoCuarto(idCuarto, nuevoEstado);
            ctx.json(cuarto);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al cambiar estado: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al cambiar estado: " + e.getMessage());
        }
    }

    // PATCH: Actualizar precio del cuarto
    public void actualizarPrecioCuarto(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("id"));
            String body = ctx.body();

            // Extraer nuevoPrecio del cuerpo JSON
            BigDecimal nuevoPrecio = new BigDecimal(objectMapper.readTree(body).get("precio_alquiler").asText());

            Cuarto cuarto = cuartoService.actualizarPrecioCuarto(idCuarto, nuevoPrecio);
            ctx.json(cuarto);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido o formato de precio incorrecto");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al actualizar precio: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al actualizar precio: " + e.getMessage());
        }
    }

    // DELETE: Eliminar cuarto
    public void deleteCuarto(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));

            // Verificar si el cuarto existe antes de eliminar
            if (!cuartoService.existsById(id)) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Cuarto no encontrado con ID: " + id);
                return;
            }

            cuartoService.deleteCuarto(id);
            ctx.status(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al eliminar el cuarto: " + e.getMessage());
        }
    }
}