// InquilinoController.java
package com.poleth.api.controller;

import com.poleth.api.model.Inquilino;
import com.poleth.api.service.InquilinoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.List;
import java.util.Optional;

public class InquilinoController {
    private final InquilinoService inquilinoService;
    private final ObjectMapper objectMapper;

    public InquilinoController(InquilinoService inquilinoService) {
        this.inquilinoService = inquilinoService;
        this.objectMapper = new ObjectMapper();
    }

    // Crear un nuevo inquilino
    public void createInquilino(Context ctx) {
        try {
            Inquilino inquilino = objectMapper.readValue(ctx.body(), Inquilino.class);

            Inquilino savedInquilino = inquilinoService.createInquilino(inquilino);
            ctx.status(HttpStatus.CREATED)
                    .json(savedInquilino);
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al crear el inquilino: " + e.getMessage());
        }
    }

    // Obtener todos los inquilinos
    public void getAllInquilinos(Context ctx) {
        try {
            ctx.json(inquilinoService.getAllInquilinos());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los inquilinos: " + e.getMessage());
        }
    }

    // Obtener inquilino por ID
    public void getInquilinoById(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Inquilino> inquilino = inquilinoService.getInquilinoById(id);

            if (inquilino.isPresent()) {
                ctx.json(inquilino.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Inquilino no encontrado con ID: " + id);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de inquilino inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el inquilino: " + e.getMessage());
        }
    }

    // Obtener inquilino por email
    public void getInquilinoByEmail(Context ctx) {
        try {
            String email = ctx.pathParam("email");
            Optional<Inquilino> inquilino = inquilinoService.getInquilinoByEmail(email);

            if (inquilino.isPresent()) {
                ctx.json(inquilino.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Inquilino no encontrado con email: " + email);
            }
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el inquilino: " + e.getMessage());
        }
    }

    // Obtener inquilino por INE
    public void getInquilinoByIne(Context ctx) {
        try {
            String ine = ctx.pathParam("ine");
            Optional<Inquilino> inquilino = inquilinoService.getInquilinoByIne(ine);

            if (inquilino.isPresent()) {
                ctx.json(inquilino.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Inquilino no encontrado con INE: " + ine);
            }
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el inquilino: " + e.getMessage());
        }
    }

    // Buscar inquilinos por nombre
    public void getInquilinosByNombre(Context ctx) {
        try {
            String nombre = ctx.queryParam("nombre");
            if (nombre == null || nombre.trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro 'nombre' es requerido");
                return;
            }

            List<Inquilino> inquilinos = inquilinoService.getInquilinosByNombre(nombre);
            ctx.json(inquilinos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar inquilinos: " + e.getMessage());
        }
    }

    // Buscar inquilinos por teléfono
    public void getInquilinosByTelefono(Context ctx) {
        try {
            String telefono = ctx.queryParam("telefono");
            if (telefono == null || telefono.trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro 'telefono' es requerido");
                return;
            }

            List<Inquilino> inquilinos = inquilinoService.getInquilinosByTelefono(telefono);
            ctx.json(inquilinos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar inquilinos: " + e.getMessage());
        }
    }

    // Búsqueda avanzada por múltiples criterios
    public void searchInquilinos(Context ctx) {
        try {
            String nombre = ctx.queryParam("nombre");
            String email = ctx.queryParam("email");
            String telefono = ctx.queryParam("telefono");

            // Si solo hay un término de búsqueda general
            String terminoGeneral = ctx.queryParam("q");
            if (terminoGeneral != null && !terminoGeneral.trim().isEmpty()) {
                List<Inquilino> inquilinos = inquilinoService.searchInquilinos(terminoGeneral);
                ctx.json(inquilinos);
                return;
            }

            // Búsqueda por criterios específicos
            List<Inquilino> inquilinos = inquilinoService.searchInquilinos(nombre, email, telefono);
            ctx.json(inquilinos);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error en la búsqueda: " + e.getMessage());
        }
    }

    // Actualizar inquilino
    public void updateInquilino(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Inquilino inquilino = objectMapper.readValue(ctx.body(), Inquilino.class);

            Inquilino updatedInquilino = inquilinoService.updateInquilino(id, inquilino);
            ctx.json(updatedInquilino);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de inquilino inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al actualizar el inquilino: " + e.getMessage());
        }
    }

    // Eliminar inquilino
    public void deleteInquilino(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            
            // Verificar si el inquilino existe antes de eliminarlo
            Optional<Inquilino> inquilino = inquilinoService.getInquilinoById(id);
            if (inquilino.isEmpty()) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Inquilino no encontrado con ID: " + id);
                return;
            }

            inquilinoService.deleteInquilino(id);
            ctx.status(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de inquilino inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al eliminar el inquilino: " + e.getMessage());
        }
    }

    // Verificar si existe inquilino por email
    public void existsByEmail(Context ctx) {
        try {
            String email = ctx.pathParam("email");
            boolean exists = inquilinoService.existsByEmail(email);
            ctx.json(new ExistsResponse(exists));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar el inquilino: " + e.getMessage());
        }
    }

    // Verificar si existe inquilino por INE
    public void existsByIne(Context ctx) {
        try {
            String ine = ctx.pathParam("ine");
            boolean exists = inquilinoService.existsByIne(ine);
            ctx.json(new ExistsResponse(exists));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar el inquilino: " + e.getMessage());
        }
    }

    // Verificar si existe inquilino por nombre
    public void existsByNombre(Context ctx) {
        try {
            String nombre = ctx.pathParam("nombre");
            boolean exists = inquilinoService.existsByNombre(nombre);
            ctx.json(new ExistsResponse(exists));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar el inquilino: " + e.getMessage());
        }
    }

    // Obtener estadísticas de inquilinos
    public void getStats(Context ctx) {
        try {
            InquilinoService.InquilinoStats stats = inquilinoService.getStats();
            ctx.json(stats);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener estadísticas: " + e.getMessage());
        }
    }

    // Clases internas para respuestas JSON
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