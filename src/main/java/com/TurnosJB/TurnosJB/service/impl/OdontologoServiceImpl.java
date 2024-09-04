package com.TurnosJB.TurnosJB.service.impl;

import com.TurnosJB.TurnosJB.entity.Odontologo;
import com.TurnosJB.TurnosJB.repository.IOdontologoRepository;
import com.TurnosJB.TurnosJB.service.IOdontologoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OdontologoServiceImpl implements IOdontologoService {

    @Autowired
    private IOdontologoRepository odontologoRepository;

    @Override
    public Odontologo guardar(Odontologo odontologo) {
        return odontologoRepository.save(odontologo);
    }

    @Override
    public Odontologo buscarPorId(Long id) {
        Optional<Odontologo> odontologo = odontologoRepository.findById(id);
        return odontologo.orElse(null);
    }

    @Override
    public void eliminar(Long id) {
        odontologoRepository.deleteById(id);
    }

    @Override
    public Odontologo actualizar(Odontologo odontologo) {
        return odontologoRepository.save(odontologo);
    }

    @Override
    public List<Odontologo> listar() {
        return odontologoRepository.findAll();
    }
}
