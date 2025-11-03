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
    public CuartoMueble saveCuartoMueble(CuartoMueble cuartoMueble) {
        return cuartoMuebleRepository.save(cuartoMueble);
    }

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

        // Establecer cantidad por defecto si es null
        if (cuartoMueble.getCantidad() == null) {
            cuartoMueble.setCantidad(0);
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

        // Buscar el cuarto mueble existente
        Optional<CuartoMueble> cuartoMuebleExistenteOpt = cuartoMuebleRepository.findById(id);
        if (cuartoMuebleExistenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Cuarto mueble no encontrado con ID: " + id);
        }

        CuartoMueble cuartoMuebleExistente = cuartoMuebleExistenteOpt.get();

        // Actualizar campos permitidos (no se pueden cambiar cuarto ni catálogo)
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

    // Método para obtener cuarto muebles por estado
    public List<CuartoMueble> getCuartoMueblesByEstado(String estado) {
        return cuartoMuebleRepository.findByEstado(estado);
    }

    // Método para obtener cuarto muebles con stock
    public List<CuartoMueble> getCuartoMueblesWithStock() {
        return cuartoMuebleRepository.findWithStock();
    }

    // Método para obtener cuarto muebles sin stock
    public List<CuartoMueble> getCuartoMueblesWithoutStock() {
        return cuartoMuebleRepository.findWithoutStock();
    }

    // Método para obtener cuarto muebles con stock por cuarto
    public List<CuartoMueble> getCuartoMueblesWithStockByCuarto(Integer idCuarto) {
        return cuartoMuebleRepository.findWithStockByCuarto(idCuarto);
    }

    // Método para obtener cuarto muebles sin stock por cuarto
    public List<CuartoMueble> getCuartoMueblesWithoutStockByCuarto(Integer idCuarto) {
        return cuartoMuebleRepository.findWithoutStockByCuarto(idCuarto);
    }

    // Método para verificar si un cuarto mueble existe por ID
    public boolean existsById(Integer id) {
        return cuartoMuebleRepository.findById(id).isPresent();
    }

    // Método para verificar si existe cuarto mueble por cuarto y catálogo
    public boolean existsByCuartoAndCatalogo(Integer idCuarto, Integer idCatalogoMueble) {
        return cuartoMuebleRepository.existsByCuartoAndCatalogo(idCuarto, idCatalogoMueble);
    }

    // Método para verificar si existen cuarto muebles por cuarto
    public boolean existsByCuarto(Integer idCuarto) {
        return cuartoMuebleRepository.existsByCuarto(idCuarto);
    }

    // Método para verificar si existen cuarto muebles por catálogo
    public boolean existsByCatalogoMueble(Integer idCatalogoMueble) {
        return cuartoMuebleRepository.existsByCatalogoMueble(idCatalogoMueble);
    }

    // Método para contar todos los cuarto muebles
    public Long countCuartoMuebles() {
        return cuartoMuebleRepository.count();
    }

    // Método para contar cuarto muebles por cuarto
    public Long countByCuarto(Integer idCuarto) {
        return cuartoMuebleRepository.countByCuarto(idCuarto);
    }

    // Método para contar cuarto muebles por catálogo
    public Long countByCatalogoMueble(Integer idCatalogoMueble) {
        return cuartoMuebleRepository.countByCatalogoMueble(idCatalogoMueble);
    }

    // Método para sumar la cantidad total de muebles por cuarto
    public Long sumCantidadByCuarto(Integer idCuarto) {
        return cuartoMuebleRepository.sumCantidadByCuarto(idCuarto);
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

    // Método para incrementar cantidad
    public boolean incrementarCantidad(Integer idCuartoMueble, Integer incremento) {
        if (incremento == null || incremento <= 0) {
            throw new IllegalArgumentException("El incremento debe ser un número positivo");
        }

        // Verificar que el cuarto mueble exista
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

        // Verificar que el cuarto mueble exista
        Optional<CuartoMueble> cuartoMueble = cuartoMuebleRepository.findById(idCuartoMueble);
        if (cuartoMueble.isEmpty()) {
            throw new IllegalArgumentException("Cuarto mueble no encontrado con ID: " + idCuartoMueble);
        }

        // Verificar que haya suficiente stock
        if (cuartoMueble.get().getCantidad() < decremento) {
            throw new IllegalArgumentException("No hay suficiente stock para realizar el decremento");
        }

        int updated = cuartoMuebleRepository.decrementarCantidad(idCuartoMueble, decremento);
        return updated > 0;
    }

    // Método para eliminar todos los cuarto muebles de un cuarto
    public int deleteByCuarto(Integer idCuarto) {
        return cuartoMuebleRepository.deleteByCuarto(idCuarto);
    }

    // Método para agregar o actualizar mueble en cuarto (upsert)
    public CuartoMueble agregarMuebleACuarto(Integer idCuarto, Integer idCatalogoMueble, Integer cantidad, String estado) {
        // Buscar si ya existe
        Optional<CuartoMueble> existente = cuartoMuebleRepository.findByCuartoAndCatalogo(idCuarto, idCatalogoMueble);
        
        if (existente.isPresent()) {
            // Si existe, actualizar cantidad
            CuartoMueble cuartoMueble = existente.get();
            if (cantidad != null) {
                cuartoMueble.setCantidad(cantidad);
            }
            if (estado != null) {
                cuartoMueble.setEstado(estado);
            }
            return cuartoMuebleRepository.save(cuartoMueble);
        } else {
            // Si no existe, crear nuevo
            CuartoMueble nuevoCuartoMueble = new CuartoMueble(idCuarto, idCatalogoMueble, cantidad, estado);
            return createCuartoMueble(nuevoCuartoMueble);
        }
    }

    // Método para obtener estadísticas de cuarto muebles
    public CuartoMuebleStats getStats() {
        Long totalCuartoMuebles = cuartoMuebleRepository.count();
        Long conStock = (long) cuartoMuebleRepository.findWithStock().size();
        Long sinStock = (long) cuartoMuebleRepository.findWithoutStock().size();
        
        return new CuartoMuebleStats(totalCuartoMuebles, conStock, sinStock);
    }

    // Método para obtener estadísticas por cuarto
    public CuartoMuebleStats getStatsByCuarto(Integer idCuarto) {
        Long totalPorCuarto = cuartoMuebleRepository.countByCuarto(idCuarto);
        Long conStockPorCuarto = (long) cuartoMuebleRepository.findWithStockByCuarto(idCuarto).size();
        Long sinStockPorCuarto = (long) cuartoMuebleRepository.findWithoutStockByCuarto(idCuarto).size();
        Long cantidadTotal = cuartoMuebleRepository.sumCantidadByCuarto(idCuarto);
        
        return new CuartoMuebleStats(totalPorCuarto, conStockPorCuarto, sinStockPorCuarto, cantidadTotal);
    }

    // Clase interna para estadísticas
    public static class CuartoMuebleStats {
        private final Long total;
        private final Long conStock;
        private final Long sinStock;
        private final Long cantidadTotal;

        public CuartoMuebleStats(Long total, Long conStock, Long sinStock) {
            this.total = total;
            this.conStock = conStock;
            this.sinStock = sinStock;
            this.cantidadTotal = null;
        }

        public CuartoMuebleStats(Long total, Long conStock, Long sinStock, Long cantidadTotal) {
            this.total = total;
            this.conStock = conStock;
            this.sinStock = sinStock;
            this.cantidadTotal = cantidadTotal;
        }

        public Long getTotal() {
            return total;
        }

        public Long getConStock() {
            return conStock;
        }

        public Long getSinStock() {
            return sinStock;
        }

        public Long getCantidadTotal() {
            return cantidadTotal;
        }
    }
}