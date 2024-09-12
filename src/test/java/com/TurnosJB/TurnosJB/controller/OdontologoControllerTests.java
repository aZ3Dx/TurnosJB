package com.TurnosJB.TurnosJB.controller;

import com.TurnosJB.TurnosJB.entity.Odontologo;
import com.TurnosJB.TurnosJB.service.IOdontologoService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@DisplayName("Controlador de odontologos - Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(OdontologoController.class)
public class OdontologoControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IOdontologoService iOdontologoService;

    @BeforeEach
    public void setUp() {
        Odontologo odontologo1 = new Odontologo(1L, "Oder", "Perez", "A123456");
        Odontologo odontologo2 = new Odontologo(2L, "Opal", "Gonzalez", "B234567");
        List<Odontologo> odontologos = Arrays.asList(odontologo1, odontologo2);
        given(iOdontologoService.listar()).willReturn(odontologos);
        given(iOdontologoService.buscarPorId(1L)).willReturn(odontologo1);
        given(iOdontologoService.actualizar(Mockito.any(Odontologo.class))).willReturn(odontologo1);
        given(iOdontologoService.guardar(Mockito.any(Odontologo.class))).willReturn(odontologo1);
    }

    @DisplayName("Listar odontologos - GET /odontologos")
    @Order(1)
    @Test
    public void testListarOdontologos() throws Exception {
        mvc.perform(get("/odontologos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").value("Oder"));
    }

    @DisplayName("Obtener odontologo por id - GET /odontologos/{id}")
    @Order(2)
    @Test
    public void testConsultarOdontologoPorId() throws Exception {
        mvc.perform(get("/odontologos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"nombre\":\"Oder\",\"apellido\":\"Perez\",\"matricula\":\"A123456\"}"));
    }

    @DisplayName("Actualizar odontologo - PUT /odontologos")
    @Order(3)
    @Test
    public void testActualizarOdontologo() throws Exception {
        mvc.perform(put("/odontologos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"nombre\":\"Oder\",\"apellido\":\"Perez\",\"matricula\":\"A123456\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":1,\"nombre\":\"Oder\",\"apellido\":\"Perez\",\"matricula\":\"A123456\"}"));
    }

    @DisplayName("Eliminar odontologo - DELETE /odontologos/{id}")
    @Order(4)
    @Test
    public void testEliminarOdontologo() throws Exception {
        mvc.perform(delete("/odontologos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Guardar odontologo - POST /odontologos")
    @Order(5)
    @Test
    public void testGuardarOdontologo() throws Exception {
        mvc.perform(post("/odontologos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"nombre\":\"Oder\",\"apellido\":\"Perez\",\"matricula\":\"A123456\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":1,\"nombre\":\"Oder\",\"apellido\":\"Perez\",\"matricula\":\"A123456\"}"));
    }

}
