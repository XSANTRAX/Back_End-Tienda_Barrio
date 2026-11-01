package com.tienda.inventario.service.impl;

import com.tienda.inventario.dto.ProductoDTO;
import com.tienda.inventario.entity.Categoria;
import com.tienda.inventario.entity.Producto;
import com.tienda.inventario.entity.Proveedor;
import com.tienda.inventario.repository.CategoriaRepository;
import com.tienda.inventario.repository.ProductoRepository;
import com.tienda.inventario.repository.ProveedorRepository;
import com.tienda.inventario.service.ProductoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProveedorRepository proveedorRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository,
            ProveedorRepository proveedorRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    public List<ProductoDTO> obtenerTodos() {
        return productoRepository.findAll().stream().map(this::mapearADTO).collect(Collectors.toList());
    }

    @Override
    public List<ProductoDTO> obtenerPorCategoria(Long idCategoria) {
        return productoRepository.findByCategoria_IdCategoria(idCategoria).stream()
                .map(this::mapearADTO).collect(Collectors.toList());
    }

    @Override
    public List<ProductoDTO> obtenerPorProveedor(Long idProveedor) {
        return productoRepository.findByProveedor_IdProveedor(idProveedor).stream()
                .map(this::mapearADTO).collect(Collectors.toList());
    }

    @Override
    public ProductoDTO obtenerPorId(Long idProducto) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + idProducto));
        return mapearADTO(producto);
    }

    @Override
    public ProductoDTO guardar(ProductoDTO productoDTO) {
        Producto producto = mapearAEntidad(productoDTO);
        Producto productoGuardado = productoRepository.save(producto);
        return mapearADTO(productoGuardado);
    }

    @Override
    public ProductoDTO actualizar(Long idProducto, ProductoDTO productoDTO) {
        Producto productoActual = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + idProducto));

        productoActual.setNombreProducto(productoDTO.getNombreProducto());
        productoActual.setPrecio(productoDTO.getPrecio());
        productoActual.setStockMinimo(productoDTO.getStockMinimo());

        if (productoDTO.getIdCategoria() != null &&
                !productoDTO.getIdCategoria().equals(productoActual.getCategoria().getIdCategoria())) {
            Categoria categoria = categoriaRepository.findById(productoDTO.getIdCategoria())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            productoActual.setCategoria(categoria);
        }

        if (productoDTO.getIdProveedor() != null &&
                !productoDTO.getIdProveedor().equals(productoActual.getProveedor().getIdProveedor())) {
            Proveedor proveedor = proveedorRepository.findById(productoDTO.getIdProveedor())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
            productoActual.setProveedor(proveedor);
        }

        Producto productoActualizado = productoRepository.save(productoActual);
        return mapearADTO(productoActualizado);
    }

    @Override
    public void eliminar(Long idProducto) {
        productoRepository.deleteById(idProducto);
    }

    private ProductoDTO mapearADTO(Producto producto) {
        return new ProductoDTO(
                producto.getIdProducto(),
                producto.getNombreProducto(),
                producto.getPrecio(),
                producto.getStockMinimo(),
                producto.getCategoria() != null ? producto.getCategoria().getIdCategoria() : null,
                producto.getProveedor() != null ? producto.getProveedor().getIdProveedor() : null);
    }

    private Producto mapearAEntidad(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setIdProducto(dto.getIdProducto());
        producto.setNombreProducto(dto.getNombreProducto());
        producto.setPrecio(dto.getPrecio());
        producto.setStockMinimo(dto.getStockMinimo());

        if (dto.getIdCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + dto.getIdCategoria()));
            producto.setCategoria(categoria);
        }

        if (dto.getIdProveedor() != null) {
            Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id: " + dto.getIdProveedor()));
            producto.setProveedor(proveedor);
        }

        return producto;
    }
}