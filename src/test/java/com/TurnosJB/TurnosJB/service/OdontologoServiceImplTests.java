package com.TurnosJB.TurnosJB.service;

import com.TurnosJB.TurnosJB.entity.Odontologo;
import com.TurnosJB.TurnosJB.service.impl.OdontologoServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class OdontologoServiceImplTests {

    @Autowired
    private OdontologoServiceImpl odontologoService;

    private Odontologo crearOdontologo() {
        Odontologo odontologo = new Odontologo();
        odontologo.setNombre("Omar");
        odontologo.setApellido("Peña");
        odontologo.setMatricula("1234");
        return odontologo;
    }

    @Test
    @Transactional
    public void testGuardarOdontologo() {
        // Arrange
        Odontologo odontologo = crearOdontologo();

        // Act
        Odontologo savedOdontologo = odontologoService.guardar(odontologo);

        // Assert
        assertAll("Verificar odontologo guardado",
                () -> assertNotNull(savedOdontologo.getId()),
                () -> assertEquals(odontologo.getNombre(), savedOdontologo.getNombre()),
                () -> assertEquals(odontologo.getApellido(), savedOdontologo.getApellido()),
                () -> assertEquals(odontologo.getMatricula(), savedOdontologo.getMatricula())
        );
    }

    @Test
    @Transactional
    public void testBuscarOdontologoPorId() {
        // Arrange
        Odontologo odontologo = crearOdontologo();
        Odontologo odontologoGuardado = odontologoService.guardar(odontologo);

        // Act
        Odontologo odontologoBuscado = odontologoService.buscarPorId(odontologo.getId());

        // Assert
        assertAll("Verificar odontologo buscado",
                () -> assertNotNull(odontologoBuscado),
                () -> assertEquals(odontologoGuardado.getId(), odontologoBuscado.getId()),
                () -> assertEquals(odontologoGuardado.getNombre(), odontologoBuscado.getNombre()),
                () -> assertEquals(odontologoGuardado.getApellido(), odontologoBuscado.getApellido()),
                () -> assertEquals(odontologoGuardado.getMatricula(), odontologoBuscado.getMatricula())
        );
    }

    @Test
    public void testEliminarOdontologo() {
        // Arrange
        Odontologo odontologo = crearOdontologo();
        Odontologo odontologoGuardado = odontologoService.guardar(odontologo);
        int cantidadOdontologosAntes = odontologoService.listar().size();

        // Act
        odontologoService.eliminar(odontologoGuardado.getId());
        int cantidadOdontologosDespues = odontologoService.listar().size();

        // Assert
        assertEquals(cantidadOdontologosAntes - 1, cantidadOdontologosDespues);
    }

    @Test
    @Transactional
    public void testActualizarOdontologo() {
        // Arrange
        Odontologo odontologo = crearOdontologo();
        Odontologo odontologoGuardado = odontologoService.guardar(odontologo);

        // Act
        odontologoGuardado.setMatricula("A321");
        odontologoService.actualizar(odontologoGuardado);
        Odontologo odontologoActualizado = odontologoService.buscarPorId(odontologoGuardado.getId());

        // Assert
        assertAll("Verificar odontologo actualizado",
                () -> assertEquals("A321", odontologoActualizado.getMatricula())
        );
    }

    @Test
    @Transactional
    public void testListarOdontologos() {
        // Arrange
        Odontologo odontologo1 = crearOdontologo();
        odontologoService.guardar(odontologo1);
        Odontologo odontologo2 = crearOdontologo();
        odontologo2.setMatricula("A321");
        odontologoService.guardar(odontologo2);

        // Act
        List<Odontologo> odontologos = odontologoService.listar();

        // Assert
        assertEquals(2, odontologos.size());
    }

}
