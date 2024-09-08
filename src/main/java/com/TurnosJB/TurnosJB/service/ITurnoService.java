package com.TurnosJB.TurnosJB.service;

import com.TurnosJB.TurnosJB.entity.Turno;

import java.util.List;

public interface ITurnoService {
    Turno guardar(Turno turno);

    Turno buscarPorId(Long id);

    void eliminar(Long id);

    Turno actualizar(Turno turno);

    List<Turno> listar();

    List<Turno> obtenerTurnosPorPaciente(Long id);

    List<Turno> obtenerTurnosPorOdontologo(Long id);
}
