package com.TurnosJB.TurnosJB.service;

import com.TurnosJB.TurnosJB.entity.Paciente;

import java.util.List;

public interface IPacienteService {

    Paciente guardar(Paciente paciente);

    Paciente buscarPorId(Long id);

    void eliminar(Long id);

    Paciente actualizar(Paciente paciente);

    List<Paciente> listar();

    Paciente buscarPorDni(String ced);
}
