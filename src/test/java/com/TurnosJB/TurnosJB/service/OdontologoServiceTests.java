package com.TurnosJB.TurnosJB.service;

import com.TurnosJB.TurnosJB.entity.Odontologo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@DisplayName("Servicio de odontologo - Tests")
@SpringBootTest
public class OdontologoServiceTests {

    @Autowired
    private IOdontologoService iOdontologoService;

    private Odontologo crearOdontologo() {
        Odontologo odontologo = new Odontologo();
        odontologo.setNombre("Omar");
        odontologo.setApellido("Peña");
        odontologo.setMatricula("1234");
        return odontologo;
    }

    @DisplayName("Guardar odontologo")
    @Test
    @Transactional
    public void testGuardarOdontologo() {
        // Arrange
        Odontologo odontologo = crearOdontologo();

        // Act
        Odontologo savedOdontologo = iOdontologoService.guardar(odontologo);

        // Assert
        assertEquals(odontologo.getNombre(), savedOdontologo.getNombre());
    }

    @DisplayName("Buscar odontologo por id")
    @Test
    @Transactional
    public void testBuscarOdontologoPorId() {
        // Arrange
        Odontologo odontologo = crearOdontologo();
        Odontologo odontologoGuardado = iOdontologoService.guardar(odontologo);

        // Act
        Odontologo odontologoBuscado = iOdontologoService.buscarPorId(odontologo.getId());

        // Assert
        assertEquals(odontologoGuardado.getId(), odontologoBuscado.getId());
    }

    @DisplayName("Eliminar odontologo")
    @Test
    public void testEliminarOdontologo() {
        // Arrange
        Odontologo odontologo = crearOdontologo();
        Odontologo odontologoGuardado = iOdontologoService.guardar(odontologo);
        int cantidadOdontologosAntes = iOdontologoService.listar().size();

        // Act
        iOdontologoService.eliminar(odontologoGuardado.getId());
        int cantidadOdontologosDespues = iOdontologoService.listar().size();

        // Assert
        assertEquals(cantidadOdontologosAntes - 1, cantidadOdontologosDespues);
    }

    @DisplayName("Actualizar odontologo")
    @Test
    @Transactional
    public void testActualizarOdontologo() {
        // Arrange
        Odontologo odontologo = crearOdontologo();
        Odontologo odontologoGuardado = iOdontologoService.guardar(odontologo);

        // Act
        odontologoGuardado.setMatricula("A321");
        iOdontologoService.actualizar(odontologoGuardado);
        Odontologo odontologoActualizado = iOdontologoService.buscarPorId(odontologoGuardado.getId());

        // Assert
        assertEquals("A321", odontologoActualizado.getMatricula());
    }

    @DisplayName("Listar odontologos")
    @Test
    @Transactional
    public void testListarOdontologos() {
        // Arrange
        Odontologo odontologo1 = crearOdontologo();
        iOdontologoService.guardar(odontologo1);
        Odontologo odontologo2 = crearOdontologo();
        odontologo2.setMatricula("A321");
        iOdontologoService.guardar(odontologo2);

        // Act
        List<Odontologo> odontologos = iOdontologoService.listar();

        // Assert
        assertEquals(2, odontologos.size());
    }

    @DisplayName("Buscar odontologo por matricula")
    @Test
    @Transactional
    public void testBuscarOdontologoPorMatricula() {
        // Arrange
        Odontologo odontologo = crearOdontologo();
        iOdontologoService.guardar(odontologo);

        // Act
        Odontologo odontologoBuscado = iOdontologoService.buscarPorMatricula(odontologo.getMatricula());

        // Assert
        assertEquals(odontologo.getMatricula(), odontologoBuscado.getMatricula());
    }

}
