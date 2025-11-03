// CatalogoMuebleController.java
package com.poleth.api.controller;

import com.poleth.api.model.CatalogoMueble;
import com.poleth.api.service.CatalogoMuebleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.List;
import java.util.Optional;

public class CatalogoMuebleController {
    private final CatalogoMuebleService catalogoMuebleService;
    private final ObjectMapper objectMapper;

    public CatalogoMuebleController(CatalogoMuebleService catalogoMuebleService) {
        this.catalogoMuebleService = catalogoMuebleService;
        this.objectMapper = new ObjectMapper();
    }

    public void createCatalogoMueble(Context ctx) {
        try {
            CatalogoMueble catalogoMueble = objectMapper.readValue(ctx.body(), CatalogoMueble.class);

            // Usar el método createCatalogoMueble que incluye validaciones
            CatalogoMueble savedCatalogoMueble = catalogoMuebleService.createCatalogoMueble(catalogoMueble);
            ctx.status(HttpStatus.CREATED)
                    .json(savedCatalogoMueble);
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al crear el mueble en el catálogo: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al crear el mueble en el catálogo: " + e.getMessage());
        }
    }

    public void getAllCatalogoMuebles(Context ctx) {
        try {
            List<CatalogoMueble> catalogoMuebles = catalogoMuebleService.getAllCatalogoMuebles();
            ctx.json(catalogoMuebles);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los muebles del catálogo: " + e.getMessage());
        }
    }

    public void getCatalogoMuebleById(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Optional<CatalogoMueble> catalogoMueble = catalogoMuebleService.getCatalogoMuebleById(id);

            if (catalogoMueble.isPresent()) {
                ctx.json(catalogoMueble.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Mueble no encontrado en el catálogo con ID: " + id);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de mueble inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el mueble del catálogo: " + e.getMessage());
        }
    }

    public void updateCatalogoMueble(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            CatalogoMueble catalogoMuebleActualizado = objectMapper.readValue(ctx.body(), CatalogoMueble.class);

            // Usar el método updateCatalogoMueble que incluye validaciones
            CatalogoMueble updatedCatalogoMueble = catalogoMuebleService.updateCatalogoMueble(id, catalogoMuebleActualizado);
            ctx.json(updatedCatalogoMueble);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de mueble inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al actualizar el mueble del catálogo: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al actualizar el mueble del catálogo: " + e.getMessage());
        }
    }

    public void deleteCatalogoMueble(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            
            // Verificar si el mueble existe antes de eliminar
            if (!catalogoMuebleService.existsById(id)) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Mueble no encontrado en el catálogo con ID: " + id);
                return;
            }
            
            catalogoMuebleService.deleteCatalogoMueble(id);
            ctx.status(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de mueble inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al eliminar el mueble del catálogo: " + e.getMessage());
        }
    }

    // Métodos específicos para CatalogoMueble

    public void getCatalogoMuebleByNombre(Context ctx) {
        try {
            String nombre = ctx.queryParam("nombre");
            if (nombre == null || nombre.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro nombre es requerido");
                return;
            }

            Optional<CatalogoMueble> catalogoMueble = catalogoMuebleService.getCatalogoMuebleByNombre(nombre);

            if (catalogoMueble.isPresent()) {
                ctx.json(catalogoMueble.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Mueble no encontrado con nombre: " + nombre);
            }
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar mueble por nombre: " + e.getMessage());
        }
    }

    public void getCatalogoMueblesByNombreContaining(Context ctx) {
        try {
            String texto = ctx.queryParam("texto");
            if (texto == null || texto.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro texto es requerido");
                return;
            }

            List<CatalogoMueble> catalogoMuebles = catalogoMuebleService.getCatalogoMueblesByNombreContaining(texto);
            ctx.json(catalogoMuebles);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar muebles por nombre: " + e.getMessage());
        }
    }

    public void getCatalogoMueblesByDescripcionContaining(Context ctx) {
        try {
            String texto = ctx.queryParam("texto");
            if (texto == null || texto.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro texto es requerido");
                return;
            }

            List<CatalogoMueble> catalogoMuebles = catalogoMuebleService.getCatalogoMueblesByDescripcionContaining(texto);
            ctx.json(catalogoMuebles);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar muebles por descripción: " + e.getMessage());
        }
    }

    public void getCatalogoMueblesWithDescripcion(Context ctx) {
        try {
            List<CatalogoMueble> catalogoMuebles = catalogoMuebleService.getCatalogoMueblesWithDescripcion();
            ctx.json(catalogoMuebles);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener muebles con descripción: " + e.getMessage());
        }
    }

    public void getCatalogoMueblesWithoutDescripcion(Context ctx) {
        try {
            List<CatalogoMueble> catalogoMuebles = catalogoMuebleService.getCatalogoMueblesWithoutDescripcion();
            ctx.json(catalogoMuebles);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener muebles sin descripción: " + e.getMessage());
        }
    }

    public void getCatalogoMueblesOrderByNombre(Context ctx) {
        try {
            List<CatalogoMueble> catalogoMuebles = catalogoMuebleService.getCatalogoMueblesOrderByNombre();
            ctx.json(catalogoMuebles);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener muebles ordenados: " + e.getMessage());
        }
    }

    public void actualizarDescripcionMueble(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            String body = ctx.body();
            
            // Extraer descripcion del cuerpo JSON
            String nuevaDescripcion = objectMapper.readTree(body).get("descripcion").asText();

            CatalogoMueble catalogoMueble = catalogoMuebleService.actualizarDescripcionMueble(id, nuevaDescripcion);
            ctx.json(catalogoMueble);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de mueble inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al actualizar descripción: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al actualizar descripción: " + e.getMessage());
        }
    }

    public void eliminarDescripcionMueble(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            CatalogoMueble catalogoMueble = catalogoMuebleService.eliminarDescripcionMueble(id);
            ctx.json(catalogoMueble);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de mueble inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al eliminar descripción: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al eliminar descripción: " + e.getMessage());
        }
    }

    public void countCatalogoMuebles(Context ctx) {
        try {
            Long count = catalogoMuebleService.countCatalogoMuebles();
            ctx.json(count);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar muebles del catálogo: " + e.getMessage());
        }
    }

    public void existsByNombreMueble(Context ctx) {
        try {
            String nombre = ctx.queryParam("nombre");
            if (nombre == null || nombre.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro nombre es requerido");
                return;
            }

            boolean exists = catalogoMuebleService.existsByNombreMueble(nombre);
            ctx.json(exists);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar existencia del mueble: " + e.getMessage());
        }
    }


}