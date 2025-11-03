// CatalogoMuebleService.java
package com.poleth.api.service;

import com.poleth.api.model.CatalogoMueble;
import com.poleth.api.repository.CatalogoMuebleRepository;
import java.util.List;
import java.util.Optional;

public class CatalogoMuebleService {
    private final CatalogoMuebleRepository catalogoMuebleRepository;

    public CatalogoMuebleService(CatalogoMuebleRepository catalogoMuebleRepository) {
        this.catalogoMuebleRepository = catalogoMuebleRepository;
    }

    // Métodos CRUD básicos
    public CatalogoMueble saveCatalogoMueble(CatalogoMueble catalogoMueble) {
        return catalogoMuebleRepository.save(catalogoMueble);
    }

    public List<CatalogoMueble> getAllCatalogoMuebles() {
        return catalogoMuebleRepository.findAll();
    }

    public Optional<CatalogoMueble> getCatalogoMuebleById(Integer id) {
        return catalogoMuebleRepository.findById(id);
    }

    public void deleteCatalogoMueble(Integer id) {
        catalogoMuebleRepository.delete(id);
    }

    public boolean existsByNombreMueble(String nombreMueble) {
        return catalogoMuebleRepository.existsByNombreMueble(nombreMueble);
    }

    // Método para contar todos los muebles del catálogo
    public Long countCatalogoMuebles() {
        return catalogoMuebleRepository.count();
    }

    // Método para crear un nuevo mueble en el catálogo con validaciones
    public CatalogoMueble createCatalogoMueble(CatalogoMueble catalogoMueble) {
        // Validaciones básicas
        if (catalogoMueble.getNombreMueble() == null || catalogoMueble.getNombreMueble().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del mueble es requerido");
        }

        // Validar longitud de campos
        if (catalogoMueble.getNombreMueble().length() > 100) {
            throw new IllegalArgumentException("El nombre del mueble no puede exceder 100 caracteres");
        }

        // Verificar si el nombre del mueble ya existe
        if (existsByNombreMueble(catalogoMueble.getNombreMueble())) {
            throw new IllegalArgumentException("El mueble '" + catalogoMueble.getNombreMueble() + "' ya existe en el catálogo");
        }

        // Guardar el mueble
        return catalogoMuebleRepository.save(catalogoMueble);
    }

    // Método para actualizar un mueble existente en el catálogo
    public CatalogoMueble updateCatalogoMueble(Integer id, CatalogoMueble catalogoMuebleActualizado) {
        // Validaciones básicas
        if (catalogoMuebleActualizado.getNombreMueble() == null || catalogoMuebleActualizado.getNombreMueble().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del mueble es requerido");
        }

        // Validar longitud de campos
        if (catalogoMuebleActualizado.getNombreMueble().length() > 100) {
            throw new IllegalArgumentException("El nombre del mueble no puede exceder 100 caracteres");
        }

        // Buscar el mueble existente
        Optional<CatalogoMueble> muebleExistenteOpt = catalogoMuebleRepository.findById(id);
        if (muebleExistenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Mueble no encontrado en el catálogo con ID: " + id);
        }

        CatalogoMueble muebleExistente = muebleExistenteOpt.get();

        // Verificar si el nuevo nombre ya existe (excluyendo el mueble actual)
        Optional<CatalogoMueble> muebleConMismoNombre = catalogoMuebleRepository.findByNombreMueble(catalogoMuebleActualizado.getNombreMueble());
        if (muebleConMismoNombre.isPresent() && !muebleConMismoNombre.get().getIdCatalogoMueble().equals(id)) {
            throw new IllegalArgumentException("El mueble '" + catalogoMuebleActualizado.getNombreMueble() + "' ya existe en el catálogo");
        }

        // Actualizar los campos
        muebleExistente.setNombreMueble(catalogoMuebleActualizado.getNombreMueble());
        muebleExistente.setDescripcion(catalogoMuebleActualizado.getDescripcion());

        // Guardar los cambios
        return catalogoMuebleRepository.save(muebleExistente);
    }

