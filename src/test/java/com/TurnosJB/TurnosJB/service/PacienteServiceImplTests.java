package com.TurnosJB.TurnosJB.service;

import com.TurnosJB.TurnosJB.entity.Domicilio;
import com.TurnosJB.TurnosJB.entity.Paciente;
import com.TurnosJB.TurnosJB.service.impl.PacienteServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class PacienteServiceImplTests {
    @Autowired
    private PacienteServiceImpl pacienteService;

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
        paciente.setNombre("Paco");
        paciente.setApellido("Peña");
        paciente.setDni("12345678");
        paciente.setFechaAlta(LocalDate.now());
        paciente.setDomicilio(crearDomicilio());
        return paciente;
    }

    @Test
    @Transactional
    public void testGuardarPaciente() {
        // Arrange
        Paciente paciente = crearPaciente();

        // Act
        Paciente savedPaciente = pacienteService.guardar(paciente);

        // Assert
        assertAll("Verificar paciente guardado",
                () -> assertNotNull(savedPaciente.getId()),
                () -> assertEquals(paciente.getNombre(), savedPaciente.getNombre()),
                () -> assertEquals(paciente.getApellido(), savedPaciente.getApellido()),
                () -> assertEquals(paciente.getDni(), savedPaciente.getDni()),
                () -> assertNotNull(savedPaciente.getDomicilio())
        );
    }

    @Test
    @Transactional
    public void testBuscarPacientePorId() {
        // Arrange
        Paciente paciente = crearPaciente();
        Paciente pacienteGuardado = pacienteService.guardar(paciente);

        // Act
        Paciente pacienteBuscado = pacienteService.buscarPorId(pacienteGuardado.getId());

        // Assert
        assertAll("Verificar paciente buscado",
                () -> assertNotNull(pacienteBuscado),
                () -> assertEquals(pacienteGuardado.getId(), pacienteBuscado.getId()),
                () -> assertEquals(pacienteGuardado.getNombre(), pacienteBuscado.getNombre()),
                () -> assertEquals(pacienteGuardado.getApellido(), pacienteBuscado.getApellido()),
                () -> assertEquals(pacienteGuardado.getDni(), pacienteBuscado.getDni()),
                () -> assertEquals(pacienteGuardado.getDomicilio(), pacienteBuscado.getDomicilio())
        );
    }

    @Test
    public void testEliminarPaciente() {
        // Arrange
        Paciente paciente = crearPaciente();
        Paciente pacienteGuardado = pacienteService.guardar(paciente);
        int cantidadPacientesAntes = pacienteService.listar().size();

        // Act
        pacienteService.eliminar(pacienteGuardado.getId());
        int cantidadPacientesDespues = pacienteService.listar().size();

        // Assert
        assertEquals(cantidadPacientesAntes - 1, cantidadPacientesDespues);
    }

    @Test
    @Transactional
    public void testActualizarPaciente() {
        // Arrange
        Paciente paciente = crearPaciente();
        Paciente pacienteGuardado = pacienteService.guardar(paciente);

        // Act
        pacienteGuardado.setDni("87654321");
        pacienteService.actualizar(pacienteGuardado);
        Paciente pacienteActualizado = pacienteService.buscarPorId(pacienteGuardado.getId());

        // Assert
        assertAll("Verificar paciente actualizado",
                () -> assertEquals("87654321", pacienteActualizado.getDni())
        );
    }

    @Test
    @Transactional
    public void testListarPacientes() {
        // Arrange
        Paciente paciente1 = crearPaciente();
        pacienteService.guardar(paciente1);

        Paciente paciente2 = crearPaciente();
        paciente2.setDni("87654321");
        pacienteService.guardar(paciente2);

        // Act
        List<Paciente> pacientes = pacienteService.listar();

        // Assert
        assertEquals(2, pacientes.size());
    }

    @Test
    @Transactional
    public void testBuscarPacientePorDni() {
        // Arrange
        Paciente paciente = crearPaciente();
        pacienteService.guardar(paciente);

        // Act
        Paciente pacienteBuscado = pacienteService.buscarPorDni(paciente.getDni());

        // Assert
        assertEquals(paciente, pacienteBuscado);
    }
}
