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
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.List;
import java.util.Optional;

@Service
public class OdontologoServiceImpl implements IOdontologoService {

    private final IOdontologoRepository iOdontologoRepository;
    private final ITurnoRepository iTurnoRepository;

    private static final Logger LOGGER = LogManager.getLogger(OdontologoServiceImpl.class);

    @Autowired
    public OdontologoServiceImpl(IOdontologoRepository iOdontologoRepository, ITurnoRepository iTurnoRepository) {
        this.iOdontologoRepository = iOdontologoRepository;
        this.iTurnoRepository = iTurnoRepository;
    }

    @Override
    public Odontologo guardar(Odontologo odontologo) throws ConflictException, BadRequestException {
        LOGGER.info("Comenzamos a persistir un odontólogo");

        LOGGER.info("Revisamos que no hayan odontólogos con la misma matrícula");
        // Revisamos que no hayan odontologos con el mismo matricula
        if (iOdontologoRepository.findByMatricula(odontologo.getMatricula()) != null) {
            throw new ConflictException("Ya existe un odontologo con la matricula " + odontologo.getMatricula());
        }
        LOGGER.info("Revisamos que los campos de odontólogo no estén vacíos");
        // Revisamos que los campos de Odontologo no estén vacíos
        if (Optional.ofNullable(odontologo.getNombre()).orElse("").isBlank() ||
                Optional.ofNullable(odontologo.getApellido()).orElse("").isBlank() ||
                Optional.ofNullable(odontologo.getMatricula()).orElse("").isBlank()) {
            throw new BadRequestException("Los campos de odontologo no pueden estar vacíos");
        }
        LOGGER.info("Este es el odontologo que se guardó " + odontologo.getNombre());
        return iOdontologoRepository.save(odontologo);
    }

    @Override
    public Odontologo buscarPorId(Long id) throws ResourceNotFoundException {
        Optional<Odontologo> odontologo = iOdontologoRepository.findById(id);
        if (odontologo.isPresent()) {
            LOGGER.info("El odontólogo encontrado es " + odontologo.get());
            return odontologo.get();
        } else {
            LOGGER.error("No se encontró el odontólogo con id " + id);
            throw new ResourceNotFoundException("No se encontró el odontólogo con id " + id);
        }
    }

    @Override
    public void eliminar(Long id) throws DataIntegrityViolationException {
        // Revisamos que el odontologo no tenga turnos asociados
        if (!iTurnoRepository.findByOdontologoId(id).isEmpty()) {
            LOGGER.error("El odontólogo no puede ser eliminado porque tiene turnos asociados.");
            throw new DataIntegrityViolationException("El odontologo no puede ser eliminado porque tiene turnos asociados");
        }
        iOdontologoRepository.deleteById(id);
    }

    @Override
    public Odontologo actualizar(Odontologo odontologo) throws ResourceNotFoundException, ConflictException, BadRequestException {
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
            LOGGER.error("Los campos de odontólogos no pueden estar vacíos.");
            throw new BadRequestException("Los campos de Odontologo no pueden estar vacíos");
        }
        return iOdontologoRepository.save(odontologo);
    }

    @Override
    public List<Odontologo> listar() {
        LOGGER.info("Lista de todos los odontólogos");
        return iOdontologoRepository.findAll();
    }

    @Override
    public Odontologo buscarPorMatricula(String matricula) {
        LOGGER.info("Se busca el odontólogo con matrícula " + matricula);
        return iOdontologoRepository.findByMatricula(matricula);
    }
}
