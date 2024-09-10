package com.TurnosJB.TurnosJB.controller;

import com.TurnosJB.TurnosJB.entity.Odontologo;
import com.TurnosJB.TurnosJB.service.IOdontologoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/odontologos")
public class OdontologoController {

    private final IOdontologoService iOdontologoService;

    @Autowired
    public OdontologoController(IOdontologoService iOdontologoService) {
        this.iOdontologoService = iOdontologoService;
    }

    @GetMapping
    public ResponseEntity<List<Odontologo>> listar() {
        return ResponseEntity.status(HttpStatus.OK).body(iOdontologoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Odontologo> consultarPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(iOdontologoService.buscarPorId(id));
    }

    @PutMapping
    public ResponseEntity<Odontologo> actualizar(@RequestBody Odontologo odontologo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(iOdontologoService.actualizar(odontologo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        iOdontologoService.eliminar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<Odontologo> guardar(@RequestBody Odontologo odontologo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(iOdontologoService.guardar(odontologo));
    }
}
