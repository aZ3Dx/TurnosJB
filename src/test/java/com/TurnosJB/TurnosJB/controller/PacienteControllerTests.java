package com.TurnosJB.TurnosJB.controller;

import com.TurnosJB.TurnosJB.entity.Domicilio;
import com.TurnosJB.TurnosJB.entity.Paciente;
import com.TurnosJB.TurnosJB.service.IPacienteService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@DisplayName("Controlador de pacientes - Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(PacienteController.class)
public class PacienteControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IPacienteService iPacienteService;

    @BeforeEach
    public void setUp() {
        Paciente paciente1 = new Paciente(1L, "Juan", "Perez", "123456", LocalDate.of(2024, 1, 1), new Domicilio(1L, "Calle 1", 1, "Localidad 1", "Provincia 1"));
        Paciente paciente2 = new Paciente(2L, "Pedro", "Gonzalez", "234567", LocalDate.of(2024, 2, 2), new Domicilio(2L, "Calle 2", 2, "Localidad 2", "Provincia 2"));
        List<Paciente> pacientes = Arrays.asList(paciente1, paciente2);
        given(iPacienteService.listar()).willReturn(pacientes);
        given(iPacienteService.buscarPorId(1L)).willReturn(paciente1);
        given(iPacienteService.actualizar(Mockito.any(Paciente.class))).willReturn(paciente1);
        given(iPacienteService.guardar(Mockito.any(Paciente.class))).willReturn(paciente1);
    }

    @DisplayName("Listar pacientes - GET /pacientes")
    @Order(1)
    @Test
    public void testListarPacientes() throws Exception {
        mvc.perform(get("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }

    @DisplayName("Obtener paciente por id - GET /pacientes/{id}")
    @Order(2)
    @Test
    public void testConsultarPacientePorId() throws Exception {
        mvc.perform(get("/pacientes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"nombre\":\"Juan\",\"apellido\":\"Perez\",\"dni\":\"123456\",\"fechaAlta\":\"2024-01-01\"}"));
    }

    @DisplayName("Actualizar paciente - PUT /pacientes")
    @Order(3)
    @Test
    public void testActualizarPaciente() throws Exception {
        mvc.perform(put("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"nombre\":\"Juan\",\"apellido\":\"Perez\",\"dni\":\"123456\",\"fechaAlta\":\"2024-01-01\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":1,\"nombre\":\"Juan\",\"apellido\":\"Perez\",\"dni\":\"123456\",\"fechaAlta\":\"2024-01-01\"}"));
    }

    @DisplayName("Eliminar paciente - DELETE /pacientes/{id}")
    @Order(4)
    @Test
    public void testEliminarPaciente() throws Exception {
        mvc.perform(delete("/pacientes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Guardar paciente - POST /pacientes")
    @Order(5)
    @Test
    public void testGuardarPaciente() throws Exception {
        mvc.perform(post("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Juan\",\"apellido\":\"Perez\",\"dni\":\"123456\",\"fechaAlta\":\"2024-01-01\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":1,\"nombre\":\"Juan\",\"apellido\":\"Perez\",\"dni\":\"123456\",\"fechaAlta\":\"2024-01-01\"}"));
    }

}
