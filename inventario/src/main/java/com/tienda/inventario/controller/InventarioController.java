package com.tienda.inventario.controller;

import com.tienda.inventario.dto.InventarioDTO;
import com.tienda.inventario.service.InventarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventarios")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public ResponseEntity<List<InventarioDTO>> obtenerTodos() {
        return ResponseEntity.ok(inventarioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventarioDTO> obtenerPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(inventarioService.obtenerPorId(id));
    }

    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<InventarioDTO> obtenerPorProducto(@PathVariable("idProducto") Long idProducto) {
        return ResponseEntity.ok(inventarioService.obtenerPorProducto(idProducto));
    }

    @PostMapping
    public ResponseEntity<InventarioDTO> guardar(@RequestBody InventarioDTO inventarioDTO) {
        return new ResponseEntity<>(inventarioService.guardar(inventarioDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventarioDTO> actualizar(@PathVariable("id") Long id, @RequestBody InventarioDTO inventarioDTO) {
        return ResponseEntity.ok(inventarioService.actualizar(id, inventarioDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        inventarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}