package com.TurnosJB.TurnosJB.repository;

import com.TurnosJB.TurnosJB.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface IPacienteRepository extends JpaRepository<Paciente, Long> {
}
