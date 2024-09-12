package com.TurnosJB.TurnosJB.service;

import com.TurnosJB.TurnosJB.entity.Domicilio;
import com.TurnosJB.TurnosJB.entity.Paciente;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@DisplayName("Servicio de paciente - Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class PacienteServiceTests {

    @Autowired
    private IPacienteService iPacienteService;

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
        paciente.setNombre("Paco");
        paciente.setApellido("Peña");
        paciente.setDni("12345678");
        paciente.setFechaAlta(LocalDate.now());
        paciente.setDomicilio(crearDomicilio());
        return paciente;
    }

    @DisplayName("Guardar paciente")
    @Order(1)
    @Test
    @Transactional
    public void testGuardarPaciente() {
        // Arrange
        Paciente paciente = crearPaciente();

        // Act
        Paciente pacienteGuardado = iPacienteService.guardar(crearPaciente());

        // Assert
        assertAll(
                () -> assertEquals(paciente.getNombre(), pacienteGuardado.getNombre()),
                () -> assertEquals(paciente.getApellido(), pacienteGuardado.getApellido()),
                () -> assertEquals(paciente.getDni(), pacienteGuardado.getDni()),
                () -> assertEquals(paciente.getFechaAlta(), pacienteGuardado.getFechaAlta()),
                () -> assertEquals(paciente.getDomicilio().getCalle(), pacienteGuardado.getDomicilio().getCalle()),
                () -> assertEquals(paciente.getDomicilio().getNumero(), pacienteGuardado.getDomicilio().getNumero()),
                () -> assertEquals(paciente.getDomicilio().getLocalidad(), pacienteGuardado.getDomicilio().getLocalidad()),
                () -> assertEquals(paciente.getDomicilio().getProvincia(), pacienteGuardado.getDomicilio().getProvincia())
        );
    }

    @DisplayName("Buscar paciente por id")
    @Order(2)
    @Test
    @Transactional
    public void testBuscarPacientePorId() {
        // Arrange
        Paciente paciente = crearPaciente();
        Paciente pacienteGuardado = iPacienteService.guardar(paciente);
        entityManager.detach(pacienteGuardado);

        // Act
        Paciente pacienteBuscado = iPacienteService.buscarPorId(pacienteGuardado.getId());

        // Assert
        assertEquals(pacienteGuardado, pacienteBuscado);
    }

    @DisplayName("Eliminar paciente")
    @Order(3)
    @Test
    public void testEliminarPaciente() {
        // Arrange
        Paciente paciente = crearPaciente();
        Paciente pacienteGuardado = iPacienteService.guardar(paciente);
        entityManager.detach(pacienteGuardado);
        int cantidadPacientesAntes = iPacienteService.listar().size();

        // Act
        iPacienteService.eliminar(pacienteGuardado.getId());
        int cantidadPacientesDespues = iPacienteService.listar().size();

        // Assert
        assertEquals(cantidadPacientesAntes - 1, cantidadPacientesDespues);
    }

    @DisplayName("Actualizar paciente")
    @Order(4)
    @Test
    @Transactional
    public void testActualizarPaciente() {
        // Arrange
        Paciente paciente = crearPaciente();
        Paciente pacienteGuardado = iPacienteService.guardar(paciente);
        entityManager.detach(pacienteGuardado);

        // Act
        Paciente pacienteAActualizar = new Paciente();
        pacienteAActualizar.setId(pacienteGuardado.getId());
        pacienteAActualizar.setDni("87654321");
        Paciente pacienteActualizado = iPacienteService.actualizar(pacienteAActualizar);
        entityManager.detach(pacienteActualizado);

        // Assert
        assertAll(
                () -> assertNotEquals(pacienteGuardado, pacienteActualizado),
                () -> assertEquals("87654321", pacienteActualizado.getDni())
        );
    }

    @DisplayName("Listar pacientes")
    @Order(5)
    @Test
    @Transactional
    public void testListarPacientes() {
        // Arrange
        Paciente paciente1 = crearPaciente();
        Paciente paciente2 = crearPaciente();
        paciente2.setDni("87654321");
        iPacienteService.guardar(paciente1);
        iPacienteService.guardar(paciente2);

        // Act
        List<Paciente> pacientes = iPacienteService.listar();

        // Assert
        assertEquals(2, pacientes.size());
    }

    @DisplayName("Buscar paciente por dni")
    @Order(6)
    @Test
    @Transactional
    public void testBuscarPacientePorDni() {
        // Arrange
        Paciente paciente = crearPaciente();
        Paciente pacienteGuardado = iPacienteService.guardar(paciente);
        entityManager.detach(pacienteGuardado);

        // Act
        Paciente pacienteBuscado = iPacienteService.buscarPorDni(pacienteGuardado.getDni());

        // Assert
        assertEquals(pacienteGuardado, pacienteBuscado);
    }
}
