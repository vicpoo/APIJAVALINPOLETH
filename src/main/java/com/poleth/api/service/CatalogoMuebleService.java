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

        if (catalogoMueble.getEstadoMueble() != null && catalogoMueble.getEstadoMueble().length() > 20) {
            throw new IllegalArgumentException("El estado del mueble no puede exceder 20 caracteres");
        }

        // Verificar si el nombre del mueble ya existe
        if (catalogoMuebleRepository.existsByNombreMueble(catalogoMueble.getNombreMueble())) {
            throw new IllegalArgumentException("El mueble '" + catalogoMueble.getNombreMueble() + "' ya existe en el catálogo");
        }

        // Establecer estado por defecto si no se proporciona
        if (catalogoMueble.getEstadoMueble() == null) {
            catalogoMueble.setEstadoMueble("activo");
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

        if (catalogoMuebleActualizado.getEstadoMueble() != null && catalogoMuebleActualizado.getEstadoMueble().length() > 20) {
            throw new IllegalArgumentException("El estado del mueble no puede exceder 20 caracteres");
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
        muebleExistente.setEstadoMueble(catalogoMuebleActualizado.getEstadoMueble());

        // Guardar los cambios
        return catalogoMuebleRepository.save(muebleExistente);
    }

    // Método para verificar si un mueble existe por ID
    public boolean existsById(Integer id) {
        return catalogoMuebleRepository.existsById(id);
    }

    // Métodos específicos para búsquedas
    public Optional<CatalogoMueble> getCatalogoMuebleByNombre(String nombreMueble) {
        return catalogoMuebleRepository.findByNombreMueble(nombreMueble);
    }

    public List<CatalogoMueble> getCatalogoMueblesByNombreContaining(String texto) {
        return catalogoMuebleRepository.findByNombreContaining(texto);
    }

    public List<CatalogoMueble> getCatalogoMueblesByDescripcionContaining(String texto) {
        return catalogoMuebleRepository.findByDescripcionContaining(texto);
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

    // Método para cambiar el estado de un mueble
    public CatalogoMueble cambiarEstadoMueble(Integer id, String nuevoEstado) {
        Optional<CatalogoMueble> muebleOpt = catalogoMuebleRepository.findById(id);
        if (muebleOpt.isEmpty()) {
            throw new IllegalArgumentException("Mueble no encontrado en el catálogo con ID: " + id);
        }

        CatalogoMueble mueble = muebleOpt.get();
        mueble.setEstadoMueble(nuevoEstado);

        return catalogoMuebleRepository.save(mueble);
    }

    // Método para obtener muebles por estado
    public List<CatalogoMueble> getCatalogoMueblesByEstado(String estado) {
        return catalogoMuebleRepository.findByEstado(estado);
    }

    // Método para obtener muebles activos
    public List<CatalogoMueble> getCatalogoMueblesActivos() {
        return catalogoMuebleRepository.findActivos();
    }

    // Método para obtener muebles con descripción
    public List<CatalogoMueble> getCatalogoMueblesConDescripcion() {
        return catalogoMuebleRepository.findWithDescripcion();
    }

    // Método para obtener muebles sin descripción
    public List<CatalogoMueble> getCatalogoMueblesSinDescripcion() {
        return catalogoMuebleRepository.findWithoutDescripcion();
    }
}