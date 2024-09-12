package com.TurnosJB.TurnosJB.service;

import com.TurnosJB.TurnosJB.entity.Domicilio;
import com.TurnosJB.TurnosJB.entity.Odontologo;
import com.TurnosJB.TurnosJB.entity.Paciente;
import com.TurnosJB.TurnosJB.entity.Turno;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@DisplayName("Servicio de turno - Tests")
@SpringBootTest
public class TurnoServiceImplTests {

    @Autowired
    private ITurnoService iTurnoService;

    @Autowired
    private IPacienteService iPacienteService;

    @Autowired
    private IOdontologoService iOdontologoService;

    @Autowired
    private EntityManager entityManager;

    private Domicilio crearDomicilio() {
        Domicilio domicilio = new Domicilio();
        domicilio.setCalle("Calle 1");
        domicilio.setNumero(123);
        domicilio.setLocalidad("Loc1");
        domicilio.setProvincia("Prov1");
        return domicilio;
    }

    private Paciente crearPaciente() {
        Paciente paciente = new Paciente();
        paciente.setNombre("Pablo");
        paciente.setApellido("Perez");
        paciente.setDni("12345678");
        paciente.setFechaAlta(LocalDate.now());
        paciente.setDomicilio(crearDomicilio());
        return paciente;
    }

    private Odontologo crearOdontologo() {
        Odontologo odontologo = new Odontologo();
        odontologo.setNombre("Orales");
        odontologo.setApellido("Olas");
        odontologo.setMatricula("1234");
        return odontologo;
    }

    private Turno crearTurno(Paciente paciente, Odontologo odontologo) {
        Turno turno = new Turno();
        turno.setPaciente(paciente);
        turno.setOdontologo(odontologo);
        turno.setFecha(LocalDate.of(2024, 12, 12));
        turno.setHora(LocalTime.of(12, 0, 0));
        return turno;
    }

    @DisplayName("Guardar turno")
    @Test
    @Transactional
    public void testGuardarTurno() {
        // Arrange
        Paciente paciente = crearPaciente();
        iPacienteService.guardar(paciente);
        Odontologo odontologo = crearOdontologo();
        iOdontologoService.guardar(odontologo);
        Turno turno = crearTurno(paciente, odontologo);

        // Act
        Turno turnoGuardado = iTurnoService.guardar(turno);

        // Assert
        assertAll(
                () -> assertEquals(paciente.getNombre(), turnoGuardado.getPaciente().getNombre()),
                () -> assertEquals(odontologo.getNombre(), turnoGuardado.getOdontologo().getNombre()),
                () -> assertEquals(LocalDate.of(2024, 12, 12), turnoGuardado.getFecha()),
                () -> assertEquals(LocalTime.of(12, 0, 0), turnoGuardado.getHora())
        );
    }

    @DisplayName("Buscar turno por id")
    @Test
    @Transactional
    public void testBuscarTurnoPorId() {
        // Arrange
        Paciente paciente = crearPaciente();
        iPacienteService.guardar(paciente);
        Odontologo odontologo = crearOdontologo();
        iOdontologoService.guardar(odontologo);
        Turno turno = crearTurno(paciente, odontologo);
        Turno turnoGuardado = iTurnoService.guardar(turno);
        entityManager.detach(turnoGuardado);

        // Act
        Turno turnoBuscado = iTurnoService.buscarPorId(turnoGuardado.getId());

        // Assert
        assertEquals(turnoGuardado, turnoBuscado);
    }

    @DisplayName("Eliminar turno")
    @Test
    @Transactional
    public void testEliminarTurno() {
        // Arrange
        Paciente paciente = crearPaciente();
        iPacienteService.guardar(paciente);
        Odontologo odontologo = crearOdontologo();
        iOdontologoService.guardar(odontologo);
        Turno turno = crearTurno(paciente, odontologo);
        Turno turnoGuardado = iTurnoService.guardar(turno);
        entityManager.detach(turnoGuardado);
        int cantidadTurnosAntes = iTurnoService.listar().size();

        // Act
        iTurnoService.eliminar(turnoGuardado.getId());
        int cantidadTurnosDespues = iTurnoService.listar().size();

        // Assert
        assertEquals(cantidadTurnosAntes - 1, cantidadTurnosDespues);
    }

