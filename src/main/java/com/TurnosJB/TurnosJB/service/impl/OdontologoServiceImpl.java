package com.TurnosJB.TurnosJB.service.impl;

import com.TurnosJB.TurnosJB.entity.Odontologo;
import com.TurnosJB.TurnosJB.exception.BadRequestException;
import com.TurnosJB.TurnosJB.exception.ConflictException;
import com.TurnosJB.TurnosJB.exception.DataIntegrityViolationException;
import com.TurnosJB.TurnosJB.exception.ResourceNotFoundException;
import com.TurnosJB.TurnosJB.repository.IOdontologoRepository;
import com.TurnosJB.TurnosJB.repository.ITurnoRepository;
import com.TurnosJB.TurnosJB.service.IOdontologoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
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
        log.info("Comenzamos a persistir un odontólogo...");

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
        log.info("Este es el odontologo que se guardó " + odontologo.getNombre());
        return iOdontologoRepository.save(odontologo);
    }

    @Override
    public Odontologo buscarPorId(Long id) throws ResourceNotFoundException {
        log.info("Comenzamos a buscar por id un odontólogo...");

        Optional<Odontologo> odontologo = iOdontologoRepository.findById(id);
        if (odontologo.isPresent()) {
            log.info("El odontólogo encontrado es " + odontologo.get());
            return odontologo.get();
        } else {
            throw new ResourceNotFoundException("No se encontró el odontólogo con id " + id);
        }
    }

    @Override
    public void eliminar(Long id) throws DataIntegrityViolationException {
        log.info("Comenzamos a eliminar un odontólogo...");

        // Revisamos que el odontologo no tenga turnos asociados
        if (!iTurnoRepository.findByOdontologoId(id).isEmpty()) {
            throw new DataIntegrityViolationException("El odontologo no puede ser eliminado porque tiene turnos asociados");
        }
        log.info("Se eliminó el odontólogo con id: " + id);
        iOdontologoRepository.deleteById(id);
    }

    @Override
    public Odontologo actualizar(Odontologo odontologo) throws ResourceNotFoundException, ConflictException, BadRequestException {
        log.info("Comenzamos a actualizar un odontólogo...");

        // Revisamos que exista el id del odontologo
        if (!iOdontologoRepository.existsById(odontologo.getId())) {
            throw new ResourceNotFoundException("No existe un odontologo con el id " + odontologo.getId());
        }
        Optional<Odontologo> datosGuardados = iOdontologoRepository.findById(odontologo.getId());
        if (datosGuardados.isPresent()) {
           // Por cada valor que sea nulo o vacío, lo reemplazamos por el valor que ya tiene el odontologo
            if (Optional.ofNullable(odontologo.getNombre()).orElse("").isBlank()) {
                odontologo.setNombre(datosGuardados.get().getNombre());
            }
            if (Optional.ofNullable(odontologo.getApellido()).orElse("").isBlank()) {
                odontologo.setApellido(datosGuardados.get().getApellido());
            }
            if (Optional.ofNullable(odontologo.getMatricula()).orElse("").isBlank()) {
                odontologo.setMatricula(datosGuardados.get().getMatricula());
            }
        }
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
        log.info("El odontólogo que se guardó " + odontologo.getNombre());
        return iOdontologoRepository.save(odontologo);
    }

    @Override
    public List<Odontologo> listar() {
        log.info("Lista de todos los odontólogos");
        return iOdontologoRepository.findAll();
    }

    @Override
    public Odontologo buscarPorMatricula(String matricula) {
        log.info("Se encuentra el odontólogo con matrícula: " + matricula);
        return iOdontologoRepository.findByMatricula(matricula);
    }
}
