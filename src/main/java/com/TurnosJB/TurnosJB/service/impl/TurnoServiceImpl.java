package com.TurnosJB.TurnosJB.service.impl;

import com.TurnosJB.TurnosJB.entity.Paciente;
import com.TurnosJB.TurnosJB.entity.Turno;
import com.TurnosJB.TurnosJB.exception.BadRequestException;
import com.TurnosJB.TurnosJB.exception.ResourceNotFoundException;
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
    @Override
    public Turno guardar(Turno turno) throws BadRequestException {
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
}
