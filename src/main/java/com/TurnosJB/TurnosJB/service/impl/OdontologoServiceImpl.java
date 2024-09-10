package com.TurnosJB.TurnosJB.service.impl;

import com.TurnosJB.TurnosJB.entity.Odontologo;
import com.TurnosJB.TurnosJB.exception.BadRequestException;
import com.TurnosJB.TurnosJB.exception.ConflictException;
import com.TurnosJB.TurnosJB.exception.DataIntegrityViolationException;
import com.TurnosJB.TurnosJB.exception.ResourceNotFoundException;
import com.TurnosJB.TurnosJB.repository.IOdontologoRepository;
import com.TurnosJB.TurnosJB.repository.ITurnoRepository;
import com.TurnosJB.TurnosJB.service.IOdontologoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OdontologoServiceImpl implements IOdontologoService {

    private final IOdontologoRepository iOdontologoRepository;
    private final ITurnoRepository iTurnoRepository;

    @Autowired
    public OdontologoServiceImpl(IOdontologoRepository iOdontologoRepository, ITurnoRepository iTurnoRepository) {
        this.iOdontologoRepository = iOdontologoRepository;
        this.iTurnoRepository = iTurnoRepository;
    }

    @Override
    public Odontologo guardar(Odontologo odontologo) throws ConflictException, BadRequestException {
        // Revisamos que no hayan odontologos con el mismo matricula
        if (iOdontologoRepository.findByMatricula(odontologo.getMatricula()) != null) {
            throw new ConflictException("Ya existe un odontologo con la matricula " + odontologo.getMatricula());
        }
        // Revisamos que los campos de Odontologo no estén vacíos
        if (Optional.ofNullable(odontologo.getNombre()).orElse("").isBlank() ||
                Optional.ofNullable(odontologo.getApellido()).orElse("").isBlank() ||
                Optional.ofNullable(odontologo.getMatricula()).orElse("").isBlank()) {
            throw new BadRequestException("Los campos de odontologo no pueden estar vacíos");
        }
        return iOdontologoRepository.save(odontologo);
    }

    @Override
    public Odontologo buscarPorId(Long id) throws ResourceNotFoundException {
        Optional<Odontologo> odontologo = iOdontologoRepository.findById(id);
        if (odontologo.isPresent()) {
            return odontologo.get();
        } else {
            throw new ResourceNotFoundException("No se encontró el odontólogo con id " + id);
        }
    }

    @Override
    public void eliminar(Long id) throws DataIntegrityViolationException {
        // Revisamos que el odontologo no tenga turnos asociados
        if (!iTurnoRepository.findByOdontologoId(id).isEmpty()) {
            throw new DataIntegrityViolationException("El odontologo no puede ser eliminado porque tiene turnos asociados");
        }
        iOdontologoRepository.deleteById(id);
    }

    @Override
    public Odontologo actualizar(Odontologo odontologo) {
        // Revisamos que si la nueva matrícula puede cambiar
        Odontologo odontologoConLaMismaMatricula = iOdontologoRepository.findByMatricula(odontologo.getMatricula());
        if (odontologoConLaMismaMatricula != null && !odontologoConLaMismaMatricula.getId().equals(odontologo.getId())) {
            throw new ConflictException("Ya existe un odontologo con la matricula " + odontologo.getMatricula());
        }
        // Revisamos que los campos de Odontologo no estén vacíos
        if (Optional.ofNullable(odontologo.getNombre()).orElse("").isBlank() ||
                Optional.ofNullable(odontologo.getApellido()).orElse("").isBlank() ||
                Optional.ofNullable(odontologo.getMatricula()).orElse("").isBlank()) {
            throw new BadRequestException("Los campos de Odontologo no pueden estar vacíos");
        }
        return iOdontologoRepository.save(odontologo);
    }

    @Override
    public List<Odontologo> listar() {
        return iOdontologoRepository.findAll();
    }

    @Override
    public Odontologo buscarPorMatricula(String matricula) {
        return iOdontologoRepository.findByMatricula(matricula);
    }
}
