// ImagenCuartoPublicaController.java
package com.poleth.api.controller;

import com.poleth.api.model.ImagenCuartoPublica;
import com.poleth.api.service.ImagenCuartoPublicaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class ImagenCuartoPublicaController {
    private final ImagenCuartoPublicaService imagenService;
    private final ObjectMapper objectMapper;

    public ImagenCuartoPublicaController(ImagenCuartoPublicaService imagenService) {
        this.imagenService = imagenService;
        this.objectMapper = new ObjectMapper();
    }

    // Subir y crear una nueva imagen
    public void uploadImagen(Context ctx) {
        try {
            // Obtener datos del formulario
            String idCuartoStr = ctx.formParam("idCuarto");
            String descripcion = ctx.formParam("descripcion");
            String esPublicaStr = ctx.formParam("esPublica");
            
            if (idCuartoStr == null || idCuartoStr.trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El ID del cuarto es requerido");
                return;
            }

            Integer idCuarto = Integer.parseInt(idCuartoStr);
            Boolean esPublica = esPublicaStr != null ? Boolean.parseBoolean(esPublicaStr) : true;

            // Obtener el archivo
            var uploadedFile = ctx.uploadedFile("imagen");
            if (uploadedFile == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El archivo de imagen es requerido");
                return;
            }

            String originalFileName = uploadedFile.filename();
            InputStream fileStream = uploadedFile.content();

            // Subir y guardar la imagen
            ImagenCuartoPublica savedImagen = imagenService.uploadAndSaveImagen(
                    idCuarto, descripcion, esPublica, fileStream, originalFileName);

            ctx.status(HttpStatus.CREATED)
                    .json(savedImagen);

        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al subir la imagen: " + e.getMessage());
        }
    }

    // Obtener todas las imágenes
    public void getAllImagenes(Context ctx) {
        try {
            ctx.json(imagenService.getAllImagenes());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener las imágenes: " + e.getMessage());
        }
    }

    // Obtener imagen por ID
    public void getImagenById(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Optional<ImagenCuartoPublica> imagen = imagenService.getImagenById(id);

            if (imagen.isPresent()) {
                ctx.json(imagen.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Imagen no encontrada con ID: " + id);
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de imagen inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener la imagen: " + e.getMessage());
        }
    }

    // Servir archivo de imagen
    public void serveImagen(Context ctx) {
        try {
            String fileName = ctx.pathParam("fileName");
            String imageUrl = "/api/images/" + fileName;
            
            Optional<ImagenCuartoPublica> imagenOpt = imagenService.getImagenByUrl(imageUrl);
            
            if (imagenOpt.isEmpty()) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Imagen no encontrada");
                return;
            }

            ImagenCuartoPublica imagen = imagenOpt.get();
            
            // Verificar si es pública o si el usuario tiene permisos para ver privadas
            if (!imagen.isPublica()) {
                // Aquí podrías agregar lógica de autenticación/autorización
                ctx.status(HttpStatus.FORBIDDEN)
                        .json("No tiene permisos para ver esta imagen");
                return;
            }

            File imageFile = imagenService.getImageFile(imageUrl);
            if (imageFile == null || !imageFile.exists()) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Archivo de imagen no encontrado");
                return;
            }

            // Determinar el tipo de contenido basado en la extensión del archivo
            String contentType = getContentType(fileName);
            ctx.contentType(contentType);
            ctx.result(new FileInputStream(imageFile));

        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al servir la imagen: " + e.getMessage());
        }
    }

    // Obtener imágenes por cuarto
    public void getImagenesByCuarto(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("idCuarto"));
            List<ImagenCuartoPublica> imagenes = imagenService.getImagenesByCuarto(idCuarto);
            ctx.json(imagenes);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener las imágenes: " + e.getMessage());
        }
    }

    // Obtener imágenes públicas por cuarto
    public void getImagenesPublicasByCuarto(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("idCuarto"));
            List<ImagenCuartoPublica> imagenes = imagenService.getImagenesPublicasByCuarto(idCuarto);
            ctx.json(imagenes);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener las imágenes públicas: " + e.getMessage());
        }
    }

    // Obtener imágenes privadas por cuarto
    public void getImagenesPrivadasByCuarto(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("idCuarto"));
            List<ImagenCuartoPublica> imagenes = imagenService.getImagenesPrivadasByCuarto(idCuarto);
            ctx.json(imagenes);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener las imágenes privadas: " + e.getMessage());
        }
    }

    // Actualizar imagen (solo metadatos, no el archivo)
    public void updateImagen(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            ImagenCuartoPublica imagen = objectMapper.readValue(ctx.body(), ImagenCuartoPublica.class);

            ImagenCuartoPublica updatedImagen = imagenService.updateImagen(id, imagen);
            ctx.json(updatedImagen);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de imagen inválido");
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al actualizar la imagen: " + e.getMessage());
        }
    }

    // Eliminar imagen
    public void deleteImagen(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            
            // Verificar si la imagen existe antes de eliminarla
            Optional<ImagenCuartoPublica> imagen = imagenService.getImagenById(id);
            if (imagen.isEmpty()) {
                ctx.status(HttpStatus.NOT_FOUND)
                        .json("Imagen no encontrada con ID: " + id);
                return;
            }

            imagenService.deleteImagen(id);
            ctx.status(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de imagen inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al eliminar la imagen: " + e.getMessage());
        }
    }

    // Eliminar todas las imágenes de un cuarto
    public void deleteImagenesByCuarto(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("idCuarto"));
            int deletedCount = imagenService.deleteByCuarto(idCuarto);
            ctx.json(new DeleteResponse(deletedCount, "Imágenes eliminadas: " + deletedCount));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al eliminar las imágenes: " + e.getMessage());
        }
    }

    // Actualizar visibilidad de todas las imágenes de un cuarto
    public void updateVisibilidadByCuarto(Context ctx) {
        try {
            Integer idCuarto = Integer.parseInt(ctx.pathParam("idCuarto"));
            String body = ctx.body();
            String esPublicaStr = extractJsonValue(body, "esPublica");
            
            if (esPublicaStr == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json("El campo 'esPublica' es requerido");
                return;
            }

            Boolean esPublica = Boolean.parseBoolean(esPublicaStr);
            int updatedCount = imagenService.updateVisibilidadByCuarto(idCuarto, esPublica);
            ctx.json(new UpdateResponse(updatedCount, "Imágenes actualizadas: " + updatedCount));
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al actualizar la visibilidad: " + e.getMessage());
        }
    }

    // Verificar si existe imagen por URL
    public void existsByUrl(Context ctx) {
        try {
            String url = ctx.pathParam("url");
            boolean exists = imagenService.existsByUrl(url);
            ctx.json(new ExistsResponse(exists));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al verificar la imagen: " + e.getMessage());
        }
    }

    // Obtener estadísticas de imágenes
    public void getStats(Context ctx) {
        try {
            ImagenCuartoPublicaService.ImagenStats stats = imagenService.getStats();
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
            ImagenCuartoPublicaService.ImagenStats stats = imagenService.getStatsByCuarto(idCuarto);
            ctx.json(stats);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("ID de cuarto inválido");
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al obtener estadísticas: " + e.getMessage());
        }
    }

    // Contar imágenes
    public void countImagenes(Context ctx) {
        try {
            Long count = imagenService.countImagenes();
            ctx.json(new CountResponse(count));
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json("Error al contar las imágenes: " + e.getMessage());
        }
    }

    // Método auxiliar para determinar el tipo de contenido
    private String getContentType(String fileName) {
        if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.toLowerCase().endsWith(".png")) {
            return "image/png";
        } else if (fileName.toLowerCase().endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.toLowerCase().endsWith(".webp")) {
            return "image/webp";
        } else if (fileName.toLowerCase().endsWith(".bmp")) {
            return "image/bmp";
        } else {
            return "application/octet-stream";
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

    private static class UpdateResponse {
        private final int updatedCount;
        private final String message;
        
        public UpdateResponse(int updatedCount, String message) {
            this.updatedCount = updatedCount;
            this.message = message;
        }
        
        public int getUpdatedCount() {
            return updatedCount;
        }
        
        public String getMessage() {
            return message;
        }
    }
}