package com.TurnosJB.TurnosJB.controller;

import com.TurnosJB.TurnosJB.entity.Odontologo;
import com.TurnosJB.TurnosJB.service.IOdontologoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
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
        log.info("Listando odontólogos.");
        return ResponseEntity.status(HttpStatus.OK).body(iOdontologoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Odontologo> consultarPorId(@PathVariable Long id) {
        log.info("Consultando odontólogos por id.");
        return ResponseEntity.status(HttpStatus.OK).body(iOdontologoService.buscarPorId(id));
    }

    @PutMapping
    public ResponseEntity<Odontologo> actualizar(@RequestBody Odontologo odontologo) {
        log.info("Actualizando odontólogo.");
        return ResponseEntity.status(HttpStatus.CREATED).body(iOdontologoService.actualizar(odontologo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Eliminando odontólogo.");
        iOdontologoService.eliminar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<Odontologo> guardar(@RequestBody Odontologo odontologo) {
        log.info("Guardando odontólogo.");
        return ResponseEntity.status(HttpStatus.CREATED).body(iOdontologoService.guardar(odontologo));
    }
}
