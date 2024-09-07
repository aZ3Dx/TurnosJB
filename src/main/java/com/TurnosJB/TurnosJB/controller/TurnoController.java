package com.TurnosJB.TurnosJB.controller;

import com.TurnosJB.TurnosJB.entity.Turno;
import com.TurnosJB.TurnosJB.service.ITurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/turnos")
public class TurnoController {

    @Autowired
    private ITurnoService iTurnoService;

    @GetMapping
    public ResponseEntity<List<Turno>> listar() {
        return ResponseEntity.ok(iTurnoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Turno> consultarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(iTurnoService.buscarPorId(id));
    }

    @PutMapping
    public ResponseEntity<Turno> actualizar(@RequestBody Turno turno) {
        return ResponseEntity.ok(iTurnoService.actualizar(turno));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        iTurnoService.eliminar(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Turno> guardar(@RequestBody Turno turno) {
        return ResponseEntity.ok(iTurnoService.guardar(turno));
    }

    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<Turno>> obtenerTurnosPorPaciente(@PathVariable Long id) {
        List<Turno> turnos = iTurnoService.obtenerTurnosPorPaciente(id);
        return ResponseEntity.ok(turnos);
    }
}
