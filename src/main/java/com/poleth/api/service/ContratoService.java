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

    // Métodos CRUD básicos
    public List<Contrato> getAllContratos() {
        return contratoRepository.findAll();
    }

    public Optional<Contrato> getContratoById(Integer id) {
        return contratoRepository.findById(id);
    }

    public void deleteContrato(Integer id) {
        contratoRepository.delete(id);
    }

    // Método para crear un nuevo contrato con validaciones
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
        if (contrato.getFechaFinalizacion() != null) {
            if (contrato.getFechaFinalizacion().isBefore(contrato.getFechaInicio())) {
                throw new IllegalArgumentException("La fecha de finalización no puede ser anterior a la fecha de inicio");
            }
        }

        // Validar monto no negativo
        if (contrato.getMontoRentaAcordada() != null &&
                contrato.getMontoRentaAcordada().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto de renta no puede ser negativo");
        }

        // Validar longitud del estado
        if (contrato.getEstadoContrato() != null &&
                contrato.getEstadoContrato().length() > 50) {
            throw new IllegalArgumentException("El estado del contrato no puede exceder 50 caracteres");
        }

        // Validar estado si se proporciona
        if (contrato.getEstadoContrato() != null) {
            String estado = contrato.getEstadoContrato().toLowerCase();
            if (!estado.equals("activo") && !estado.equals("finalizado") &&
                    !estado.equals("cancelado") && !estado.equals("suspendido")) {
                throw new IllegalArgumentException("Estado inválido. Valores permitidos: 'activo', 'finalizado', 'cancelado', 'suspendido'");
            }
        } else {
            // Establecer estado por defecto
            contrato.setEstadoContrato("activo");
        }

        // Establecer monto por defecto si es null
        if (contrato.getMontoRentaAcordada() == null) {
            contrato.setMontoRentaAcordada(BigDecimal.ZERO);
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

        // Recargar con relaciones para evitar LazyInitializationException
        return contratoRepository.findById(contratoGuardado.getIdContrato())
                .orElseThrow(() -> new RuntimeException("Error al recuperar el contrato creado"));
    }

    // Método para actualizar un contrato existente
    public Contrato updateContrato(Integer id, Contrato contratoActualizado) {
        // Validaciones básicas
        if (contratoActualizado.getFechaInicio() == null) {
            throw new IllegalArgumentException("La fecha de inicio es requerida");
        }

        // Validar fechas si ambas están presentes
        if (contratoActualizado.getFechaFinalizacion() != null) {
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

        // Validar estado si se proporciona
        if (contratoActualizado.getEstadoContrato() != null) {
            String estado = contratoActualizado.getEstadoContrato().toLowerCase();
            if (!estado.equals("activo") && !estado.equals("finalizado") &&
                    !estado.equals("cancelado") && !estado.equals("suspendido")) {
                throw new IllegalArgumentException("Estado inválido. Valores permitidos: 'activo', 'finalizado', 'cancelado', 'suspendido'");
            }
        }

        // Buscar el contrato existente
        Optional<Contrato> contratoExistenteOpt = contratoRepository.findById(id);
        if (contratoExistenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Contrato no encontrado con ID: " + id);
        }

        Contrato contratoExistente = contratoExistenteOpt.get();

        // Verificar conflictos con otros contratos activos (solo si se cambia el cuarto)
        if (!contratoExistente.getIdCuarto().equals(contratoActualizado.getIdCuarto()) &&
                contratoRepository.existsContratoActivoByCuarto(contratoActualizado.getIdCuarto())) {
            throw new IllegalArgumentException("El nuevo cuarto ya tiene un contrato activo");
        }

        // Verificar conflictos con otros contratos activos (solo si se cambia el inquilino)
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

        // Solo actualizar estado si se proporciona
        if (contratoActualizado.getEstadoContrato() != null) {
            contratoExistente.setEstadoContrato(contratoActualizado.getEstadoContrato());
        }

        // Solo actualizar monto si se proporciona
        if (contratoActualizado.getMontoRentaAcordada() != null) {
            contratoExistente.setMontoRentaAcordada(contratoActualizado.getMontoRentaAcordada());
        }

        // Guardar los cambios
        contratoRepository.save(contratoExistente);

        // Recargar con relaciones para evitar LazyInitializationException
        return contratoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error al recuperar el contrato actualizado"));
    }

    // Método para verificar si un contrato existe por ID
    public boolean existsById(Integer id) {
        return contratoRepository.existsById(id);
    }

    // Métodos específicos para Contrato
    public List<Contrato> getContratosByCuarto(Integer idCuarto) {
        return contratoRepository.findByIdCuarto(idCuarto);
    }

    public List<Contrato> getContratosByInquilino(Integer idInquilino) {
        return contratoRepository.findByIdInquilino(idInquilino);
    }

    // Método para obtener contratos activos
    public List<Contrato> getContratosActivos() {
        return contratoRepository.findContratosActivos();
    }

    // Método para obtener contratos por estado
    public List<Contrato> getContratosPorEstado(String estado) {
        return contratoRepository.findByEstadoContrato(estado);
    }

    // Método para obtener contratos próximos a expirar
    public List<Contrato> getContratosProximosAExpirar(int dias) {
        return contratoRepository.findContratosProximosAExpirar(dias);
    }

    // Método para obtener contratos vencidos
    public List<Contrato> getContratosVencidos() {
        return contratoRepository.findContratosVencidos();
    }

    // Método para obtener contratos con pagos próximos
    public List<Contrato> getContratosConPagosProximos(int dias) {
        return contratoRepository.findContratosConPagosProximos(dias);
    }

    // Método para obtener contratos por rango de fechas
    public List<Contrato> getContratosPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return contratoRepository.findByFechaInicioBetween(fechaInicio, fechaFin);
    }

    // Método para finalizar un contrato
    public Contrato finalizarContrato(Integer idContrato) {
        Optional<Contrato> contratoOpt = contratoRepository.findById(idContrato);
        if (contratoOpt.isEmpty()) {
            throw new IllegalArgumentException("Contrato no encontrado con ID: " + idContrato);
        }

        Contrato contrato = contratoOpt.get();
        contrato.setEstadoContrato("finalizado");
        contrato.setFechaFinalizacion(LocalDate.now());

        return contratoRepository.save(contrato);
    }

    // Método para cancelar un contrato
    public Contrato cancelarContrato(Integer idContrato) {
        Optional<Contrato> contratoOpt = contratoRepository.findById(idContrato);
        if (contratoOpt.isEmpty()) {
            throw new IllegalArgumentException("Contrato no encontrado con ID: " + idContrato);
        }

        Contrato contrato = contratoOpt.get();
        contrato.setEstadoContrato("cancelado");
        contrato.setFechaFinalizacion(LocalDate.now());

        return contratoRepository.save(contrato);
    }

    // Método para verificar si un contrato está activo
    public boolean estaContratoActivo(Integer idContrato) {
        Optional<Contrato> contratoOpt = contratoRepository.findById(idContrato);
        if (contratoOpt.isEmpty()) {
            return false;
        }

        Contrato contrato = contratoOpt.get();
        return contrato.estaActivo() && contrato.estaVigente();
    }
}