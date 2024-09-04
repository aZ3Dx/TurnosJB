package com.TurnosJB.TurnosJB.controller;

import com.TurnosJB.TurnosJB.entity.Odontologo;
import com.TurnosJB.TurnosJB.service.IOdontologoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/odontologos")
public class OdontologoController {

    @Autowired
    private IOdontologoService iOdontologoService;

    @GetMapping
    public ResponseEntity<List<Odontologo>> listar() {
        return ResponseEntity.ok(iOdontologoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Odontologo> consultarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(iOdontologoService.buscarPorId(id));
    }

    @PutMapping
    public ResponseEntity<Odontologo> actualizar(@RequestBody Odontologo odontologo) {
        return ResponseEntity.ok(iOdontologoService.actualizar(odontologo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        iOdontologoService.eliminar(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Odontologo> guardar(@RequestBody Odontologo odontologo) {
        return ResponseEntity.ok(iOdontologoService.guardar(odontologo));
    }
}
