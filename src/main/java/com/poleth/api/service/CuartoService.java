// CuartoService.java
package com.poleth.api.service;

import com.poleth.api.model.Cuarto;
import com.poleth.api.repository.CuartoRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class CuartoService {
    private final CuartoRepository cuartoRepository;

    public CuartoService(CuartoRepository cuartoRepository) {
        this.cuartoRepository = cuartoRepository;
    }

    // Métodos CRUD básicos
    public List<Cuarto> getAllCuartos() {
        return cuartoRepository.findAll();
    }

    public Optional<Cuarto> getCuartoById(Integer id) {
        return cuartoRepository.findById(id);
    }

    public void deleteCuarto(Integer id) {
        cuartoRepository.delete(id);
    }

    // Método para crear un nuevo cuarto con validaciones
    public Cuarto createCuarto(Cuarto cuarto) {
        // Validaciones básicas
        if (cuarto.getIdPropietario() == null) {
            throw new IllegalArgumentException("El ID del propietario es requerido");
        }

        if (cuarto.getNombreCuarto() == null || cuarto.getNombreCuarto().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cuarto es requerido");
        }

        // Validar longitud de campos
        if (cuarto.getNombreCuarto().length() > 100) {
            throw new IllegalArgumentException("El nombre del cuarto no puede exceder 100 caracteres");
        }

        if (cuarto.getEstadoCuarto() != null && cuarto.getEstadoCuarto().length() > 50) {
            throw new IllegalArgumentException("El estado del cuarto no puede exceder 50 caracteres");
        }

        // Validar precio no negativo
        if (cuarto.getPrecioAlquiler() != null && cuarto.getPrecioAlquiler().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio de alquiler no puede ser negativo");
        }

        // Establecer estado por defecto si es null
        if (cuarto.getEstadoCuarto() == null || cuarto.getEstadoCuarto().isEmpty()) {
            cuarto.setEstadoCuarto("disponible");
        }

        // Establecer precio por defecto si es null
        if (cuarto.getPrecioAlquiler() == null) {
            cuarto.setPrecioAlquiler(BigDecimal.ZERO);
        }

        // Verificar si ya existe un cuarto con el mismo nombre para el mismo propietario
        if (cuartoRepository.existsByNombreAndPropietario(cuarto.getNombreCuarto(), cuarto.getIdPropietario())) {
            throw new IllegalArgumentException("Ya existe un cuarto con el nombre '" + cuarto.getNombreCuarto() + "' para este propietario");
        }

        // Guardar el cuarto
        Cuarto cuartoGuardado = cuartoRepository.save(cuarto);

        // Recargar con relaciones para evitar LazyInitializationException
        return cuartoRepository.findById(cuartoGuardado.getIdCuarto())
                .orElseThrow(() -> new RuntimeException("Error al recuperar el cuarto creado"));
    }

    // Método para actualizar un cuarto existente
    public Cuarto updateCuarto(Integer id, Cuarto cuartoActualizado) {
        // Validaciones básicas
        if (cuartoActualizado.getNombreCuarto() == null || cuartoActualizado.getNombreCuarto().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cuarto es requerido");
        }

        // Validar longitud de campos
        if (cuartoActualizado.getNombreCuarto().length() > 100) {
            throw new IllegalArgumentException("El nombre del cuarto no puede exceder 100 caracteres");
        }

        if (cuartoActualizado.getEstadoCuarto() != null && cuartoActualizado.getEstadoCuarto().length() > 50) {
            throw new IllegalArgumentException("El estado del cuarto no puede exceder 50 caracteres");
        }

        // Validar precio no negativo
        if (cuartoActualizado.getPrecioAlquiler() != null && cuartoActualizado.getPrecioAlquiler().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio de alquiler no puede ser negativo");
        }

        // Buscar el cuarto existente
        Optional<Cuarto> cuartoExistenteOpt = cuartoRepository.findById(id);
        if (cuartoExistenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Cuarto no encontrado con ID: " + id);
        }

        Cuarto cuartoExistente = cuartoExistenteOpt.get();

        // Verificar si el nuevo nombre ya existe para el mismo propietario (excluyendo el cuarto actual)
        if (!cuartoExistente.getNombreCuarto().equals(cuartoActualizado.getNombreCuarto()) &&
                cuartoRepository.existsByNombreAndPropietario(cuartoActualizado.getNombreCuarto(), cuartoExistente.getIdPropietario())) {
            throw new IllegalArgumentException("Ya existe un cuarto con el nombre '" + cuartoActualizado.getNombreCuarto() + "' para este propietario");
        }

        // Actualizar los campos (no actualizamos idPropietario ni createdAt)
        cuartoExistente.setNombreCuarto(cuartoActualizado.getNombreCuarto());
        cuartoExistente.setPrecioAlquiler(cuartoActualizado.getPrecioAlquiler());
        cuartoExistente.setEstadoCuarto(cuartoActualizado.getEstadoCuarto());
        cuartoExistente.setDescripcionCuarto(cuartoActualizado.getDescripcionCuarto());

        // Guardar los cambios
        cuartoRepository.save(cuartoExistente);

        // Obtener el cuarto actualizado CON la relación propietario cargada
        return cuartoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error al recuperar el cuarto actualizado"));
    }

    // Método para verificar si un cuarto existe por ID
    public boolean existsById(Integer id) {
        return cuartoRepository.existsById(id);
    }

    // Método para obtener cuartos por propietario
    public List<Cuarto> getCuartosByPropietario(Integer idPropietario) {
        return cuartoRepository.findByIdPropietario(idPropietario);
    }

    // Método para cambiar el estado de un cuarto
    public Cuarto cambiarEstadoCuarto(Integer idCuarto, String nuevoEstado) {
        Optional<Cuarto> cuartoOpt = cuartoRepository.findById(idCuarto);
        if (cuartoOpt.isEmpty()) {
            throw new IllegalArgumentException("Cuarto no encontrado con ID: " + idCuarto);
        }

        // Validar longitud del estado
        if (nuevoEstado != null && nuevoEstado.length() > 50) {
            throw new IllegalArgumentException("El estado del cuarto no puede exceder 50 caracteres");
        }

        Cuarto cuarto = cuartoOpt.get();
        cuarto.setEstadoCuarto(nuevoEstado);

        // Guardar y luego recuperar con la relación cargada
        cuartoRepository.save(cuarto);
        return cuartoRepository.findById(idCuarto)
                .orElseThrow(() -> new RuntimeException("Error al recuperar el cuarto actualizado"));
    }

    // Método para actualizar el precio de un cuarto
    public Cuarto actualizarPrecioCuarto(Integer idCuarto, BigDecimal nuevoPrecio) {
        Optional<Cuarto> cuartoOpt = cuartoRepository.findById(idCuarto);
        if (cuartoOpt.isEmpty()) {
            throw new IllegalArgumentException("Cuarto no encontrado con ID: " + idCuarto);
        }

        // Validar precio no negativo
        if (nuevoPrecio != null && nuevoPrecio.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio de alquiler no puede ser negativo");
        }

        Cuarto cuarto = cuartoOpt.get();
        cuarto.setPrecioAlquiler(nuevoPrecio);

        // Guardar y luego recuperar con la relación cargada
        cuartoRepository.save(cuarto);
        return cuartoRepository.findById(idCuarto)
                .orElseThrow(() -> new RuntimeException("Error al recuperar el cuarto actualizado"));
    }

    // Método para obtener cuartos disponibles
    public List<Cuarto> getCuartosDisponibles() {
        return cuartoRepository.findAvailable();
    }

    // Método para buscar cuartos por estado
    public List<Cuarto> getCuartosPorEstado(String estado) {
        return cuartoRepository.findByEstado(estado);
    }
}