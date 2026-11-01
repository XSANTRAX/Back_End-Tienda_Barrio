package com.tienda.inventario.service.impl;

import com.tienda.inventario.dto.InventarioDTO;
import com.tienda.inventario.dto.MovimientoInventarioDTO;
import com.tienda.inventario.entity.Inventario;
import com.tienda.inventario.entity.Producto;
import com.tienda.inventario.repository.InventarioRepository;
import com.tienda.inventario.repository.ProductoRepository;
import com.tienda.inventario.service.InventarioService;
import com.tienda.inventario.service.MovimientoInventarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;
    private final MovimientoInventarioService movimientoService;

    public InventarioServiceImpl(InventarioRepository inventarioRepository,
            ProductoRepository productoRepository,
            MovimientoInventarioService movimientoService) {
        this.inventarioRepository = inventarioRepository;
        this.productoRepository = productoRepository;
        this.movimientoService = movimientoService;
    }

    @Override
    public List<InventarioDTO> obtenerTodos() {
        return inventarioRepository.findAll().stream().map(this::mapearADTO).collect(Collectors.toList());
    }

    @Override
    public InventarioDTO obtenerPorId(Long idInventario) {
        Inventario inv = inventarioRepository.findById(idInventario)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));
        return mapearADTO(inv);
    }

    @Override
    public InventarioDTO obtenerPorProducto(Long idProducto) {
        Inventario inv = inventarioRepository.findByProducto_IdProducto(idProducto)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));
        return mapearADTO(inv);
    }

    @Override
    @Transactional // Garantiza que si falla el movimiento, no se guarde el inventario
    public InventarioDTO guardar(InventarioDTO inventarioDTO) {
        Inventario inventario = mapearAEntidad(inventarioDTO);
        Inventario guardado = inventarioRepository.save(inventario);

        // Registrar movimientos automáticos si hay cantidades iniciales
        if (guardado.getCantidadBodega() > 0) {
            movimientoService.registrarMovimiento(new MovimientoInventarioDTO(null, guardado.getIdInventario(),
                    "ENTRADA", guardado.getCantidadBodega(), LocalDateTime.now(), "Apertura de stock en Bodega"));
        }
        if (guardado.getCantidadMostrador() > 0) {
            movimientoService.registrarMovimiento(new MovimientoInventarioDTO(null, guardado.getIdInventario(),
                    "ENTRADA", guardado.getCantidadMostrador(), LocalDateTime.now(), "Apertura de stock en Mostrador"));
        }

        return mapearADTO(guardado);
    }

    @Override
    @Transactional
    public InventarioDTO actualizar(Long idInventario, InventarioDTO dto) {
        Inventario inventarioActual = inventarioRepository.findById(idInventario)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));

        int Bodega = (dto.getCantidadBodega() != null ? dto.getCantidadBodega() : inventarioActual.getCantidadBodega())
                - inventarioActual.getCantidadBodega();
        int Mostrador = (dto.getCantidadMostrador() != null ? dto.getCantidadMostrador()
                : inventarioActual.getCantidadMostrador()) - inventarioActual.getCantidadMostrador();

        // Aplicamos los nuevos valores
        if (dto.getCantidadBodega() != null)
            inventarioActual.setCantidadBodega(dto.getCantidadBodega());
        if (dto.getCantidadMostrador() != null)
            inventarioActual.setCantidadMostrador(dto.getCantidadMostrador());

        Inventario inventarioGuardado = inventarioRepository.save(inventarioActual);

        // Si hay cambios en las cantidades, registramos movimientos automáticos
        if (Bodega != 0) {
            String tipo = Bodega > 0 ? "ENTRADA" : "SALIDA";
            movimientoService
                    .registrarMovimiento(new MovimientoInventarioDTO(null, inventarioGuardado.getIdInventario(), tipo,
                            Math.abs(Bodega), LocalDateTime.now(), "Ajuste de sistema en Bodega"));
        }

        if (Mostrador != 0) {
            String tipo = Mostrador > 0 ? "ENTRADA" : "SALIDA";
            movimientoService
                    .registrarMovimiento(new MovimientoInventarioDTO(null, inventarioGuardado.getIdInventario(), tipo,
                            Math.abs(Mostrador), LocalDateTime.now(), "Ajuste de sistema en Mostrador"));
        }

        return mapearADTO(inventarioGuardado);
    }

    @Override
    public void eliminar(Long idInventario) {
        inventarioRepository.deleteById(idInventario);
    }

    private InventarioDTO mapearADTO(Inventario inventario) {
        return new InventarioDTO(
                inventario.getIdInventario(),
                inventario.getCantidadBodega(),
                inventario.getCantidadMostrador(),
                inventario.getProducto() != null ? inventario.getProducto().getIdProducto() : null);
    }

    private Inventario mapearAEntidad(InventarioDTO dto) {
        Inventario inventario = new Inventario();
        inventario.setIdInventario(dto.getIdInventario());
        inventario.setCantidadBodega(dto.getCantidadBodega() != null ? dto.getCantidadBodega() : 0);
        inventario.setCantidadMostrador(dto.getCantidadMostrador() != null ? dto.getCantidadMostrador() : 0);

        if (dto.getIdProducto() != null) {
            Producto producto = productoRepository.findById(dto.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            inventario.setProducto(producto);
        }
        return inventario;
    }
}