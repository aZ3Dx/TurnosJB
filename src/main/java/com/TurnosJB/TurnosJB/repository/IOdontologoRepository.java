package com.TurnosJB.TurnosJB.repository;

import com.TurnosJB.TurnosJB.entity.Odontologo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface IOdontologoRepository extends JpaRepository<Odontologo, Long> {
}
