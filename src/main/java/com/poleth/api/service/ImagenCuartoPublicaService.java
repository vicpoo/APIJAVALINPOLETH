// ImagenCuartoPublicaService.java
package com.poleth.api.service;

import com.poleth.api.model.ImagenCuartoPublica;
import com.poleth.api.repository.ImagenCuartoPublicaRepository;
import com.poleth.api.config.FileUploadConfig;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

public class ImagenCuartoPublicaService {
    private final ImagenCuartoPublicaRepository imagenRepository;

    public ImagenCuartoPublicaService(ImagenCuartoPublicaRepository imagenRepository) {
        this.imagenRepository = imagenRepository;
    }

    // Métodos CRUD básicos
    public ImagenCuartoPublica saveImagen(ImagenCuartoPublica imagen) {
        return imagenRepository.save(imagen);
    }

    public List<ImagenCuartoPublica> getAllImagenes() {
        return imagenRepository.findAll();
    }

    public Optional<ImagenCuartoPublica> getImagenById(Integer id) {
        return imagenRepository.findById(id);
    }

    public void deleteImagen(Integer id) {
        // Obtener la imagen primero para eliminar el archivo físico
        Optional<ImagenCuartoPublica> imagenOpt = imagenRepository.findById(id);
        if (imagenOpt.isPresent()) {
            ImagenCuartoPublica imagen = imagenOpt.get();
            // Eliminar archivo físico
            deletePhysicalFile(imagen.getUrlImagen());
            // Eliminar de la base de datos
            imagenRepository.delete(id);
        }
    }

    public boolean existsByUrl(String urlImagen) {
        return imagenRepository.existsByUrl(urlImagen);
    }

    // Método para subir y guardar una nueva imagen
    public ImagenCuartoPublica uploadAndSaveImagen(Integer idCuarto, String descripcion, 
                                                  Boolean esPublica, InputStream fileStream, 
                                                  String originalFileName) {
        // Validaciones básicas
        if (idCuarto == null) {
            throw new IllegalArgumentException("El ID del cuarto es requerido");
        }

        if (fileStream == null) {
            throw new IllegalArgumentException("El archivo de imagen es requerido");
        }

        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del archivo es requerido");
        }

        // Validar tipo de archivo
        if (!isValidImageFile(originalFileName)) {
            throw new IllegalArgumentException("Tipo de archivo no permitido. Solo se permiten imágenes (JPG, PNG, GIF, WEBP)");
        }

        // Generar nombre único para el archivo
        String fileName = FileUploadConfig.generateImageFileName(originalFileName, idCuarto);
        String filePath = FileUploadConfig.getImagesDir() + fileName;
        String imageUrl = FileUploadConfig.getImageUrl(fileName);

        try {
            // Guardar archivo físicamente
            File targetFile = new File(filePath);
            Files.copy(fileStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Crear y guardar entidad en base de datos
            ImagenCuartoPublica imagen = new ImagenCuartoPublica();
            imagen.setIdCuarto(idCuarto);
            imagen.setUrlImagen(imageUrl);
            imagen.setDescripcion(descripcion);
            imagen.setEsPublica(esPublica != null ? esPublica : true);

            return imagenRepository.save(imagen);

        } catch (Exception e) {
            // Si hay error, eliminar archivo físico si se creó
            File failedFile = new File(filePath);
            if (failedFile.exists()) {
                failedFile.delete();
            }
            throw new RuntimeException("Error al guardar la imagen: " + e.getMessage(), e);
        }
    }

    // Método para actualizar una imagen existente
    public ImagenCuartoPublica updateImagen(Integer id, ImagenCuartoPublica imagenActualizada) {
        // Validaciones básicas
        if (imagenActualizada.getDescripcion() != null && imagenActualizada.getDescripcion().length() > 100) {
            throw new IllegalArgumentException("La descripción no puede exceder 100 caracteres");
        }

        // Buscar la imagen existente
        Optional<ImagenCuartoPublica> imagenExistenteOpt = imagenRepository.findById(id);
        if (imagenExistenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Imagen no encontrada con ID: " + id);
        }

        ImagenCuartoPublica imagenExistente = imagenExistenteOpt.get();

        // Actualizar campos permitidos
        if (imagenActualizada.getDescripcion() != null) {
            imagenExistente.setDescripcion(imagenActualizada.getDescripcion());
        }
        if (imagenActualizada.getEsPublica() != null) {
            imagenExistente.setEsPublica(imagenActualizada.getEsPublica());
        }

        // Guardar los cambios
        return imagenRepository.save(imagenExistente);
    }

    // Método para obtener imágenes por cuarto
    public List<ImagenCuartoPublica> getImagenesByCuarto(Integer idCuarto) {
        return imagenRepository.findByCuarto(idCuarto);
    }

    // Método para obtener imágenes públicas por cuarto
    public List<ImagenCuartoPublica> getImagenesPublicasByCuarto(Integer idCuarto) {
        return imagenRepository.findPublicasByCuarto(idCuarto);
    }

    // Método para obtener imágenes privadas por cuarto
    public List<ImagenCuartoPublica> getImagenesPrivadasByCuarto(Integer idCuarto) {
        return imagenRepository.findPrivadasByCuarto(idCuarto);
    }

