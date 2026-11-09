package com.tienda.inventario.controller;

import com.tienda.inventario.dto.LoginResponseDTO;
import com.tienda.inventario.dto.UsuarioDTO;
import com.tienda.inventario.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> obtenerTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<UsuarioDTO>> obtenerActivos() {
        return ResponseEntity.ok(usuarioService.obtenerActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> guardar(@RequestBody UsuarioDTO usuarioDTO) {
        return new ResponseEntity<>(usuarioService.guardar(usuarioDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizar(@PathVariable("id") Long id, @RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.actualizar(id, usuarioDTO));
    }

    @PatchMapping("/{id}/estado") // Usamos PATCH porque solo modificamos un campo específico (estado)
    public ResponseEntity<Void> cambiarEstado(@PathVariable("id") Long id, @RequestParam Boolean estado) {
        usuarioService.cambiarEstado(id, estado);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        try {
            String user = credenciales.get("nombreUsuario");
            String pass = credenciales.get("password");
            
            LoginResponseDTO respuestaLogin = usuarioService.login(user, pass);
            
            return ResponseEntity.ok(respuestaLogin);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}