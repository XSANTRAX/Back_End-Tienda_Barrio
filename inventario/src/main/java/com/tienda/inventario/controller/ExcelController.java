package com.tienda.inventario.controller;

import com.tienda.inventario.repository.CategoriaRepository;
import com.tienda.inventario.repository.ProductoRepository;
import com.tienda.inventario.repository.InventarioRepository;
import com.tienda.inventario.repository.ProveedorRepository;
import com.tienda.inventario.repository.MovimientoInventarioRepository;
import com.tienda.inventario.service.ExcelService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
@CrossOrigin(origins = "*")
public class ExcelController {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final InventarioRepository inventarioRepository;
    private final ExcelService excelService;

    // Constructor con todos los repositorios inyectados correctamente
    ExcelController(
            CategoriaRepository categoriaRepository,
            ProductoRepository productoRepository,
            ProveedorRepository proveedorRepository,
            MovimientoInventarioRepository movimientoInventarioRepository,
            InventarioRepository inventarioRepository,
            ExcelService excelService) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
        this.proveedorRepository = proveedorRepository;
        this.movimientoInventarioRepository = movimientoInventarioRepository;
        this.inventarioRepository = inventarioRepository; 
        this.excelService = excelService;
    }

    // --- 1. ENDPOINT PARA DESCARGAR CATEGORÍAS ---
    @GetMapping("/categorias")
    public ResponseEntity<InputStreamResource> descargarCategorias() {
        List<?> categorias = categoriaRepository.findAll();
        String[] columnas = { "ID", "Nombre de Categoría", "Descripción" };
        String[] campos = { "idCategoria", "nombreCategoria", "descripcion" };

        ByteArrayInputStream stream = excelService.exportarDatos(categorias, columnas, "Categorias", campos);
        return construirRespuesta(stream, "reporte_categorias.xlsx");
    }

    // --- 2. ENDPOINT PARA DESCARGAR INVENTARIO COMPLETO ---
    @GetMapping("/inventario-completo")
    public ResponseEntity<InputStreamResource> descargarInventarioCompleto() {
        List<?> inventarios = inventarioRepository.findAll();

        String[] columnas = { "Código", "Producto", "Categoría", "Proveedor", "Stock Bodega", "Stock Mostrador",
                "Precio Unitario" };
        String[] campos = {
                "idInventario",
                "producto.nombreProducto",
                "producto.categoria.nombreCategoria",
                "producto.proveedor.nombre",
                "cantidadBodega",
                "cantidadMostrador",
                "producto.precio"
        };

        ByteArrayInputStream stream = excelService.exportarDatos(inventarios, columnas, "Inventario General", campos);
        return construirRespuesta(stream, "reporte_inventario_completo.xlsx");
    }

    // --- 3. ENDPOINT PARA DESCARGAR PROVEEDORES ---
    @GetMapping("/proveedores")
    public ResponseEntity<InputStreamResource> descargarProveedores() {
        List<?> proveedores = proveedorRepository.findAll();
        String[] columnas = { "ID", "Nombre", "Contacto", "Teléfono", "Correo" };
        String[] campos = { "idProveedor", "nombre", "contacto", "telefono", "email" };

        ByteArrayInputStream stream = excelService.exportarDatos(proveedores, columnas, "Proveedores", campos);
        return construirRespuesta(stream, "reporte_proveedores.xlsx");
    }

    // --- 4. ENDPOINT PARA DESCARGAR MOVIMIENTOS ---
    @GetMapping("/movimientos")
    public ResponseEntity<InputStreamResource> descargarMovimientos() {
        List<?> movimientos = movimientoInventarioRepository.findAll();
        String[] columnas = { "ID", "Tipo", "Cantidad", "Descripción", "Fecha" };
        String[] campos = { "idMovimiento", "tipoMovimiento", "cantidad", "descripcion", "fechaMovimiento" };

        ByteArrayInputStream stream = excelService.exportarDatos(movimientos, columnas, "Movimientos", campos);
        return construirRespuesta(stream, "reporte_movimientos.xlsx");
    }

    private ResponseEntity<InputStreamResource> construirRespuesta(ByteArrayInputStream stream, String nombreArchivo) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + nombreArchivo);

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(
                        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(stream));
    }
}