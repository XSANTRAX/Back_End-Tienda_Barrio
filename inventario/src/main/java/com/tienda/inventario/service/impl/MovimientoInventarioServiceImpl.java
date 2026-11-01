package com.tienda.inventario.service.impl;

import com.tienda.inventario.dto.MovimientoInventarioDTO;
import com.tienda.inventario.entity.Inventario;
import com.tienda.inventario.entity.MovimientoInventario;
import com.tienda.inventario.repository.InventarioRepository;
import com.tienda.inventario.repository.MovimientoInventarioRepository;
import com.tienda.inventario.service.MovimientoInventarioService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovimientoInventarioServiceImpl implements MovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoRepository;
    private final InventarioRepository inventarioRepository;

    public MovimientoInventarioServiceImpl(MovimientoInventarioRepository movimientoRepository, InventarioRepository inventarioRepository) {
        this.movimientoRepository = movimientoRepository;
        this.inventarioRepository = inventarioRepository;
    }

    @Override
    public List<MovimientoInventarioDTO> obtenerTodos() {
        return movimientoRepository.findAll().stream().map(this::mapearADTO).collect(Collectors.toList());
    }

    @Override
    public List<MovimientoInventarioDTO> obtenerPorInventario(Long idInventario) {
        return movimientoRepository.findByInventario_IdInventario(idInventario).stream()
                .map(this::mapearADTO).collect(Collectors.toList());
    }

    @Override
    public List<MovimientoInventarioDTO> obtenerPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return movimientoRepository.findByFechaMovimientoBetween(fechaInicio, fechaFin).stream()
                .map(this::mapearADTO).collect(Collectors.toList());
    }

    @Override
    public List<MovimientoInventarioDTO> obtenerPorInventarioYFechas(Long idInventario, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return movimientoRepository.findByInventario_IdInventarioAndFechaMovimientoBetween(idInventario, fechaInicio, fechaFin).stream()
                .map(this::mapearADTO).collect(Collectors.toList());
    }

    @Override
    public MovimientoInventarioDTO obtenerPorId(Long idMovimiento) {
        MovimientoInventario movimiento = movimientoRepository.findById(idMovimiento)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));
        return mapearADTO(movimiento);
    }

    @Override
    public MovimientoInventarioDTO registrarMovimiento(MovimientoInventarioDTO dto) {
        MovimientoInventario movimiento = mapearAEntidad(dto);
        if (movimiento.getFechaMovimiento() == null) {
            movimiento.setFechaMovimiento(LocalDateTime.now());
        }
        return mapearADTO(movimientoRepository.save(movimiento));
    }

    private MovimientoInventarioDTO mapearADTO(MovimientoInventario mov) {
        return new MovimientoInventarioDTO(
                mov.getIdMovimiento(),
                mov.getInventario() != null ? mov.getInventario().getIdInventario() : null,
                mov.getTipoMovimiento(),
                mov.getCantidad(),
                mov.getFechaMovimiento(),
                mov.getDescripcion()
        );
    }

    private MovimientoInventario mapearAEntidad(MovimientoInventarioDTO dto) {
        MovimientoInventario mov = new MovimientoInventario();
        mov.setIdMovimiento(dto.getIdMovimiento());
        mov.setTipoMovimiento(dto.getTipoMovimiento());
        mov.setCantidad(dto.getCantidad());
        mov.setFechaMovimiento(dto.getFechaMovimiento());
        mov.setDescripcion(dto.getDescripcion());
        
        if (dto.getIdInventario() != null) {
            Inventario inv = inventarioRepository.findById(dto.getIdInventario())
                    .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));
            mov.setInventario(inv);
        }
        return mov;
    }
}