    // Método para obtener todas las imágenes públicas
    public List<ImagenCuartoPublica> getAllImagenesPublicas() {
        return imagenRepository.findAllPublicas();
    }

    // Método para obtener todas las imágenes privadas
    public List<ImagenCuartoPublica> getAllImagenesPrivadas() {
        return imagenRepository.findAllPrivadas();
    }

    // Método para buscar imagen por URL
    public Optional<ImagenCuartoPublica> getImagenByUrl(String urlImagen) {
        return imagenRepository.findByUrl(urlImagen);
    }

    // Método para verificar si una imagen existe por ID
    public boolean existsById(Integer id) {
        return imagenRepository.findById(id).isPresent();
    }

    // Método para verificar si existen imágenes para un cuarto
    public boolean existsByCuarto(Integer idCuarto) {
        return imagenRepository.existsByCuarto(idCuarto);
    }

    // Método para contar imágenes por cuarto
    public Long countByCuarto(Integer idCuarto) {
        return imagenRepository.countByCuarto(idCuarto);
    }

    // Método para contar imágenes públicas por cuarto
    public Long countPublicasByCuarto(Integer idCuarto) {
        return imagenRepository.countPublicasByCuarto(idCuarto);
    }

    // Método para contar imágenes privadas por cuarto
    public Long countPrivadasByCuarto(Integer idCuarto) {
        return imagenRepository.countPrivadasByCuarto(idCuarto);
    }

    // Método para contar todas las imágenes
    public Long countImagenes() {
        return imagenRepository.count();
    }

    // Método para eliminar todas las imágenes de un cuarto
    public int deleteByCuarto(Integer idCuarto) {
        // Obtener todas las imágenes del cuarto para eliminar archivos físicos
        List<ImagenCuartoPublica> imagenes = imagenRepository.findByCuarto(idCuarto);
        for (ImagenCuartoPublica imagen : imagenes) {
            deletePhysicalFile(imagen.getUrlImagen());
        }
        
        // Eliminar de la base de datos
        return imagenRepository.deleteByCuarto(idCuarto);
    }

    // Método para actualizar visibilidad de todas las imágenes de un cuarto
    public int updateVisibilidadByCuarto(Integer idCuarto, Boolean esPublica) {
        return imagenRepository.updateVisibilidadByCuarto(idCuarto, esPublica);
    }

    // Método para obtener estadísticas de imágenes
    public ImagenStats getStats() {
        Long totalImagenes = imagenRepository.count();
        Long imagenesPublicas = (long) imagenRepository.findAllPublicas().size();
        Long imagenesPrivadas = (long) imagenRepository.findAllPrivadas().size();
        
        return new ImagenStats(totalImagenes, imagenesPublicas, imagenesPrivadas);
    }

    // Método para obtener estadísticas por cuarto
    public ImagenStats getStatsByCuarto(Integer idCuarto) {
        Long totalImagenes = imagenRepository.countByCuarto(idCuarto);
        Long imagenesPublicas = imagenRepository.countPublicasByCuarto(idCuarto);
        Long imagenesPrivadas = imagenRepository.countPrivadasByCuarto(idCuarto);
        
        return new ImagenStats(totalImagenes, imagenesPublicas, imagenesPrivadas);
    }

    // Método para validar tipo de archivo
    private boolean isValidImageFile(String fileName) {
        if (fileName == null) return false;
        
        String extension = fileName.toLowerCase();
        return extension.endsWith(".jpg") || extension.endsWith(".jpeg") || 
               extension.endsWith(".png") || extension.endsWith(".gif") || 
               extension.endsWith(".webp") || extension.endsWith(".bmp");
    }

    // Método para eliminar archivo físico
    private void deletePhysicalFile(String imageUrl) {
        try {
            if (imageUrl != null && imageUrl.startsWith("/api/images/")) {
                String fileName = imageUrl.substring("/api/images/".length());
                String filePath = FileUploadConfig.getImagesDir() + fileName;
                File file = new File(filePath);
                if (file.exists()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            // Log del error pero no lanzar excepción para no interrumpir el flujo principal
            System.err.println("Error al eliminar archivo físico: " + e.getMessage());
        }
    }

    // Método para servir archivo físico
    public File getImageFile(String imageUrl) {
        if (imageUrl == null || !imageUrl.startsWith("/api/images/")) {
            return null;
        }
        
        String fileName = imageUrl.substring("/api/images/".length());
        String filePath = FileUploadConfig.getImagesDir() + fileName;
        File file = new File(filePath);
        
        return file.exists() ? file : null;
    }

    // Clase interna para estadísticas
    public static class ImagenStats {
        private final Long totalImagenes;
        private final Long imagenesPublicas;
        private final Long imagenesPrivadas;

        public ImagenStats(Long totalImagenes, Long imagenesPublicas, Long imagenesPrivadas) {
            this.totalImagenes = totalImagenes;
            this.imagenesPublicas = imagenesPublicas;
            this.imagenesPrivadas = imagenesPrivadas;
        }

        public Long getTotalImagenes() {
            return totalImagenes;
        }

        public Long getImagenesPublicas() {
            return imagenesPublicas;
        }

        public Long getImagenesPrivadas() {
            return imagenesPrivadas;
        }
    }
}