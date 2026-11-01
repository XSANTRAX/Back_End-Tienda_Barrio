package com.tienda.inventario.service;

import com.tienda.inventario.dto.CategoriaDTO;
import java.util.List;

public interface CategoriaService {
    List<CategoriaDTO> obtenerTodas();
    CategoriaDTO obtenerPorId(Long idCategoria);
    CategoriaDTO guardar(CategoriaDTO categoriaDTO);
    CategoriaDTO actualizar(Long idCategoria, CategoriaDTO categoriaDTO);
    void eliminar(Long idCategoria);
}