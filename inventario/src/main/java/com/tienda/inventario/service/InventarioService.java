package com.tienda.inventario.service;

import com.tienda.inventario.dto.InventarioDTO;
import java.util.List;

public interface InventarioService {
    List<InventarioDTO> obtenerTodos();
    InventarioDTO obtenerPorId(Long idInventario);
    InventarioDTO obtenerPorProducto(Long idProducto);
    InventarioDTO guardar(InventarioDTO inventarioDTO);
    InventarioDTO actualizar(Long idInventario, InventarioDTO inventarioDTO);
    void eliminar(Long idInventario);
}