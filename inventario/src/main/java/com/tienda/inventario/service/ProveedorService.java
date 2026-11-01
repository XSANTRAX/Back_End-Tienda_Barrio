package com.tienda.inventario.service;

import com.tienda.inventario.dto.ProveedorDTO;
import java.util.List;

public interface ProveedorService {
    List<ProveedorDTO> obtenerTodos();
    ProveedorDTO obtenerPorId(Long idProveedor);
    ProveedorDTO guardar(ProveedorDTO proveedorDTO);
    ProveedorDTO actualizar(Long idProveedor, ProveedorDTO proveedorDTO);
    void eliminar(Long idProveedor);
}