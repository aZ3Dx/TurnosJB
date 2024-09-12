package com.TurnosJB.TurnosJB.controller;

import com.TurnosJB.TurnosJB.entity.Domicilio;
import com.TurnosJB.TurnosJB.entity.Odontologo;
import com.TurnosJB.TurnosJB.entity.Paciente;
import com.TurnosJB.TurnosJB.entity.Turno;
import com.TurnosJB.TurnosJB.service.ITurnoService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@DisplayName("Controlador de turnos - Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(TurnoController.class)
public class TurnoControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ITurnoService iTurnoService;

    @BeforeEach
    public void setUp() {
        Turno turno1 = new Turno(
                1L,
                new Paciente(1L, "Juan", "Perez", "123456", LocalDate.now(),
                        new Domicilio(1L, "Calle 1", 1, "Localidad 1", "Provincia 1")),
                new Odontologo(1L, "Ana", "Martinez", "987654"),
                LocalDate.now(),
                LocalTime.now());
        Turno turno2 = new Turno(
                2L,
                new Paciente(2L, "Juan", "Perez", "123456", LocalDate.now(),
                        new Domicilio(2L, "Calle 1", 1, "Localidad 1", "Provincia 1")),
                new Odontologo(1L, "Ana", "Martinez", "987654"),
                LocalDate.now(),
                LocalTime.now());
        List<Turno> turnos = Arrays.asList(turno1, turno2);
        given(iTurnoService.listar()).willReturn(turnos);
        given(iTurnoService.buscarPorId(1L)).willReturn(turno1);
        given(iTurnoService.actualizar(Mockito.any(Turno.class))).willReturn(turno1);
        given(iTurnoService.guardar(Mockito.any(Turno.class))).willReturn(turno1);
        given(iTurnoService.obtenerTurnosPorPaciente(1L)).willReturn(turnos);
    }

    @DisplayName("Listar turnos - GET /turnos")
    @Order(1)
    @Test
    public void testListarTurnos() throws Exception {
        mvc.perform(get("/turnos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].paciente.nombre").value("Juan"));
    }

    @DisplayName("Obtener turno por id - GET /turnos/{id}")
    @Order(2)
    @Test
    public void testConsultarTurnoPorId() throws Exception {
        mvc.perform(get("/turnos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['id']").value(1));
    }

    @DisplayName("Actualizar turno - PUT /turnos")
    @Order(3)
    @Test
    public void testActualizarTurno() throws Exception {
        mvc.perform(put("/turnos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"paciente\":{\"id\":1,\"nombre\":\"Juan\",\"apellido\":\"Perez\",\"dni\":\"123456\",\"fechaAlta\":\"2024-09-10\"}," +
                                "\"odontologo\":{\"id\":1,\"nombre\":\"Ana\",\"apellido\":\"Martinez\",\"matricula\":\"987654\"}," +
                                "\"fecha\":\"2024-09-10\",\"hora\":\"10:00\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$['id']").value(1));
    }

    @DisplayName("Eliminar turno - DELETE /turnos/{id}")
    @Order(4)
    @Test
    public void testEliminarTurno() throws Exception {
        mvc.perform(delete("/turnos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Guardar turno - POST /turnos")
    @Order(5)
    @Test
    public void testGuardarTurno() throws Exception {
        mvc.perform(post("/turnos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"paciente\":{\"id\":1,\"nombre\":\"Juan\",\"apellido\":\"Perez\",\"dni\":\"123456\",\"fechaAlta\":\"2024-09-10\"}," +
                                "\"odontologo\":{\"id\":1,\"nombre\":\"Ana\",\"apellido\":\"Martinez\",\"matricula\":\"987654\"}," +
                                "\"fecha\":\"2024-09-10\",\"hora\":\"10:00\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$['id']").value(1));
    }

    @DisplayName("Listar turnos por paciente - GET /turnos/paciente/{id}")
    @Order(6)
    @Test
    public void testListarTurnosPorPaciente() throws Exception {
        mvc.perform(get("/turnos/paciente/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].paciente.id").value(1));
    }
}