    @DisplayName("Actualizar turno")
    @Test
    @Transactional
    public void testActualizarTurno() {
        // Arrange
        Paciente paciente = crearPaciente();
        iPacienteService.guardar(paciente);
        Odontologo odontologo = crearOdontologo();
        iOdontologoService.guardar(odontologo);
        Turno turno = crearTurno(paciente, odontologo);
        Turno turnoGuardado = iTurnoService.guardar(turno);
        entityManager.detach(turnoGuardado);

        // Act
        Turno turnoAActualzar = new Turno();
        turnoAActualzar.setId(turnoGuardado.getId());
        turnoAActualzar.setFecha(LocalDate.of(2024, 12, 12));
        turnoAActualzar.setHora(LocalTime.of(0, 0, 0));
        turnoAActualzar.setPaciente(turnoGuardado.getPaciente());
        turnoAActualzar.setOdontologo(turnoGuardado.getOdontologo());
        //turnoGuardado.setHora(LocalTime.of(0, 0, 0));
        Turno turnoActualizado = iTurnoService.actualizar(turnoAActualzar);
        entityManager.detach(turnoActualizado);

        // Assert
        assertAll(
                () -> assertNotEquals(turnoGuardado, turnoActualizado),
                () -> assertEquals(LocalTime.of(0, 0, 0), turnoActualizado.getHora())
        );
    }

    @DisplayName("Listar turnos")
    @Test
    @Transactional
    public void testListarTurnos() {
        // Arrange
        Paciente paciente1 = crearPaciente();
        iPacienteService.guardar(paciente1);
        Paciente paciente2 = crearPaciente();
        paciente2.setDni("87654321");
        iPacienteService.guardar(paciente2);
        Odontologo odontologo1 = crearOdontologo();
        iOdontologoService.guardar(odontologo1);
        Odontologo odontologo2 = crearOdontologo();
        odontologo2.setMatricula("4321");
        iOdontologoService.guardar(odontologo2);
        Turno turno1 = crearTurno(paciente1, odontologo1);
        Turno turno2 = crearTurno(paciente2, odontologo2);
        iTurnoService.guardar(turno1);
        iTurnoService.guardar(turno2);

        // Act
        List<Turno> turnos = iTurnoService.listar();

        // Assert
        assertEquals(2, turnos.size());
    }

    @DisplayName("Listar turnos por paciente")
    @Test
    @Transactional
    public void testListarTurnosPorPaciente() {
        // Arrange
        Paciente paciente = crearPaciente();
        iPacienteService.guardar(paciente);
        Odontologo odontologo = crearOdontologo();
        iOdontologoService.guardar(odontologo);
        Turno turno1 = crearTurno(paciente, odontologo);
        Turno turno2 = crearTurno(paciente, odontologo);
        turno2.setFecha(LocalDate.of(2024, 12, 24));
        iTurnoService.guardar(turno1);
        iTurnoService.guardar(turno2);
        Long pacienteId = iPacienteService.listar().getFirst().getId();

        // Act
        List<Turno> turnos = iTurnoService.obtenerTurnosPorPaciente(pacienteId);

        // Assert
        assertEquals(2, turnos.size());
    }

    @DisplayName("Listar turnos por odontologo")
    @Test
    @Transactional
    public void testListarTurnosPorOdontologo() {
        // Arrange
        Paciente paciente = crearPaciente();
        iPacienteService.guardar(paciente);
        Odontologo odontologo = crearOdontologo();
        iOdontologoService.guardar(odontologo);
        Turno turno1 = crearTurno(paciente, odontologo);
        Turno turno2 = crearTurno(paciente, odontologo);
        turno2.setFecha(LocalDate.of(2024, 12, 24));
        iTurnoService.guardar(turno1);
        iTurnoService.guardar(turno2);
        Long odontologoId = iOdontologoService.listar().getFirst().getId();

        // Act
        List<Turno> turnos = iTurnoService.obtenerTurnosPorOdontologo(odontologoId);

        // Assert
        assertEquals(2, turnos.size());
    }
}
