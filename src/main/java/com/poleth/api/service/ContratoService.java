// ContratoService.java
package com.poleth.api.service;

import com.poleth.api.model.Contrato;
import com.poleth.api.repository.ContratoRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ContratoService {
    private final ContratoRepository contratoRepository;

    public ContratoService(ContratoRepository contratoRepository) {
        this.contratoRepository = contratoRepository;
    }

    // Métodos CRUD básicos - CORREGIDOS
    public Contrato saveContrato(Contrato contrato) {
        Contrato contratoGuardado = contratoRepository.save(contrato);
        // Recargar con relaciones COMPLETAS para evitar LazyInitializationException
        return contratoRepository.findById(contratoGuardado.getIdContrato())
                .orElseThrow(() -> new RuntimeException("Error al recuperar el contrato guardado"));
    }

    public List<Contrato> getAllContratos() {
        return contratoRepository.findAll();
    }

    public Optional<Contrato> getContratoById(Integer id) {
        return contratoRepository.findById(id);
    }

    public void deleteContrato(Integer id) {
        contratoRepository.delete(id);
    }

    // Método para contar todos los contratos
    public Long countContratos() {
        return contratoRepository.count();
    }

    // Método para crear un nuevo contrato con validaciones - CORREGIDO
    public Contrato createContrato(Contrato contrato) {
        // Validaciones básicas
        if (contrato.getIdCuarto() == null) {
            throw new IllegalArgumentException("El ID del cuarto es requerido");
        }

        if (contrato.getIdInquilino() == null) {
            throw new IllegalArgumentException("El ID del inquilino es requerido");
        }

        if (contrato.getFechaInicio() == null) {
            throw new IllegalArgumentException("La fecha de inicio es requerida");
        }

        // Validar que la fecha de inicio no sea en el pasado
        if (contrato.getFechaInicio().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser en el pasado");
        }

        // Validar fechas si ambas están presentes
        if (contrato.getFechaFinalizacion() != null && contrato.getFechaInicio() != null) {
            if (contrato.getFechaFinalizacion().isBefore(contrato.getFechaInicio())) {
                throw new IllegalArgumentException("La fecha de finalización no puede ser anterior a la fecha de inicio");
            }
        }

        // Validar monto no negativo
        if (contrato.getMontoRentaAcordada() != null && contrato.getMontoRentaAcordada().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto de renta no puede ser negativo");
        }

        // Validar longitud del estado
        if (contrato.getEstadoContrato() != null && contrato.getEstadoContrato().length() > 50) {
            throw new IllegalArgumentException("El estado del contrato no puede exceder 50 caracteres");
        }

        // Verificar si el cuarto ya tiene un contrato activo
        if (contratoRepository.existsContratoActivoByCuarto(contrato.getIdCuarto())) {
            throw new IllegalArgumentException("El cuarto ya tiene un contrato activo");
        }

        // Verificar si el inquilino ya tiene un contrato activo
        if (contratoRepository.existsContratoActivoByInquilino(contrato.getIdInquilino())) {
            throw new IllegalArgumentException("El inquilino ya tiene un contrato activo");
        }

        // Guardar el contrato
        Contrato contratoGuardado = contratoRepository.save(contrato);
        
        // Recargar con relaciones COMPLETAS para evitar LazyInitializationException
        return contratoRepository.findById(contratoGuardado.getIdContrato())
                .orElseThrow(() -> new RuntimeException("Error al recuperar el contrato creado"));
    }

    // Método para actualizar un contrato existente - CORREGIDO
    public Contrato updateContrato(Integer id, Contrato contratoActualizado) {
        // Validaciones básicas
        if (contratoActualizado.getFechaInicio() == null) {
            throw new IllegalArgumentException("La fecha de inicio es requerida");
        }

        // Validar fechas si ambas están presentes
        if (contratoActualizado.getFechaFinalizacion() != null && contratoActualizado.getFechaInicio() != null) {
            if (contratoActualizado.getFechaFinalizacion().isBefore(contratoActualizado.getFechaInicio())) {
                throw new IllegalArgumentException("La fecha de finalización no puede ser anterior a la fecha de inicio");
            }
        }

        // Validar monto no negativo
        if (contratoActualizado.getMontoRentaAcordada() != null && 
            contratoActualizado.getMontoRentaAcordada().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto de renta no puede ser negativo");
        }

        // Validar longitud del estado
        if (contratoActualizado.getEstadoContrato() != null && 
            contratoActualizado.getEstadoContrato().length() > 50) {
            throw new IllegalArgumentException("El estado del contrato no puede exceder 50 caracteres");
        }

        // Buscar el contrato existente
        Optional<Contrato> contratoExistenteOpt = contratoRepository.findById(id);
        if (contratoExistenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Contrato no encontrado con ID: " + id);
        }

        Contrato contratoExistente = contratoExistenteOpt.get();

        // Verificar conflictos con otros contratos activos (solo si se cambia el cuarto o inquilino)
        if (!contratoExistente.getIdCuarto().equals(contratoActualizado.getIdCuarto()) &&
            contratoRepository.existsContratoActivoByCuarto(contratoActualizado.getIdCuarto())) {
            throw new IllegalArgumentException("El nuevo cuarto ya tiene un contrato activo");
        }

        if (!contratoExistente.getIdInquilino().equals(contratoActualizado.getIdInquilino()) &&
            contratoRepository.existsContratoActivoByInquilino(contratoActualizado.getIdInquilino())) {
            throw new IllegalArgumentException("El nuevo inquilino ya tiene un contrato activo");
        }

        // Actualizar los campos
        contratoExistente.setIdCuarto(contratoActualizado.getIdCuarto());
        contratoExistente.setIdInquilino(contratoActualizado.getIdInquilino());
        contratoExistente.setFechaInicio(contratoActualizado.getFechaInicio());
        contratoExistente.setFechaFinalizacion(contratoActualizado.getFechaFinalizacion());
        contratoExistente.setFechaPagoEstablecida(contratoActualizado.getFechaPagoEstablecida());
        contratoExistente.setEstadoContrato(contratoActualizado.getEstadoContrato());
        contratoExistente.setMontoRentaAcordada(contratoActualizado.getMontoRentaAcordada());

        // Guardar los cambios
        contratoRepository.save(contratoExistente);
        
        // Recargar con relaciones COMPLETAS para evitar LazyInitializationException
        return contratoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error al recuperar el contrato actualizado"));
    }

    // Método para verificar si un contrato existe por ID
    public boolean existsById(Integer id) {
        return contratoRepository.findById(id).isPresent();
    }

    // Métodos específicos para Contrato
    public List<Contrato> getContratosByCuarto(Integer idCuarto) {
        return contratoRepository.findByIdCuarto(idCuarto);
    }

    public List<Contrato> getContratosByInquilino(Integer idInquilino) {
        return contratoRepository.findByIdInquilino(idInquilino);
    }

    public List<Contrato> getContratosByEstado(String estadoContrato) {
        return contratoRepository.findByEstadoContrato(estadoContrato);
    }

    public List<Contrato> getContratosActivos() {
        return contratoRepository.findContratosActivos();
    }

    public List<Contrato> getContratosByFechaInicioBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        return contratoRepository.findByFechaInicioBetween(fechaInicio, fechaFin);
    }

    public List<Contrato> getContratosProximosAExpirar(int dias) {
        return contratoRepository.findContratosProximosAExpirar(dias);
    }

    public List<Contrato> getContratosWithRelations() {
        return contratoRepository.findAllWithRelations();
    }

    public Optional<Contrato> getContratoByIdWithRelations(Integer id) {
        return contratoRepository.findByIdWithRelations(id);
    }

    // Métodos de verificación
    public boolean existsContratoActivoByCuarto(Integer idCuarto) {
        return contratoRepository.existsContratoActivoByCuarto(idCuarto);
    }

    public boolean existsContratoActivoByInquilino(Integer idInquilino) {
        return contratoRepository.existsContratoActivoByInquilino(idInquilino);
    }

    public boolean existsByCuartoAndInquilino(Integer idCuarto, Integer idInquilino) {
        return contratoRepository.existsByCuartoAndInquilino(idCuarto, idInquilino);
    }

    // Métodos de conteo
    public Long countContratosActivos() {
        return contratoRepository.countContratosActivos();
    }

    public Long countContratosByEstado(String estadoContrato) {
        return contratoRepository.countByEstado(estadoContrato);
    }

    // Métodos de gestión - CORREGIDOS
    public Contrato finalizarContrato(Integer idContrato, LocalDate fechaFinalizacion) {
        Optional<Contrato> contratoOpt = contratoRepository.findById(idContrato);
        if (contratoOpt.isEmpty()) {
            throw new IllegalArgumentException("Contrato no encontrado con ID: " + idContrato);
        }

        Contrato contrato = contratoOpt.get();

        // Validar que la fecha de finalización no sea anterior a la fecha de inicio
        if (fechaFinalizacion.isBefore(contrato.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de finalización no puede ser anterior a la fecha de inicio");
        }

        contrato.setFechaFinalizacion(fechaFinalizacion);
        contrato.setEstadoContrato("Finalizado");

        // Guardar y recargar con relaciones COMPLETAS
        contratoRepository.save(contrato);
        return contratoRepository.findById(idContrato)
                .orElseThrow(() -> new RuntimeException("Error al recuperar el contrato actualizado"));
    }

    public Contrato actualizarEstadoContrato(Integer idContrato, String nuevoEstado) {
        Optional<Contrato> contratoOpt = contratoRepository.findById(idContrato);
        if (contratoOpt.isEmpty()) {
            throw new IllegalArgumentException("Contrato no encontrado con ID: " + idContrato);
        }

        // Validar longitud del estado
        if (nuevoEstado != null && nuevoEstado.length() > 50) {
            throw new IllegalArgumentException("El estado del contrato no puede exceder 50 caracteres");
        }

        Contrato contrato = contratoOpt.get();
        contrato.setEstadoContrato(nuevoEstado);

        // Guardar y recargar con relaciones COMPLETAS
        contratoRepository.save(contrato);
        return contratoRepository.findById(idContrato)
                .orElseThrow(() -> new RuntimeException("Error al recuperar el contrato actualizado"));
    }

    public Contrato actualizarMontoRenta(Integer idContrato, BigDecimal nuevoMonto) {
        Optional<Contrato> contratoOpt = contratoRepository.findById(idContrato);
        if (contratoOpt.isEmpty()) {
            throw new IllegalArgumentException("Contrato no encontrado con ID: " + idContrato);
        }

        // Validar monto no negativo
        if (nuevoMonto != null && nuevoMonto.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto de renta no puede ser negativo");
        }

        Contrato contrato = contratoOpt.get();
        contrato.setMontoRentaAcordada(nuevoMonto);

        // Guardar y recargar con relaciones COMPLETAS
        contratoRepository.save(contrato);
        return contratoRepository.findById(idContrato)
                .orElseThrow(() -> new RuntimeException("Error al recuperar el contrato actualizado"));
    }

    // Método para obtener contratos con paginación (simplificado)
    public List<Contrato> getContratosPaginados(int pagina, int tamaño) {
        List<Contrato> todosContratos = contratoRepository.findAll();
        int inicio = Math.max(0, (pagina - 1) * tamaño);
        int fin = Math.min(todosContratos.size(), inicio + tamaño);
        
        if (inicio >= todosContratos.size()) {
            return List.of();
        }
        
        return todosContratos.subList(inicio, fin);
    }

    // Método para obtener contratos activos paginados
    public List<Contrato> getContratosActivosPaginados(int pagina, int tamaño) {
        List<Contrato> contratosActivos = contratoRepository.findContratosActivos();
        int inicio = Math.max(0, (pagina - 1) * tamaño);
        int fin = Math.min(contratosActivos.size(), inicio + tamaño);
        
        if (inicio >= contratosActivos.size()) {
            return List.of();
        }
        
        return contratosActivos.subList(inicio, fin);
    }

    // Método para verificar si un contrato está activo
    public boolean isContratoActivo(Integer idContrato) {
        Optional<Contrato> contratoOpt = contratoRepository.findById(idContrato);
        if (contratoOpt.isEmpty()) {
            return false;
        }

        Contrato contrato = contratoOpt.get();
        return contrato.getFechaFinalizacion() == null || 
               contrato.getFechaFinalizacion().isAfter(LocalDate.now());
    }
}