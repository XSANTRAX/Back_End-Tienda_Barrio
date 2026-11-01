package com.tienda.inventario.controller;

import com.tienda.inventario.dto.MovimientoInventarioDTO;
import com.tienda.inventario.service.MovimientoInventarioService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoInventarioController {

    private final MovimientoInventarioService movimientoService;

    public MovimientoInventarioController(MovimientoInventarioService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @GetMapping
    public ResponseEntity<List<MovimientoInventarioDTO>> obtenerTodos() {
        return ResponseEntity.ok(movimientoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoInventarioDTO> obtenerPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(movimientoService.obtenerPorId(id));
    }

    @GetMapping("/inventario/{idInventario}")
    public ResponseEntity<List<MovimientoInventarioDTO>> obtenerPorInventario(@PathVariable("idInventario") Long idInventario) {
        return ResponseEntity.ok(movimientoService.obtenerPorInventario(idInventario));
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<MovimientoInventarioDTO>> obtenerPorFechas(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(movimientoService.obtenerPorFechas(inicio, fin));
    }
}