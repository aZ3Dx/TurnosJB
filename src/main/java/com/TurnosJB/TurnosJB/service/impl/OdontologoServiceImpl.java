package com.TurnosJB.TurnosJB.service.impl;

import com.TurnosJB.TurnosJB.entity.Odontologo;
import com.TurnosJB.TurnosJB.exception.BadRequestException;
import com.TurnosJB.TurnosJB.exception.ConflictException;
import com.TurnosJB.TurnosJB.exception.ResourceNotFoundException;
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
    public Odontologo guardar(Odontologo odontologo) throws ConflictException, BadRequestException {
        // Revisamos que no hayan odontologos con el mismo matricula
        if (odontologoRepository.findByMatricula(odontologo.getMatricula()) != null) {
            throw new ConflictException("Ya existe un odontologo con la matricula " + odontologo.getMatricula());
        }
        // Revisamos que los campos de Odontologo no estén vacíos
        if (Optional.ofNullable(odontologo.getNombre()).orElse("").isBlank() ||
                Optional.ofNullable(odontologo.getApellido()).orElse("").isBlank() ||
                Optional.ofNullable(odontologo.getMatricula()).orElse("").isBlank()) {
            throw new BadRequestException("Los campos de odontologo no pueden estar vacíos");
        }
        return odontologoRepository.save(odontologo);
    }

    @Override
    public Odontologo buscarPorId(Long id) throws ResourceNotFoundException {
        Optional<Odontologo> odontologo = odontologoRepository.findById(id);
        if (odontologo.isPresent()) {
            return odontologo.get();
        } else {
            throw new ResourceNotFoundException("No se encontró el odontólogo con id " + id);
        }
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

    @Override
    public Odontologo buscarPorMatricula(String matricula) {
        return odontologoRepository.findByMatricula(matricula);
    }
}
