package com.tienda.inventario.repository;

import com.tienda.inventario.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Método para buscar productos por categoría
    List<Producto> findByCategoria_IdCategoria(Long idCategoria);
    // Método para buscar productos por proveedor
    List<Producto> findByProveedor_IdProveedor(Long idProveedor);
}