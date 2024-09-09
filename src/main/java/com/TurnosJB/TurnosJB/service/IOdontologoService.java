package com.TurnosJB.TurnosJB.service;

import com.TurnosJB.TurnosJB.entity.Odontologo;

import java.util.List;

public interface IOdontologoService {

    Odontologo guardar(Odontologo odontologo);

    Odontologo buscarPorId(Long id);

    void eliminar(Long id);

    Odontologo actualizar(Odontologo odontologo);

    List<Odontologo> listar();

    Odontologo buscarPorMatricula(String matricula);
}
