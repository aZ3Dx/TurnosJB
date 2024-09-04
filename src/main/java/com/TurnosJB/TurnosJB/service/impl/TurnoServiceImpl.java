package com.TurnosJB.TurnosJB.service.impl;

import com.TurnosJB.TurnosJB.entity.Turno;
import com.TurnosJB.TurnosJB.repository.ITurnoRepository;
import com.TurnosJB.TurnosJB.service.ITurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurnoServiceImpl implements ITurnoService {

    @Autowired
    private ITurnoRepository iTurnoRepository;
    @Override
    public Turno guardar(Turno turno) {
        return iTurnoRepository.save(turno);
    }

    @Override
    public Turno buscarPorId(Long id) {
        return iTurnoRepository.findById(id).orElse(null);
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
