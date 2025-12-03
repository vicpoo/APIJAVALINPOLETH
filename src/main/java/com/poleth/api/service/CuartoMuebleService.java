// CuartoMuebleService.java
package com.poleth.api.service;

import com.poleth.api.model.CuartoMueble;
import com.poleth.api.repository.CuartoMuebleRepository;
import java.util.List;
import java.util.Optional;

public class CuartoMuebleService {
    private final CuartoMuebleRepository cuartoMuebleRepository;

    public CuartoMuebleService(CuartoMuebleRepository cuartoMuebleRepository) {
        this.cuartoMuebleRepository = cuartoMuebleRepository;
    }

    // Métodos CRUD básicos
    public List<CuartoMueble> getAllCuartoMuebles() {
        return cuartoMuebleRepository.findAll();
    }

    public Optional<CuartoMueble> getCuartoMuebleById(Integer id) {
        return cuartoMuebleRepository.findById(id);
    }

    public void deleteCuartoMueble(Integer id) {
        cuartoMuebleRepository.delete(id);
    }

    // Método para crear un nuevo cuarto mueble con validaciones
    public CuartoMueble createCuartoMueble(CuartoMueble cuartoMueble) {
        // Validaciones básicas
        if (cuartoMueble.getIdCuarto() == null) {
            throw new IllegalArgumentException("El ID del cuarto es requerido");
        }

        if (cuartoMueble.getIdCatalogoMueble() == null) {
            throw new IllegalArgumentException("El ID del catálogo de mueble es requerido");
        }

        // Validar cantidad
        if (cuartoMueble.getCantidad() != null && cuartoMueble.getCantidad() < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }

        // Validar longitud del estado
        if (cuartoMueble.getEstado() != null && cuartoMueble.getEstado().length() > 50) {
            throw new IllegalArgumentException("El estado no puede exceder 50 caracteres");
        }

        // Verificar si ya existe la combinación cuarto-catálogo
        if (cuartoMuebleRepository.existsByCuartoAndCatalogo(
                cuartoMueble.getIdCuarto(), cuartoMueble.getIdCatalogoMueble())) {
            throw new IllegalArgumentException("Ya existe un mueble de este tipo en el cuarto");
        }

        // Establecer valores por defecto si son null
        if (cuartoMueble.getCantidad() == null) {
            cuartoMueble.setCantidad(0);
        }

        if (cuartoMueble.getEstado() == null || cuartoMueble.getEstado().isEmpty()) {
            cuartoMueble.setEstado("bueno");
        }

        // Validar que el estado sea válido (puedes expandir esta lista)
        String estado = cuartoMueble.getEstado().toLowerCase();
        if (!estado.equals("bueno") && !estado.equals("regular") &&
                !estado.equals("malo") && !estado.equals("necesita reparación")) {
            throw new IllegalArgumentException("Estado inválido. Valores permitidos: 'bueno', 'regular', 'malo', 'necesita reparación'");
        }

        // Guardar el cuarto mueble
        return cuartoMuebleRepository.save(cuartoMueble);
    }

    // Método para actualizar un cuarto mueble existente
    public CuartoMueble updateCuartoMueble(Integer id, CuartoMueble cuartoMuebleActualizado) {
        // Validaciones básicas
        if (cuartoMuebleActualizado.getCantidad() != null && cuartoMuebleActualizado.getCantidad() < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }

        if (cuartoMuebleActualizado.getEstado() != null && cuartoMuebleActualizado.getEstado().length() > 50) {
            throw new IllegalArgumentException("El estado no puede exceder 50 caracteres");
        }

        // Validar estado si se proporciona
        if (cuartoMuebleActualizado.getEstado() != null) {
            String estado = cuartoMuebleActualizado.getEstado().toLowerCase();
            if (!estado.equals("bueno") && !estado.equals("regular") &&
                    !estado.equals("malo") && !estado.equals("necesita reparación")) {
                throw new IllegalArgumentException("Estado inválido. Valores permitidos: 'bueno', 'regular', 'malo', 'necesita reparación'");
            }
        }

        // Buscar el cuarto mueble existente
        Optional<CuartoMueble> cuartoMuebleExistenteOpt = cuartoMuebleRepository.findById(id);
        if (cuartoMuebleExistenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Cuarto mueble no encontrado con ID: " + id);
        }

        CuartoMueble cuartoMuebleExistente = cuartoMuebleExistenteOpt.get();

        // NO se pueden cambiar cuarto ni catálogo (son FK)
        // Actualizar campos permitidos
        if (cuartoMuebleActualizado.getCantidad() != null) {
            cuartoMuebleExistente.setCantidad(cuartoMuebleActualizado.getCantidad());
        }
        if (cuartoMuebleActualizado.getEstado() != null) {
            cuartoMuebleExistente.setEstado(cuartoMuebleActualizado.getEstado());
        }

        // Guardar los cambios
        return cuartoMuebleRepository.save(cuartoMuebleExistente);
    }

