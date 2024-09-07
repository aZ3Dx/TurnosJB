package com.TurnosJB.TurnosJB.service.impl;

import com.TurnosJB.TurnosJB.entity.Paciente;
import com.TurnosJB.TurnosJB.entity.Turno;
import com.TurnosJB.TurnosJB.exception.BadRequestException;
import com.TurnosJB.TurnosJB.exception.ResourceNotFoundException;
import com.TurnosJB.TurnosJB.repository.IOdontologoRepository;
import com.TurnosJB.TurnosJB.repository.IPacienteRepository;
import com.TurnosJB.TurnosJB.repository.ITurnoRepository;
import com.TurnosJB.TurnosJB.service.ITurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TurnoServiceImpl implements ITurnoService {

    @Autowired
    private ITurnoRepository iTurnoRepository;


    @Autowired
    private IPacienteRepository iPacienteRepository;

    @Autowired
    private IOdontologoRepository iOdontologoRepository;

    @Override
    public Turno guardar(Turno turno) throws BadRequestException {
        if (turno.getPaciente() == null || turno.getOdontologo() == null) {
            throw new BadRequestException("Los datos del turno no pueden ser nulos.");
        }

        if ((turno.getPaciente().getId() == null) || !iPacienteRepository.existsById(turno.getPaciente().getId())) {
            throw new BadRequestException("El paciente con ID " + turno.getPaciente().getId() + " no existe.");
        }

        if ((turno.getOdontologo().getId() == null) || !iOdontologoRepository.existsById(turno.getOdontologo().getId())) {
            throw new BadRequestException("El odontólogo con ID " + turno.getOdontologo().getId() + " no existe.");
        }

        return iTurnoRepository.save(turno);
    }

    @Override
    public Turno buscarPorId(Long id) throws ResourceNotFoundException {
        Optional<Turno> turno = iTurnoRepository.findById(id);
        if (turno.isPresent()) {
            return turno.get();
        } else {
            throw new ResourceNotFoundException("No se encontró el turno con id " + id);
        }
    }

    @Override
    public void eliminar(Long id) {
        iTurnoRepository.deleteById(id);
    }

    @Override
    public Turno actualizar(Turno turno) {
        return iTurnoRepository.save(turno);
    }

    @Override
    public List<Turno> listar() {
        return iTurnoRepository.findAll();
    }

    @Override
    public List<Turno> obtenerTurnosPorPaciente(Long id) {
        return iTurnoRepository.findByPacienteId(id);
    }
}
