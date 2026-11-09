package com.tienda.inventario.service;

import com.tienda.inventario.dto.LoginResponseDTO;
import com.tienda.inventario.dto.UsuarioDTO;
import java.util.List;

public interface UsuarioService {
    List<UsuarioDTO> obtenerTodos();
    List<UsuarioDTO> obtenerActivos();
    UsuarioDTO obtenerPorId(Long idUsuario);
    UsuarioDTO guardar(UsuarioDTO usuarioDTO);
    UsuarioDTO actualizar(Long idUsuario, UsuarioDTO usuarioDTO);
    void cambiarEstado(Long idUsuario, Boolean estado);
    LoginResponseDTO login(String nombreUsuario, String password);
}