    // Método para obtener cuarto muebles por cuarto
    public List<CuartoMueble> getCuartoMueblesByCuarto(Integer idCuarto) {
        return cuartoMuebleRepository.findByCuarto(idCuarto);
    }

    // Método para obtener cuarto muebles por catálogo de mueble
    public List<CuartoMueble> getCuartoMueblesByCatalogo(Integer idCatalogoMueble) {
        return cuartoMuebleRepository.findByCatalogoMueble(idCatalogoMueble);
    }

    // Método para obtener cuarto mueble específico por cuarto y catálogo
    public Optional<CuartoMueble> getCuartoMuebleByCuartoAndCatalogo(Integer idCuarto, Integer idCatalogoMueble) {
        return cuartoMuebleRepository.findByCuartoAndCatalogo(idCuarto, idCatalogoMueble);
    }

    // Método para obtener cuarto muebles con stock
    public List<CuartoMueble> getCuartoMueblesWithStock() {
        return cuartoMuebleRepository.findWithStock();
    }

    // Método para obtener cuarto muebles sin stock
    public List<CuartoMueble> getCuartoMueblesWithoutStock() {
        return cuartoMuebleRepository.findWithoutStock();
    }

    // Método para verificar si un cuarto mueble existe por ID
    public boolean existsById(Integer id) {
        return cuartoMuebleRepository.findById(id).isPresent();
    }

    // Método para actualizar cantidad
    public boolean updateCantidad(Integer idCuartoMueble, Integer nuevaCantidad) {
        if (nuevaCantidad == null || nuevaCantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }

        // Verificar que el cuarto mueble exista
        Optional<CuartoMueble> cuartoMueble = cuartoMuebleRepository.findById(idCuartoMueble);
        if (cuartoMueble.isEmpty()) {
            throw new IllegalArgumentException("Cuarto mueble no encontrado con ID: " + idCuartoMueble);
        }

        int updated = cuartoMuebleRepository.updateCantidad(idCuartoMueble, nuevaCantidad);
        return updated > 0;
    }

    // Método para obtener cuarto muebles por estado
    public List<CuartoMueble> getCuartoMueblesByEstado(String estado) {
        return cuartoMuebleRepository.findByEstado(estado);
    }

    // Método para incrementar cantidad
    public boolean incrementarCantidad(Integer idCuartoMueble, Integer incremento) {
        if (incremento == null || incremento <= 0) {
            throw new IllegalArgumentException("El incremento debe ser un número positivo");
        }

        Optional<CuartoMueble> cuartoMueble = cuartoMuebleRepository.findById(idCuartoMueble);
        if (cuartoMueble.isEmpty()) {
            throw new IllegalArgumentException("Cuarto mueble no encontrado con ID: " + idCuartoMueble);
        }

        int updated = cuartoMuebleRepository.incrementarCantidad(idCuartoMueble, incremento);
        return updated > 0;
    }

    // Método para decrementar cantidad
    public boolean decrementarCantidad(Integer idCuartoMueble, Integer decremento) {
        if (decremento == null || decremento <= 0) {
            throw new IllegalArgumentException("El decremento debe ser un número positivo");
        }

        Optional<CuartoMueble> cuartoMueble = cuartoMuebleRepository.findById(idCuartoMueble);
        if (cuartoMueble.isEmpty()) {
            throw new IllegalArgumentException("Cuarto mueble no encontrado con ID: " + idCuartoMueble);
        }

        int updated = cuartoMuebleRepository.decrementarCantidad(idCuartoMueble, decremento);
        return updated > 0;
    }

    // Método para obtener cuarto muebles con stock por cuarto
    public List<CuartoMueble> getCuartoMueblesWithStockByCuarto(Integer idCuarto) {
        return cuartoMuebleRepository.findWithStockByCuarto(idCuarto);
    }

    // Método para obtener cuarto muebles sin stock por cuarto
    public List<CuartoMueble> getCuartoMueblesWithoutStockByCuarto(Integer idCuarto) {
        return cuartoMuebleRepository.findWithoutStockByCuarto(idCuarto);
    }
}