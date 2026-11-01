package com.tienda.inventario.service;

import com.tienda.inventario.dto.ProductoDTO;
import java.util.List;

public interface ProductoService {
    List<ProductoDTO> obtenerTodos();
    List<ProductoDTO> obtenerPorCategoria(Long idCategoria);
    List<ProductoDTO> obtenerPorProveedor(Long idProveedor);
    ProductoDTO obtenerPorId(Long idProducto);
    ProductoDTO guardar(ProductoDTO productoDTO);
    ProductoDTO actualizar(Long idProducto, ProductoDTO productoDTO);
    void eliminar(Long idProducto);
}