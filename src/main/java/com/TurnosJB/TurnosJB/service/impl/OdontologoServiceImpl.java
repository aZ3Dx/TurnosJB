package com.TurnosJB.TurnosJB.service.impl;

import com.TurnosJB.TurnosJB.entity.Odontologo;
import com.TurnosJB.TurnosJB.repository.IOdontologoRepository;
import com.TurnosJB.TurnosJB.service.IOdontologoService;

import java.util.List;
import java.util.Optional;

public class OdontologoServiceImpl implements IOdontologoService {

    private IOdontologoRepository odontologoRepository;

    @Override
    public Odontologo guardar(Odontologo odontologo) {
        Odontologo odontologoGuardado = odontologoRepository.save(odontologo);
        return odontologoGuardado;
    }

    @Override
    public Odontologo buscarPorId(Long id) {
        Optional<Odontologo> odontologo = odontologoRepository.findById(id);
        return odontologo.orElse(null);
    }

    @Override
    public void eliminar(Long id) {

    }

    @Override
    public void actualizar(Odontologo odontologo) {

    }

    @Override
    public List<Odontologo> listar() {
        return List.of();
    }
}
