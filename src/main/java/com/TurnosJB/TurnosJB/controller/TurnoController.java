package com.TurnosJB.TurnosJB.controller;

import com.TurnosJB.TurnosJB.entity.Turno;
import com.TurnosJB.TurnosJB.service.ITurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/turnos")
public class TurnoController {

    private final ITurnoService iTurnoService;

    @Autowired
    public TurnoController(ITurnoService iTurnoService) {
        this.iTurnoService = iTurnoService;
    }

    @GetMapping
    public ResponseEntity<List<Turno>> listar() {
        return ResponseEntity.status(HttpStatus.OK).body(iTurnoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Turno> consultarPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(iTurnoService.buscarPorId(id));
    }

    @PutMapping
    public ResponseEntity<Turno> actualizar(@RequestBody Turno turno) {
        return ResponseEntity.status(HttpStatus.CREATED).body(iTurnoService.actualizar(turno));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        iTurnoService.eliminar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<Turno> guardar(@RequestBody Turno turno) {
        return ResponseEntity.status(HttpStatus.CREATED).body(iTurnoService.guardar(turno));
    }

    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<Turno>> obtenerTurnosPorPaciente(@PathVariable Long id) {
        List<Turno> turnos = iTurnoService.obtenerTurnosPorPaciente(id);
        return ResponseEntity.status(HttpStatus.OK).body(turnos);
    }
}
