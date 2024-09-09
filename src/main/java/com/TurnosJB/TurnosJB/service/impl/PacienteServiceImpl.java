package com.TurnosJB.TurnosJB.service.impl;

import com.TurnosJB.TurnosJB.entity.Odontologo;
import com.TurnosJB.TurnosJB.entity.Paciente;
import com.TurnosJB.TurnosJB.exception.BadRequestException;
import com.TurnosJB.TurnosJB.exception.ConflictException;
import com.TurnosJB.TurnosJB.exception.ResourceNotFoundException;
import com.TurnosJB.TurnosJB.repository.IPacienteRepository;
import com.TurnosJB.TurnosJB.service.IPacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PacienteServiceImpl implements IPacienteService {
    @Autowired
    private IPacienteRepository iPacienteRepository;

    @Override
    public Paciente guardar(Paciente paciente) throws ConflictException, BadRequestException {
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
                Optional.ofNullable(paciente.getDomicilio().getNumero()).orElse(0).equals(0) ||
                Optional.ofNullable(paciente.getDomicilio().getLocalidad()).orElse("").isBlank() ||
                Optional.ofNullable(paciente.getDomicilio().getProvincia()).orElse("").isBlank()) {
            throw new BadRequestException("Las campos de domicilio no pueden estar vacíos");
        }
        return iPacienteRepository.save(paciente);
    }

    @Override
    public Paciente buscarPorId(Long id) throws ResourceNotFoundException {
        Optional<Paciente> paciente = iPacienteRepository.findById(id);
        if (paciente.isPresent()) {
            return paciente.get();
        } else {
            throw new ResourceNotFoundException("No se encontró el paciente con id " + id);
        }
    }

    @Override
    public void eliminar(Long id) {
        iPacienteRepository.deleteById(id);
    }

    @Override
    public Paciente actualizar(Paciente paciente) {
        return iPacienteRepository.save(paciente);
    }

    @Override
    public List<Paciente> listar() {
        return iPacienteRepository.findAll();
    }

    @Override
    public Paciente buscarPorDni(String dni) {
        return iPacienteRepository.findByDni(dni);
    }
}
