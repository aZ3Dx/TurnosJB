package com.TurnosJB.TurnosJB.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Paciente paciente;
    private Odontologo odontologo;
    private LocalDate fecha;
    private LocalTime hora;
}