    // Método para verificar si un mueble existe por ID
    public boolean existsById(Integer id) {
        return catalogoMuebleRepository.existsById(id);
    }

    // Métodos específicos para CatalogoMueble
    public Optional<CatalogoMueble> getCatalogoMuebleByNombre(String nombreMueble) {
        return catalogoMuebleRepository.findByNombreMueble(nombreMueble);
    }

    public List<CatalogoMueble> getCatalogoMueblesByNombreContaining(String texto) {
        return catalogoMuebleRepository.findByNombreContaining(texto);
    }

    public List<CatalogoMueble> getCatalogoMueblesByDescripcionContaining(String texto) {
        return catalogoMuebleRepository.findByDescripcionContaining(texto);
    }

    public List<CatalogoMueble> getCatalogoMueblesWithDescripcion() {
        return catalogoMuebleRepository.findWithDescripcion();
    }

    public List<CatalogoMueble> getCatalogoMueblesWithoutDescripcion() {
        return catalogoMuebleRepository.findWithoutDescripcion();
    }

    public List<CatalogoMueble> getCatalogoMueblesOrderByNombre() {
        return catalogoMuebleRepository.findAllOrderByNombre();
    }

    // Método para actualizar solo la descripción de un mueble
    public CatalogoMueble actualizarDescripcionMueble(Integer id, String nuevaDescripcion) {
        Optional<CatalogoMueble> muebleOpt = catalogoMuebleRepository.findById(id);
        if (muebleOpt.isEmpty()) {
            throw new IllegalArgumentException("Mueble no encontrado en el catálogo con ID: " + id);
        }

        CatalogoMueble mueble = muebleOpt.get();
        mueble.setDescripcion(nuevaDescripcion);

        return catalogoMuebleRepository.save(mueble);
    }

    // Método para eliminar la descripción de un mueble
    public CatalogoMueble eliminarDescripcionMueble(Integer id) {
        Optional<CatalogoMueble> muebleOpt = catalogoMuebleRepository.findById(id);
        if (muebleOpt.isEmpty()) {
            throw new IllegalArgumentException("Mueble no encontrado en el catálogo con ID: " + id);
        }

        CatalogoMueble mueble = muebleOpt.get();
        mueble.setDescripcion(null);

        return catalogoMuebleRepository.save(mueble);
    }

    // Método para obtener muebles con paginación (simplificado)
    public List<CatalogoMueble> getCatalogoMueblesPaginados(int pagina, int tamaño) {
        List<CatalogoMueble> todosMuebles = catalogoMuebleRepository.findAll();
        int inicio = Math.max(0, (pagina - 1) * tamaño);
        int fin = Math.min(todosMuebles.size(), inicio + tamaño);
        
        if (inicio >= todosMuebles.size()) {
            return List.of();
        }
        
        return todosMuebles.subList(inicio, fin);
    }

    // Método para buscar muebles por nombre ordenados y paginados
    public List<CatalogoMueble> getCatalogoMueblesByNombreContainingPaginados(String texto, int pagina, int tamaño) {
        List<CatalogoMueble> mueblesFiltrados = catalogoMuebleRepository.findByNombreContaining(texto);
        int inicio = Math.max(0, (pagina - 1) * tamaño);
        int fin = Math.min(mueblesFiltrados.size(), inicio + tamaño);
        
        if (inicio >= mueblesFiltrados.size()) {
            return List.of();
        }
        
        return mueblesFiltrados.subList(inicio, fin);
    }

    // Método para verificar y obtener mueble por nombre (útil para validaciones)
    public CatalogoMueble getOrThrowByNombre(String nombreMueble) {
        Optional<CatalogoMueble> muebleOpt = catalogoMuebleRepository.findByNombreMueble(nombreMueble);
        if (muebleOpt.isEmpty()) {
            throw new IllegalArgumentException("Mueble no encontrado con nombre: " + nombreMueble);
        }
        return muebleOpt.get();
    }
}