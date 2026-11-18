package com.tienda.inventario.controller;

import com.tienda.inventario.repository.CategoriaRepository;
import com.tienda.inventario.repository.ProductoRepository;
import com.tienda.inventario.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
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
@CrossOrigin(origins = "*") // Permite que tu frontend web se conecte sin problemas de CORS
public class ExcelController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ExcelService excelService; // Inyectamos la interfaz que acabamos de crear

    // --- 1. ENDPOINT PARA DESCARGAR CATEGORÍAS ---
    @GetMapping("/categorias")
    public ResponseEntity<InputStreamResource> descargarCategorias() {
        List<?> categorias = categoriaRepository.findAll();
        String[] columnas = {"ID", "Nombre de Categoría", "Descripción"};
        String[] campos = {"idCategoria", "nombreCategoria", "descripcion"}; // Atributos exactos en tu entidad Categoria

        ByteArrayInputStream stream = excelService.exportarDatos(categorias, columnas, "Categorias", campos);
        return construirRespuesta(stream, "reporte_categorias.xlsx");
    }

    // --- 2. ENDPOINT PARA DESCARGAR PRODUCTOS ---
    @GetMapping("/productos")
    public ResponseEntity<InputStreamResource> descargarProductos() {
        List<?> productos = productoRepository.findAll();
        String[] columnas = {"ID", "Nombre Producto", "Precio", "Stock"};
        String[] campos = {"idProducto", "nombreProducto", "precio", "stock"}; // Atributos exactos en tu entidad Producto

        ByteArrayInputStream stream = excelService.exportarDatos(productos, columnas, "Productos", campos);
        return construirRespuesta(stream, "reporte_productos.xlsx");
    }

    // Método auxiliar privado para empaquetar el flujo de datos como un archivo descargable
    private ResponseEntity<InputStreamResource> construirRespuesta(ByteArrayInputStream stream, String nombreArchivo) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + nombreArchivo);

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(stream));
    }
}