// PropietarioController.java
package com.poleth.api.controller;

import com.poleth.api.model.Propietario;
import com.poleth.api.service.PropietarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.List;
import java.util.Optional;

public class PropietarioController {
    private final PropietarioService propietarioService;
    private final ObjectMapper objectMapper;

    public PropietarioController(PropietarioService propietarioService) {
        this.propietarioService = propietarioService;
        this.objectMapper = new ObjectMapper();
    }

    // Crear un nuevo propietario
    public void createPropietario(Context ctx) {
        try {
            Propietario propietario = objectMapper.readValue(ctx.body(), Propietario.class);

            Propietario savedPropietario = propietarioService.createPropietario(propietario);
            ctx.status(HttpStatus.CREATED)
                    .json(savedPropietario);
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al crear el propietario: " + e.getMessage());
        }
    }

    // Obtener todos los propietarios
    public void getAllPropietarios(Context ctx) {
        try {
            ctx.json(propietarioService.getAllPropietarios());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los propietarios: " + e.getMessage());
        }
    }

    // Obtener propietario por ID
    public void getPropietarioById(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Propietario> propietario = propietarioService.getPropietarioById(id);

            if (propietario.isPresent()) {
                ctx.json(propietario.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Propietario no encontrado con ID: " + id);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de propietario inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el propietario: " + e.getMessage());
        }
    }

    // Obtener propietario por Gmail
    public void getPropietarioByGmail(Context ctx) {
        try {
            String gmail = ctx.pathParam("gmail");
            Optional<Propietario> propietario = propietarioService.getPropietarioByGmail(gmail);

            if (propietario.isPresent()) {
                ctx.json(propietario.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Propietario no encontrado con gmail: " + gmail);
            }
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el propietario: " + e.getMessage());
        }
    }

    // Buscar propietarios por nombre
    public void getPropietariosByNombre(Context ctx) {
        try {
            String nombre = ctx.queryParam("nombre");
            if (nombre == null || nombre.trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro 'nombre' es requerido");
                return;
            }

            List<Propietario> propietarios = propietarioService.getPropietariosByNombre(nombre);
            ctx.json(propietarios);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar propietarios: " + e.getMessage());
        }
    }

    // Buscar propietarios por término (nombre o gmail)
    public void searchPropietarios(Context ctx) {
        try {
            String termino = ctx.queryParam("q");
            if (termino == null || termino.trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro de búsqueda 'q' es requerido");
                return;
            }

            List<Propietario> propietarios = propietarioService.searchPropietarios(termino);
            ctx.json(propietarios);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error en la búsqueda: " + e.getMessage());
        }
    }

    // Actualizar propietario
    public void updatePropietario(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Propietario propietario = objectMapper.readValue(ctx.body(), Propietario.class);

            Propietario updatedPropietario = propietarioService.updatePropietario(id, propietario);
            ctx.json(updatedPropietario);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de propietario inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al actualizar el propietario: " + e.getMessage());
        }
    }

    // Eliminar propietario
    public void deletePropietario(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            
            // Verificar si el propietario existe antes de eliminarlo
            Optional<Propietario> propietario = propietarioService.getPropietarioById(id);
            if (propietario.isEmpty()) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Propietario no encontrado con ID: " + id);
                return;
            }

            propietarioService.deletePropietario(id);
            ctx.status(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de propietario inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al eliminar el propietario: " + e.getMessage());
        }
    }

    // Contar propietarios
    public void countPropietarios(Context ctx) {
        try {
            Long count = propietarioService.countPropietarios();
            ctx.json(new CountResponse(count));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar los propietarios: " + e.getMessage());
        }
    }

    // Verificar si existe propietario por Gmail
    public void existsByGmail(Context ctx) {
        try {
            String gmail = ctx.pathParam("gmail");
            boolean exists = propietarioService.existsByGmail(gmail);
            ctx.json(new ExistsResponse(exists));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar el propietario: " + e.getMessage());
        }
    }

    // Verificar si existe propietario por nombre
    public void existsByNombre(Context ctx) {
        try {
            String nombre = ctx.pathParam("nombre");
            boolean exists = propietarioService.existsByNombre(nombre);
            ctx.json(new ExistsResponse(exists));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar el propietario: " + e.getMessage());
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
}