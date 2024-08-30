package com.TurnosJB.TurnosJB.service;

import com.TurnosJB.TurnosJB.entity.Paciente;

import java.util.List;

public interface IPacienteService {

    void guardar(Paciente paciente);

    Paciente buscarPorId(Long id);

    void eliminar(Long id);

    void actualizar(Paciente paciente);

    List<Paciente> listar();
}
