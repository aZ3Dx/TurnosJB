package com.TurnosJB.TurnosJB.repository;

import com.TurnosJB.TurnosJB.entity.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ITurnoRepository extends JpaRepository<Turno, Long> {
}
