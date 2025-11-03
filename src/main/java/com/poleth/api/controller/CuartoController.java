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

    public void getAllCuartos(Context ctx) {
        try {
            List<Cuarto> cuartos = cuartoService.getAllCuartos();
            ctx.json(cuartos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los cuartos: " + e.getMessage());
        }
    }

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

    // Métodos específicos para Cuarto

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

    public void getCuartosByNombre(Context ctx) {
        try {
            String nombre = ctx.queryParam("nombre");
            if (nombre == null || nombre.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro nombre es requerido");
                return;
            }

            List<Cuarto> cuartos = cuartoService.getCuartosByNombre(nombre);
            ctx.json(cuartos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar cuartos por nombre: " + e.getMessage());
        }
    }

    public void getCuartosByEstado(Context ctx) {
        try {
            String estado = ctx.queryParam("estado");
            if (estado == null || estado.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro estado es requerido");
                return;
            }

            List<Cuarto> cuartos = cuartoService.getCuartosByEstado(estado);
            ctx.json(cuartos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar cuartos por estado: " + e.getMessage());
        }
    }

    public void getCuartosAvailable(Context ctx) {
        try {
            List<Cuarto> cuartos = cuartoService.getCuartosAvailable();
            ctx.json(cuartos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener cuartos disponibles: " + e.getMessage());
        }
    }

    public void getCuartosByDescripcion(Context ctx) {
        try {
            String texto = ctx.queryParam("texto");
            if (texto == null || texto.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro texto es requerido");
                return;
            }

            List<Cuarto> cuartos = cuartoService.getCuartosByDescripcionContaining(texto);
            ctx.json(cuartos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar cuartos por descripción: " + e.getMessage());
        }
    }

    public void cambiarEstadoCuarto(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("id"));
            String body = ctx.body();
            
            // Extraer nuevoEstado del cuerpo JSON
            String nuevoEstado = objectMapper.readTree(body).get("estado").asText();

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

    public void actualizarPrecioCuarto(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("id"));
            String body = ctx.body();
            
            // Extraer nuevoPrecio del cuerpo JSON
            BigDecimal nuevoPrecio = new BigDecimal(objectMapper.readTree(body).get("precio").asText());

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

    public void countCuartos(Context ctx) {
        try {
            Long count = cuartoService.countCuartos();
            ctx.json(count);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar cuartos: " + e.getMessage());
        }
    }

    public void countCuartosByPropietario(Context ctx) {
        try {
            Integer idPropietario = Integer.parseInt(ctx.pathParam("idPropietario"));
            Long count = cuartoService.countCuartosByPropietario(idPropietario);
            ctx.json(count);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de propietario inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar cuartos del propietario: " + e.getMessage());
        }
    }


    public void existsByPropietario(Context ctx) {
        try {
            Integer idPropietario = Integer.parseInt(ctx.pathParam("idPropietario"));
            boolean exists = cuartoService.existsByPropietario(idPropietario);
            ctx.json(exists);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de propietario inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar existencia de cuartos: " + e.getMessage());
        }
    }
}