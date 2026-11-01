package com.tienda.inventario.repository;

import com.tienda.inventario.entity.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    // Buscar por inventario
    List<MovimientoInventario> findByInventario_IdInventario(Long idInventario);

    // Buscar por fechas
    List<MovimientoInventario> findByFechaMovimientoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Buscar por inventario específico dentro de rango de fechas
    List<MovimientoInventario> findByInventario_IdInventarioAndFechaMovimientoBetween(
            Long idInventario, LocalDateTime fechaInicio, LocalDateTime fechaFin);
}