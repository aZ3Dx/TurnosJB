package com.TurnosJB.TurnosJB.service;

import com.TurnosJB.TurnosJB.entity.Domicilio;
import com.TurnosJB.TurnosJB.entity.Odontologo;
import com.TurnosJB.TurnosJB.entity.Paciente;
import com.TurnosJB.TurnosJB.entity.Turno;
import com.TurnosJB.TurnosJB.service.impl.OdontologoServiceImpl;
import com.TurnosJB.TurnosJB.service.impl.PacienteServiceImpl;
import com.TurnosJB.TurnosJB.service.impl.TurnoServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@SpringBootTest
public class TurnoServiceImplTests {
    @Autowired
    private TurnoServiceImpl turnoService;

    @Autowired
    private PacienteServiceImpl pacienteService;

    @Autowired
    private OdontologoServiceImpl odontologoService;

    private Paciente crearPaciente() {
        Paciente paciente = new Paciente();
        paciente.setNombre("Pablo");
        paciente.setApellido("Perez");
        paciente.setDni("12345678");
        paciente.setFechaAlta(LocalDate.now());
        paciente.setDomicilio(new Domicilio());
        return pacienteService.guardar(paciente);
    }

    private Odontologo crearOdontologo() {
        Odontologo odontologo = new Odontologo();
        odontologo.setNombre("Orales");
        odontologo.setApellido("Olas");
        odontologo.setMatricula(1234);
        return odontologoService.guardar(odontologo);
    }

    @Test
    @Transactional
    public void testGuardarTurno() {
        // Arrange
        Paciente paciente = crearPaciente();
        Odontologo odontologo = crearOdontologo();
        Turno turno = new Turno();
        turno.setPaciente(paciente);
        turno.setOdontologo(odontologo);
        turno.setFecha(LocalDate.parse("2024-10-10"));
        turno.setHora(LocalTime.parse("10:00:00"));

        // Act
        Turno turnoGuardado = turnoService.guardar(turno);

        // Assert
        assertAll("Verificar turno guardado",
                () -> assertNotNull(turnoGuardado.getId()),
                () -> assertEquals(paciente, turnoGuardado.getPaciente()),
                () -> assertEquals(odontologo, turnoGuardado.getOdontologo()),
                () -> assertEquals(LocalDate.parse("2024-10-10"), turnoGuardado.getFecha()),
                () -> assertEquals(LocalTime.parse("10:00:00"), turnoGuardado.getHora())
        );
    }

    @Test
    @Transactional
    public void testBuscarTurnoPorId() {
        // Arrange
        Paciente paciente = crearPaciente();
        Odontologo odontologo = crearOdontologo();
        Turno turno = new Turno();
        turno.setPaciente(paciente);
        turno.setOdontologo(odontologo);
        turno.setFecha(LocalDate.parse("2024-10-10"));
        turno.setHora(LocalTime.parse("10:00:00"));
        Turno turnoGuardado = turnoService.guardar(turno);

        // Act
        Turno turnoBuscado = turnoService.buscarPorId(turnoGuardado.getId());

        // Assert
        assertAll("Verificar turno guardado",
                () -> assertNotNull(turnoBuscado.getId()),
                () -> assertEquals(turnoGuardado.getPaciente(), turnoBuscado.getPaciente()),
                () -> assertEquals(turnoGuardado.getOdontologo(), turnoBuscado.getOdontologo()),
                () -> assertEquals(turnoGuardado.getFecha(), turnoBuscado.getFecha()),
                () -> assertEquals(turnoGuardado.getHora(), turnoBuscado.getHora())
        );
    }

    @Test
    public void testEliminarTurno() {
        // Arrange
        Paciente paciente = crearPaciente();
        Odontologo odontologo = crearOdontologo();
        Turno turno = new Turno();
        turno.setPaciente(paciente);
        turno.setOdontologo(odontologo);
        turno.setFecha(LocalDate.parse("2024-10-10"));
        turno.setHora(LocalTime.parse("10:00:00"));
        Turno turnoGuardado = turnoService.guardar(turno);
        int cantidadTurnosAntes = turnoService.listar().size();

        // Act
        turnoService.eliminar(turnoGuardado.getId());
        int cantidadTurnosDespues = turnoService.listar().size();

        // Assert
        assertEquals(cantidadTurnosAntes - 1, cantidadTurnosDespues);
    }

