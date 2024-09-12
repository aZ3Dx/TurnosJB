package com.TurnosJB.TurnosJB.service.impl;

import com.TurnosJB.TurnosJB.entity.Paciente;
import com.TurnosJB.TurnosJB.exception.BadRequestException;
import com.TurnosJB.TurnosJB.exception.ConflictException;
import com.TurnosJB.TurnosJB.exception.DataIntegrityViolationException;
import com.TurnosJB.TurnosJB.exception.ResourceNotFoundException;
import com.TurnosJB.TurnosJB.repository.IPacienteRepository;
import com.TurnosJB.TurnosJB.repository.ITurnoRepository;
import com.TurnosJB.TurnosJB.service.IPacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Service
public class PacienteServiceImpl implements IPacienteService {

    private final IPacienteRepository iPacienteRepository;
    private final ITurnoRepository iTurnoRepository;

    private static final Logger LOGGER = LogManager.getLogger(PacienteServiceImpl.class);


    @Autowired
    public PacienteServiceImpl(IPacienteRepository iPacienteRepository, ITurnoRepository iTurnoRepository) {
        this.iPacienteRepository = iPacienteRepository;
        this.iTurnoRepository = iTurnoRepository;
    }

    @Override
    public Paciente guardar(Paciente paciente) throws ConflictException, BadRequestException {
        LOGGER.info("Comenzamos a persistir un paciente");

        paciente.setFechaAlta(LocalDate.now());
        // Revisamos que no hayan pacientes con el mismo dni
        if (iPacienteRepository.findByDni(paciente.getDni()) != null) {
            LOGGER.error("Ya existe un paciente con el dni " + paciente.getDni());
            throw new ConflictException("Ya existe un paciente con el dni " + paciente.getDni());
        }
        // Validamos que el DNI sea correcto
        if (!paciente.getDni().matches("[0-9]+")) {
            LOGGER.error("El DNI debe ser numérico");
            throw new BadRequestException("El DNI debe ser numérico");
        }
        // Revisamos que los campos de Paciente no estén vacíos
        if (Optional.ofNullable(paciente.getNombre()).orElse("").isBlank() ||
                Optional.ofNullable(paciente.getApellido()).orElse("").isBlank() ||
                Optional.of(paciente.getDni()).orElse("").isBlank()) {
            LOGGER.error("Los campos de Paciente no pueden estar vacíos");
            throw new BadRequestException("Los campos de Paciente no pueden estar vacíos");
        }
        // Revisamos los campos de su Domicilio
        if (Optional.ofNullable(paciente.getDomicilio().getCalle()).orElse("").isBlank() ||
                Optional.ofNullable(paciente.getDomicilio().getLocalidad()).orElse("").isBlank() ||
                Optional.ofNullable(paciente.getDomicilio().getProvincia()).orElse("").isBlank()) {
            LOGGER.error("Los campos de domicilio no pueden estar vacíos");
            throw new BadRequestException("Las campos de domicilio no pueden estar vacíos");
        }
        // Revisamos el número de su Domicilio
        if (Optional.ofNullable(paciente.getDomicilio().getNumero()).orElse(0).equals(0)) {
            LOGGER.error("El campo de domicilio debe ser numérico");
            throw new BadRequestException("El campo de domicilio debe ser numérico");
        }
        LOGGER.info("Paciente registrado con el dni " + paciente.getDni());
        return iPacienteRepository.save(paciente);
    }

    @Override
    public Paciente buscarPorId(Long id) throws ResourceNotFoundException {
        Optional<Paciente> paciente = iPacienteRepository.findById(id);
        if (paciente.isPresent()) {
            LOGGER.info(paciente.get());
            return paciente.get();
        } else {
            LOGGER.error("No se encontró el paciente con id " + id);
            throw new ResourceNotFoundException("No se encontró el paciente con id " + id);
        }
    }

    @Override
    public void eliminar(Long id) throws DataIntegrityViolationException {
        // Revisamos que el paciente no tenga turnos
        if (!iTurnoRepository.findByPacienteId(id).isEmpty()) {
            LOGGER.error("No se puede eliminar el paciente porque tiene turnos asociados.");
            throw new DataIntegrityViolationException("No se puede eliminar el paciente porque tiene turnos asociados.");
        }
        iPacienteRepository.deleteById(id);
    }

    @Override
    public Paciente actualizar(Paciente paciente) throws ConflictException, BadRequestException, ResourceNotFoundException {
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
            LOGGER.error("Ya existe un paciente con el dni " + paciente.getDni());
            throw new ConflictException("Ya existe un paciente con el dni " + paciente.getDni());
        }
        // Validamos que el DNI sea correcto
        if (!paciente.getDni().matches("[0-9]+")) {
            LOGGER.error("El DNI debe ser numérico");
            throw new BadRequestException("El DNI debe ser numérico");
        }

        // Revisamos que los campos de Paciente no estén vacíos
        if (Optional.ofNullable(paciente.getNombre()).orElse("").isBlank() ||
                Optional.ofNullable(paciente.getApellido()).orElse("").isBlank() ||
                Optional.of(paciente.getDni()).orElse("").isBlank()) {
            LOGGER.error("Los campos de Paciente no pueden estar vacíos");
            throw new BadRequestException("Los campos de Paciente no pueden estar vacíos");
        }
        // Revisamos los campos de su Domicilio
        if (Optional.ofNullable(paciente.getDomicilio().getCalle()).orElse("").isBlank() ||
                Optional.ofNullable(paciente.getDomicilio().getLocalidad()).orElse("").isBlank() ||
                Optional.ofNullable(paciente.getDomicilio().getProvincia()).orElse("").isBlank()) {
            LOGGER.error("Los campos de domicilio no pueden estar vacíos");
            throw new BadRequestException("Las campos de domicilio no pueden estar vacíos");
        }
        // Revisamos el número de su Domicilio
        if (Optional.ofNullable(paciente.getDomicilio().getNumero()).orElse(0).equals(0)) {
            LOGGER.error("El campo de domicilio debe ser numérico");
            throw new BadRequestException("El campo de domicilio debe ser numérico");
        }
        LOGGER.info("Paciente actualizado con el dni " + paciente.getDni());
        return iPacienteRepository.save(paciente);
    }

    @Override
    public List<Paciente> listar() {
        LOGGER.info("Lista de todos los pacientes");
        return iPacienteRepository.findAll();
    }

    @Override
    public Paciente buscarPorDni(String dni) {
        LOGGER.info("Se busca el paciente con dni " + dni);
        return iPacienteRepository.findByDni(dni);
    }
}
