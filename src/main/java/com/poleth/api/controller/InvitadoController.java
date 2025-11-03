// InvitadoController.java
package com.poleth.api.controller;

import com.poleth.api.model.Invitado;
import com.poleth.api.service.InvitadoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.List;
import java.util.Optional;

public class InvitadoController {
    private final InvitadoService invitadoService;
    private final ObjectMapper objectMapper;

    public InvitadoController(InvitadoService invitadoService) {
        this.invitadoService = invitadoService;
        this.objectMapper = new ObjectMapper();
    }

    public void createInvitado(Context ctx) {
        try {
            Invitado invitado = objectMapper.readValue(ctx.body(), Invitado.class);

            // Usar el método createInvitado que incluye validaciones
            Invitado savedInvitado = invitadoService.createInvitado(invitado);
            ctx.status(HttpStatus.CREATED)
                    .json(savedInvitado);
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al crear el invitado: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al crear el invitado: " + e.getMessage());
        }
    }

    public void getAllInvitados(Context ctx) {
        try {
            List<Invitado> invitados = invitadoService.getAllInvitados();
            ctx.json(invitados);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener los invitados: " + e.getMessage());
        }
    }

    public void getInvitadoById(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Invitado> invitado = invitadoService.getInvitadoById(id);

            if (invitado.isPresent()) {
                ctx.json(invitado.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Invitado no encontrado con ID: " + id);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de invitado inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener el invitado: " + e.getMessage());
        }
    }

    public void getInvitadoByEmail(Context ctx) {
        try {
            String email = ctx.queryParam("email");
            if (email == null || email.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro email es requerido");
                return;
            }

            Optional<Invitado> invitado = invitadoService.getInvitadoByEmail(email);

            if (invitado.isPresent()) {
                ctx.json(invitado.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Invitado no encontrado con email: " + email);
            }
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar el invitado: " + e.getMessage());
        }
    }

    public void updateInvitado(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Invitado invitadoActualizado = objectMapper.readValue(ctx.body(), Invitado.class);

            // Usar el método updateInvitado que incluye validaciones
            Invitado updatedInvitado = invitadoService.updateInvitado(id, invitadoActualizado);
            ctx.json(updatedInvitado);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de invitado inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al actualizar el invitado: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al actualizar el invitado: " + e.getMessage());
        }
    }

    public void deleteInvitado(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            
            // Verificar si el invitado existe antes de eliminar
            if (!invitadoService.existsById(id)) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Invitado no encontrado con ID: " + id);
                return;
            }
            
            invitadoService.deleteInvitado(id);
            ctx.status(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de invitado inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al eliminar el invitado: " + e.getMessage());
        }
    }

    // Métodos específicos para Invitado

    public void getInvitadosByNombre(Context ctx) {
        try {
            String nombre = ctx.queryParam("nombre");
            if (nombre == null || nombre.isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El parámetro nombre es requerido");
                return;
            }

            List<Invitado> invitados = invitadoService.getInvitadosByNombre(nombre);
            ctx.json(invitados);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al buscar invitados por nombre: " + e.getMessage());
        }
    }

    public void getInvitadosByCuarto(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("idCuarto"));
            List<Invitado> invitados = invitadoService.getInvitadosByCuarto(idCuarto);
            ctx.json(invitados);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener invitados del cuarto: " + e.getMessage());
        }
    }

    public void getInvitadosWithoutCuarto(Context ctx) {
        try {
            List<Invitado> invitados = invitadoService.getInvitadosWithoutCuarto();
            ctx.json(invitados);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener invitados sin cuarto: " + e.getMessage());
        }
    }

    public void asignarCuarto(Context ctx) {
        try {
            Integer idInvitado = Integer.parseInt(ctx.pathParam("id"));
            String body = ctx.body();
            
            // Extraer idCuartoAcceso del cuerpo JSON
            Integer idCuartoAcceso = objectMapper.readTree(body).get("idCuartoAcceso").asInt();

            Invitado invitado = invitadoService.asignarCuarto(idInvitado, idCuartoAcceso);
            ctx.json(invitado);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al asignar cuarto: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al asignar cuarto: " + e.getMessage());
        }
    }

    public void removerCuarto(Context ctx) {
        try {
            Integer idInvitado = Integer.parseInt(ctx.pathParam("id"));
            Invitado invitado = invitadoService.removerCuarto(idInvitado);
            ctx.json(invitado);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de invitado inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al remover cuarto: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al remover cuarto: " + e.getMessage());
        }
    }

    public void asignarImagen(Context ctx) {
        try {
            Integer idInvitado = Integer.parseInt(ctx.pathParam("id"));
            String body = ctx.body();
            
            // Extraer idImagenVista del cuerpo JSON
            Integer idImagenVista = objectMapper.readTree(body).get("idImagenVista").asInt();

            Invitado invitado = invitadoService.asignarImagen(idInvitado, idImagenVista);
            ctx.json(invitado);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al asignar imagen: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al asignar imagen: " + e.getMessage());
        }
    }

    public void removerImagen(Context ctx) {
        try {
            Integer idInvitado = Integer.parseInt(ctx.pathParam("id"));
            Invitado invitado = invitadoService.removerImagen(idInvitado);
            ctx.json(invitado);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de invitado inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error al remover imagen: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error interno al remover imagen: " + e.getMessage());
        }
    }

    public void countInvitados(Context ctx) {
        try {
            Long count = invitadoService.countInvitados();
            ctx.json(count);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar invitados: " + e.getMessage());
        }
    }

}