// FileUploadConfig.java
package com.poleth.api.config;

import java.io.File;

public class FileUploadConfig {
    private static final String UPLOAD_DIR = "uploads/";
    private static final String IMAGES_DIR = UPLOAD_DIR + "images/";
    
    static {
        // Crear directorios si no existen
        new File(IMAGES_DIR).mkdirs();
    }
    
    public static String getUploadDir() {
        return UPLOAD_DIR;
    }
    
    public static String getImagesDir() {
        return IMAGES_DIR;
    }
    
    public static String generateImageFileName(String originalFileName, Integer idCuarto) {
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        return "cuarto_" + idCuarto + "_" + System.currentTimeMillis() + extension;
    }
    
    public static String getImageUrl(String fileName) {
        return "/api/images/" + fileName;
    }
}