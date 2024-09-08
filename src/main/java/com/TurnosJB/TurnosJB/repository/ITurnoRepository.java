package com.TurnosJB.TurnosJB.repository;

import com.TurnosJB.TurnosJB.entity.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITurnoRepository extends JpaRepository<Turno, Long> {

    List<Turno> findByPacienteId(Long id);

    List<Turno> findByOdontologoId(Long id);
}