    @Test
    @Transactional
    public void testActualizarTurno() {
        // Arrange
        Paciente paciente = crearPaciente();
        Odontologo odontologo = crearOdontologo();
        Turno turno = new Turno();
        turno.setPaciente(paciente);
        turno.setOdontologo(odontologo);
        turno.setFecha(LocalDate.parse("2024-10-10"));
        turno.setHora(LocalTime.parse("10:00:00"));
        Turno turnoGuardado = turnoService.guardar(turno);

        // Act
        turnoGuardado.setHora(LocalTime.parse("20:00:00"));
        Turno turnoActualizado = turnoService.actualizar(turnoGuardado);

        // Assert
        assertEquals(LocalTime.parse("20:00:00"), turnoActualizado.getHora());
    }

    @Test
    @Transactional
    public void testListarTurnos() {
        // Arrange
        Paciente paciente1 = crearPaciente();
        Paciente paciente2 = crearPaciente();
        Odontologo odontologo = crearOdontologo();
        Turno turno1 = new Turno();
        turno1.setPaciente(paciente1);
        turno1.setOdontologo(odontologo);
        turno1.setFecha(LocalDate.parse("2024-10-10"));
        turno1.setHora(LocalTime.parse("10:00:00"));
        Turno turno2 = new Turno();
        turno2.setPaciente(paciente2);
        turno2.setOdontologo(odontologo);
        turno2.setFecha(LocalDate.parse("2024-10-10"));
        turno2.setHora(LocalTime.parse("12:00:00"));
        turnoService.guardar(turno1);
        turnoService.guardar(turno2);

        // Act
        List<Turno> turnos = turnoService.listar();

        // Assert
        assertEquals(2, turnos.size());
    }

    @Test
    @Transactional
    public void testListarTurnosPorPaciente() {
        // Arrange
        Paciente paciente = crearPaciente();
        Odontologo odontologo = crearOdontologo();
        Turno turno1 = new Turno();
        turno1.setPaciente(paciente);
        turno1.setOdontologo(odontologo);
        turno1.setFecha(LocalDate.parse("2024-10-10"));
        turno1.setHora(LocalTime.parse("10:00:00"));
        turnoService.guardar(turno1);
        Turno turno2 = new Turno();
        turno2.setPaciente(paciente);
        turno2.setOdontologo(odontologo);
        turno2.setFecha(LocalDate.parse("2024-10-20"));
        turno2.setHora(LocalTime.parse("10:00:00"));
        turnoService.guardar(turno2);

        // Act
        List<Turno> turnos = turnoService.obtenerTurnosPorPaciente(paciente.getId());

        // Assert
        assertEquals(2, turnos.size());
    }

    @Test
    @Transactional
    public void testListarTurnosPorOdontologo() {
        // Arrange
        Paciente paciente = crearPaciente();
        Odontologo odontologo = crearOdontologo();
        Turno turno1 = new Turno();
        turno1.setPaciente(paciente);
        turno1.setOdontologo(odontologo);
        turno1.setFecha(LocalDate.parse("2024-10-10"));
        turno1.setHora(LocalTime.parse("10:00:00"));
        turnoService.guardar(turno1);
        Turno turno2 = new Turno();
        turno2.setPaciente(paciente);
        turno2.setOdontologo(odontologo);
        turno2.setFecha(LocalDate.parse("2024-10-20"));
        turno2.setHora(LocalTime.parse("10:00:00"));
        turnoService.guardar(turno2);

        // Act
        List<Turno> turnos = turnoService.obtenerTurnosPorOdontologo(odontologo.getId());

        // Assert
        assertEquals(2, turnos.size());
    }
}
