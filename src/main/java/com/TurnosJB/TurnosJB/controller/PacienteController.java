package com.TurnosJB.TurnosJB.controller;

import com.TurnosJB.TurnosJB.entity.Paciente;
import com.TurnosJB.TurnosJB.service.IPacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final IPacienteService iPacienteService;

    @Autowired
    public PacienteController(IPacienteService iPacienteService) {
        this.iPacienteService = iPacienteService;
    }

    @GetMapping
    public ResponseEntity<List<Paciente>> listar() {
        return ResponseEntity.status(HttpStatus.OK).body(iPacienteService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> consultarPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(iPacienteService.buscarPorId(id));
    }

    @PutMapping
    public ResponseEntity<Paciente> actualizar(@RequestBody Paciente paciente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(iPacienteService.actualizar(paciente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        iPacienteService.eliminar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<Paciente> guardar(@RequestBody Paciente paciente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(iPacienteService.guardar(paciente));
    }
}
