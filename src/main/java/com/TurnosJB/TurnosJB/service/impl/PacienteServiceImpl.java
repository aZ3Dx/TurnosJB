package com.TurnosJB.TurnosJB.service.impl;

import com.TurnosJB.TurnosJB.entity.Odontologo;
import com.TurnosJB.TurnosJB.entity.Paciente;
import com.TurnosJB.TurnosJB.exception.ResourceNotFoundException;
import com.TurnosJB.TurnosJB.repository.IPacienteRepository;
import com.TurnosJB.TurnosJB.service.IPacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteServiceImpl implements IPacienteService {
    @Autowired
    private IPacienteRepository iPacienteRepository;
    @Override
    public Paciente guardar(Paciente paciente) {
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
}
