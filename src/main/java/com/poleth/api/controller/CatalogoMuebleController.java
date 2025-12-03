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

    // POST: Crear nuevo mueble en el catálogo
    public void createCatalogoMueble(Context ctx) {
        try {
            CatalogoMueble catalogoMueble = objectMapper.readValue(ctx.body(), CatalogoMueble.class);

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

    // GET: Obtener todos los muebles del catálogo
    public void getAllCatalogoMuebles(Context ctx) {
        try {
            List<CatalogoMueble> catalogoMuebles = catalogoMuebleService.getAllCatalogoMuebles();
            ctx.json(catalogoMuebles);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los muebles del catálogo: " + e.getMessage());
        }
    }

    // GET: Obtener mueble por ID
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

    // PUT: Actualizar mueble completo
    public void updateCatalogoMueble(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            CatalogoMueble catalogoMuebleActualizado = objectMapper.readValue(ctx.body(), CatalogoMueble.class);

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

    // DELETE: Eliminar mueble completo
    public void deleteCatalogoMueble(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));

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

    // GET: Buscar mueble por nombre exacto
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

    // GET: Buscar muebles por nombre que contenga texto
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

    // GET: Buscar muebles por descripción que contenga texto
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

    // DELETE: Eliminar solo la descripción del mueble
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

    // PATCH: Cambiar estado del mueble
    public void cambiarEstadoMueble(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            CambioEstadoRequest request = objectMapper.readValue(ctx.body(), CambioEstadoRequest.class);

            CatalogoMueble catalogoMueble = catalogoMuebleService.cambiarEstadoMueble(id, request.getEstado());
            ctx.json(catalogoMueble);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de mueble inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al cambiar estado: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al cambiar estado: " + e.getMessage());
        }
    }

    // GET: Obtener muebles por estado
    public void getCatalogoMueblesByEstado(Context ctx) {
        try {
            String estado = ctx.pathParam("estado");
            List<CatalogoMueble> catalogoMuebles = catalogoMuebleService.getCatalogoMueblesByEstado(estado);
            ctx.json(catalogoMuebles);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener muebles por estado: " + e.getMessage());
        }
    }

    // GET: Obtener muebles activos
    public void getCatalogoMueblesActivos(Context ctx) {
        try {
            List<CatalogoMueble> catalogoMuebles = catalogoMuebleService.getCatalogoMueblesActivos();
            ctx.json(catalogoMuebles);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener muebles activos: " + e.getMessage());
        }
    }

    // Clase interna para cambio de estado
    private static class CambioEstadoRequest {
        private String estado;

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }
    }
}