package com.TurnosJB.TurnosJB.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "odontologos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Odontologo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private String matricula;
}
