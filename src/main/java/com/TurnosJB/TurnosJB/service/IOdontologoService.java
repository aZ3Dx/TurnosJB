package com.TurnosJB.TurnosJB.service;

import com.TurnosJB.TurnosJB.entity.Odontologo;

import java.util.List;

public interface IOdontologoService {

    Odontologo guardar(Odontologo odontologo);

    Odontologo buscarPorId(Long id);

    void eliminar(Long id);

    void actualizar(Odontologo odontologo);

    List<Odontologo> listar();
}
