package com.tienda.inventario.repository;

import com.tienda.inventario.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    // Método para buscar el inventario de un producto por su ID
    Optional<Inventario> findByProducto_IdProducto(Long idProducto);
}