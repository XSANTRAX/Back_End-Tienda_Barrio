package com.tienda.inventario.service;

import com.tienda.inventario.dto.MovimientoInventarioDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoInventarioService {
    List<MovimientoInventarioDTO> obtenerTodos();
    List<MovimientoInventarioDTO> obtenerPorInventario(Long idInventario);
    // Búsquedas por rango de fechas
    List<MovimientoInventarioDTO> obtenerPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<MovimientoInventarioDTO> obtenerPorInventarioYFechas(Long idInventario, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    MovimientoInventarioDTO obtenerPorId(Long idMovimiento);

    MovimientoInventarioDTO registrarMovimiento(MovimientoInventarioDTO movimientoDTO);
}