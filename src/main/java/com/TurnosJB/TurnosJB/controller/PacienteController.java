package com.TurnosJB.TurnosJB.controller;

import com.TurnosJB.TurnosJB.entity.Paciente;
import com.TurnosJB.TurnosJB.service.IPacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private IPacienteService iPacienteService;

    @GetMapping
    public ResponseEntity<List<Paciente>> listar() {
        return ResponseEntity.ok(iPacienteService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> consultarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(iPacienteService.buscarPorId(id));
    }

    @PutMapping
    public ResponseEntity<Paciente> actualizar(@RequestBody Paciente paciente) {
        return ResponseEntity.ok(iPacienteService.actualizar(paciente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        iPacienteService.eliminar(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Paciente> guardar(@RequestBody Paciente paciente) {
        return ResponseEntity.ok(iPacienteService.guardar(paciente));
    }
}
