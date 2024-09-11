package com.TurnosJB.TurnosJB.service.impl;

import com.TurnosJB.TurnosJB.entity.Turno;
import com.TurnosJB.TurnosJB.exception.BadRequestException;
import com.TurnosJB.TurnosJB.exception.ConflictException;
import com.TurnosJB.TurnosJB.exception.ResourceNotFoundException;
import com.TurnosJB.TurnosJB.repository.IOdontologoRepository;
import com.TurnosJB.TurnosJB.repository.IPacienteRepository;
import com.TurnosJB.TurnosJB.repository.ITurnoRepository;
import com.TurnosJB.TurnosJB.service.ITurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Service
public class TurnoServiceImpl implements ITurnoService {

    private final ITurnoRepository iTurnoRepository;
    private final IPacienteRepository iPacienteRepository;
    private final IOdontologoRepository iOdontologoRepository;

    private static final Logger LOGGER = LogManager.getLogger(TurnoServiceImpl.class);

    @Autowired
    public TurnoServiceImpl(ITurnoRepository iTurnoRepository, IPacienteRepository iPacienteRepository, IOdontologoRepository iOdontologoRepository) {
        this.iTurnoRepository = iTurnoRepository;
        this.iPacienteRepository = iPacienteRepository;
        this.iOdontologoRepository = iOdontologoRepository;
    }

    @Override
    public Turno guardar(Turno turno) throws BadRequestException, ConflictException {
        LOGGER.info("Comenzamos a persistir un turno");

        if (turno.getPaciente() == null || turno.getOdontologo() == null) {
            throw new BadRequestException("Los datos del turno no pueden ser nulos.");
        }

        if ((turno.getPaciente().getId() == null) || !iPacienteRepository.existsById(turno.getPaciente().getId())) {
            throw new BadRequestException("No se puede registrar el turno si el paciente con ID " + turno.getPaciente().getId() + " no existe.");
        }

        if ((turno.getOdontologo().getId() == null) || !iOdontologoRepository.existsById(turno.getOdontologo().getId())) {
            throw new BadRequestException("No se puede registrar el turno si el odontólogo con ID " + turno.getOdontologo().getId() + " no existe.");
        }

        // Validamos los campos de Turno
        if (turno.getFecha() == null || turno.getHora() == null) {
            throw new BadRequestException("Los campos de Turno no pueden ser nulos.");
        }

        // Validamos que la fecha y hora sea posterior a la fecha actual
        if (turno.getFecha().isBefore(LocalDate.now())) {
            throw new BadRequestException("La fecha del turno no puede ser anterior a la fecha actual.");
        }
        if (turno.getFecha().isEqual(LocalDate.now()) && turno.getHora().isBefore(LocalTime.now())) {
            throw new BadRequestException("La hora del turno no puede ser anterior a la hora actual.");
        }

        // Validamos que el paciente no tenga un turno en esa fecha y hora
        List<Turno> turnosDelPaciente = iTurnoRepository.findByPacienteId(turno.getPaciente().getId());
        for (Turno t : turnosDelPaciente) {
            if (t.getFecha().isEqual(turno.getFecha()) && t.getHora().equals(turno.getHora())) {
                throw new ConflictException("El paciente ya tiene un turno en esa fecha y hora.");
            }
        }
        // Validamos que el odontólogo no tenga un turno en esa fecha y hora
        List<Turno> turnosDelOdontologo = iTurnoRepository.findByOdontologoId(turno.getOdontologo().getId());
        for (Turno t : turnosDelOdontologo) {
            if (t.getFecha().isEqual(turno.getFecha()) && t.getHora().equals(turno.getHora())) {
                throw new ConflictException("El odontólogo ya tiene un turno en esa fecha y hora.");
            }
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
    public Turno actualizar(Turno turno) throws BadRequestException, ConflictException {
        Turno turnoAnterior = iTurnoRepository.findById(turno.getId()).orElse(null);
        if (turnoAnterior == null) {
            throw new BadRequestException("No se puede actualizar el turno si no existe.");
        }
        if (turnoAnterior.getFecha().isEqual(turno.getFecha()) &&
                turnoAnterior.getHora().equals(turno.getHora()) &&
                turnoAnterior.getPaciente().getId().equals(turno.getPaciente().getId()) &&
                turnoAnterior.getOdontologo().getId().equals(turno.getOdontologo().getId())) {
            return turnoAnterior;
        }

        if (turno.getPaciente() == null || turno.getOdontologo() == null) {
            throw new BadRequestException("Los datos del turno no pueden ser nulos.");
        }

        if ((turno.getPaciente().getId() == null) || !iPacienteRepository.existsById(turno.getPaciente().getId())) {
            throw new BadRequestException("El paciente con ID " + turno.getPaciente().getId() + " no existe.");
        }

        if ((turno.getOdontologo().getId() == null) || !iOdontologoRepository.existsById(turno.getOdontologo().getId())) {
            throw new BadRequestException("El odontólogo con ID " + turno.getOdontologo().getId() + " no existe.");
        }

        // Validamos los campos de Turno
        if (turno.getFecha() == null || turno.getHora() == null) {
            throw new BadRequestException("Los campos de Turno no pueden ser nulos.");
        }

        // Validamos que la fecha y hora sea posterior a la fecha actual
        if (turno.getFecha().isBefore(LocalDate.now())) {
            throw new BadRequestException("La fecha del turno no puede ser anterior a la fecha actual.");
        }
        if (turno.getFecha().isEqual(LocalDate.now()) && turno.getHora().isBefore(LocalTime.now())) {
            throw new BadRequestException("La hora del turno no puede ser anterior a la hora actual.");
        }

        // Validamos que el paciente no tenga un turno en esa fecha y hora
        List<Turno> turnosDelPaciente = iTurnoRepository.findByPacienteId(turno.getPaciente().getId());
        for (Turno t : turnosDelPaciente) {
            if (t.getFecha().isEqual(turno.getFecha()) && t.getHora().equals(turno.getHora())) {
                throw new ConflictException("El paciente ya tiene un turno en esa fecha y hora.");
            }
        }

        // Validamos que el odontólogo no tenga un turno en esa fecha y hora
        List<Turno> turnosDelOdontologo = iTurnoRepository.findByOdontologoId(turno.getOdontologo().getId());
        for (Turno t : turnosDelOdontologo) {
            if (t.getFecha().isEqual(turno.getFecha()) && t.getHora().equals(turno.getHora())) {
                throw new ConflictException("El odontólogo ya tiene un turno en esa fecha y hora.");
            }
        }

        return iTurnoRepository.save(turno);
    }

    @Override
    public List<Turno> listar() {
        LOGGER.info("Lista de todos los turnos");
        return iTurnoRepository.findAll();
    }

    @Override
    public List<Turno> obtenerTurnosPorPaciente(Long id) {
        LOGGER.info("Se busca el turno por paciente " + id);
        return iTurnoRepository.findByPacienteId(id);
    }

    @Override
    public List<Turno> obtenerTurnosPorOdontologo(Long id) {
        LOGGER.info("Se busca el turno por odontologo "+ id);
        return iTurnoRepository.findByOdontologoId(id);
    }
}
