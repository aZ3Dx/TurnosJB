package com.TurnosJB.TurnosJB.service.impl;

import com.TurnosJB.TurnosJB.entity.Paciente;
import com.TurnosJB.TurnosJB.exception.BadRequestException;
import com.TurnosJB.TurnosJB.exception.ConflictException;
import com.TurnosJB.TurnosJB.exception.DataIntegrityViolationException;
import com.TurnosJB.TurnosJB.exception.ResourceNotFoundException;
import com.TurnosJB.TurnosJB.repository.IPacienteRepository;
import com.TurnosJB.TurnosJB.repository.ITurnoRepository;
import com.TurnosJB.TurnosJB.service.IPacienteService;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class PacienteServiceImpl implements IPacienteService {

    private final IPacienteRepository iPacienteRepository;
    private final ITurnoRepository iTurnoRepository;

    @Autowired
    public PacienteServiceImpl(IPacienteRepository iPacienteRepository, ITurnoRepository iTurnoRepository) {
        this.iPacienteRepository = iPacienteRepository;
        this.iTurnoRepository = iTurnoRepository;
    }

    @Override
    public Paciente guardar(Paciente paciente) throws ConflictException, BadRequestException {
        log.info("Comenzamos a persistir un paciente");

        paciente.setFechaAlta(LocalDate.now());
        // Revisamos que no hayan pacientes con el mismo dni
        if (iPacienteRepository.findByDni(paciente.getDni()) != null) {
            throw new ConflictException("Ya existe un paciente con el dni " + paciente.getDni());
        }
        // Validamos que el DNI sea correcto
        if (!paciente.getDni().matches("[0-9]+")) {
            throw new BadRequestException("El DNI debe ser numérico");
        }
        // Revisamos que los campos de Paciente no estén vacíos
        if (Optional.ofNullable(paciente.getNombre()).orElse("").isBlank() ||
                Optional.ofNullable(paciente.getApellido()).orElse("").isBlank() ||
                Optional.of(paciente.getDni()).orElse("").isBlank()) {
            throw new BadRequestException("Los campos de Paciente no pueden estar vacíos");
        }
        // Revisamos los campos de su Domicilio
        if (Optional.ofNullable(paciente.getDomicilio().getCalle()).orElse("").isBlank() ||
                Optional.ofNullable(paciente.getDomicilio().getLocalidad()).orElse("").isBlank() ||
                Optional.ofNullable(paciente.getDomicilio().getProvincia()).orElse("").isBlank()) {
            throw new BadRequestException("Las campos de domicilio no pueden estar vacíos");
        }
        // Revisamos el número de su Domicilio
        if (Optional.ofNullable(paciente.getDomicilio().getNumero()).orElse(0).equals(0)) {
            throw new BadRequestException("El campo de domicilio debe ser numérico");
        }
        log.info("Paciente registrado con el dni " + paciente.getDni());
        return iPacienteRepository.save(paciente);
    }

    @Override
    public Paciente buscarPorId(Long id) throws ResourceNotFoundException {
        log.info("Empezamos a buscar un paciente por id...");
        Optional<Paciente> paciente = iPacienteRepository.findById(id);
        if (paciente.isPresent()) {
            log.info(String.valueOf(paciente.get()));
            return paciente.get();
        } else {
            throw new ResourceNotFoundException("No se encontró el paciente con id " + id);
        }
    }

    @Override
    public void eliminar(Long id) throws DataIntegrityViolationException {
        log.info("Empezamos a eliminar un paciente...");
        // Revisamos que el paciente no tenga turnos
        if (!iTurnoRepository.findByPacienteId(id).isEmpty()) {
            throw new DataIntegrityViolationException("No se puede eliminar el paciente porque tiene turnos asociados.");
        }
        log.info("El paciente eliminado es identificado con el id " + id);
        iPacienteRepository.deleteById(id);
    }

    @Override
    public Paciente actualizar(Paciente paciente) throws ConflictException, BadRequestException, ResourceNotFoundException {
        log.info("Empezamos a actualizar un paciente.");
        // Revisamos que exista el id del paciente
        if (!iPacienteRepository.existsById(paciente.getId())) {
            throw new ResourceNotFoundException("No se encontró el paciente con id " + paciente.getId());
        }
        Optional<Paciente> datosGuardados = iPacienteRepository.findById(paciente.getId());
        if (datosGuardados.isPresent()) {
            // Por cada valor nulo o vacío, reemplazamos por el valor guardado
            if (Optional.ofNullable(paciente.getNombre()).orElse("").isBlank()) {
                paciente.setNombre(datosGuardados.get().getNombre());
            }
            if (Optional.ofNullable(paciente.getApellido()).orElse("").isBlank()) {
                paciente.setApellido(datosGuardados.get().getApellido());
            }
            if (Optional.ofNullable(paciente.getDni()).orElse("").isBlank()) {
                paciente.setDni(datosGuardados.get().getDni());
            }
            if (paciente.getFechaAlta() == null) {
                paciente.setFechaAlta(datosGuardados.get().getFechaAlta());
            }
            if (paciente.getDomicilio() == null) {
                paciente.setDomicilio(datosGuardados.get().getDomicilio());
            } else {
                if (Optional.ofNullable(paciente.getDomicilio().getCalle()).orElse("").isBlank()) {
                    paciente.getDomicilio().setCalle(datosGuardados.get().getDomicilio().getCalle());
                }
                if (Optional.ofNullable(paciente.getDomicilio().getNumero()).orElse(0).equals(0)) {
                    paciente.getDomicilio().setNumero(datosGuardados.get().getDomicilio().getNumero());
                }
                if (Optional.ofNullable(paciente.getDomicilio().getLocalidad()).orElse("").isBlank()) {
                    paciente.getDomicilio().setLocalidad(datosGuardados.get().getDomicilio().getLocalidad());
                }
                if (Optional.ofNullable(paciente.getDomicilio().getProvincia()).orElse("").isBlank()) {
                    paciente.getDomicilio().setProvincia(datosGuardados.get().getDomicilio().getProvincia());
                }
            }
        }
        // Revisamos que si el nuevo DNI puede cambiar
        Paciente pacienteConElMismoDni = iPacienteRepository.findByDni(paciente.getDni());
        if (pacienteConElMismoDni != null && !pacienteConElMismoDni.getId().equals(paciente.getId())) {
            throw new ConflictException("Ya existe un paciente con el dni " + paciente.getDni());
        }
        // Validamos que el DNI sea correcto
        if (!paciente.getDni().matches("[0-9]+")) {
            throw new BadRequestException("El DNI debe ser numérico");
        }

        // Revisamos que los campos de Paciente no estén vacíos
        if (Optional.ofNullable(paciente.getNombre()).orElse("").isBlank() ||
                Optional.ofNullable(paciente.getApellido()).orElse("").isBlank() ||
                Optional.of(paciente.getDni()).orElse("").isBlank()) {
            throw new BadRequestException("Los campos de Paciente no pueden estar vacíos");
        }
        // Revisamos los campos de su Domicilio
        if (Optional.ofNullable(paciente.getDomicilio().getCalle()).orElse("").isBlank() ||
                Optional.ofNullable(paciente.getDomicilio().getLocalidad()).orElse("").isBlank() ||
                Optional.ofNullable(paciente.getDomicilio().getProvincia()).orElse("").isBlank()) {
            throw new BadRequestException("Las campos de domicilio no pueden estar vacíos");
        }
        // Revisamos el número de su Domicilio
        if (Optional.ofNullable(paciente.getDomicilio().getNumero()).orElse(0).equals(0)) {
            throw new BadRequestException("El campo de domicilio debe ser numérico");
        }
        log.info("Paciente actualizado con el dni " + paciente.getDni());
        return iPacienteRepository.save(paciente);
    }

    @Override
    public List<Paciente> listar() {
        log.info("Lista de todos los pacientes");
        return iPacienteRepository.findAll();
    }

    @Override
    public Paciente buscarPorDni(String dni) {
        log.info("Se busca el paciente con dni " + dni);
        return iPacienteRepository.findByDni(dni);
    }
}
