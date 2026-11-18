package com.tienda.inventario.service.impl;

import com.tienda.inventario.service.ExcelService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Override
    public <T> ByteArrayInputStream exportarDatos(List<T> listaDatos, String[] columnas, String nombreHoja, String[] camposAtributos) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(nombreHoja);

            // Estilo para la cabecera en negrita
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Crear cabeceras
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerCellStyle);
            }

            // Llenar datos con Reflexión de Java
            int rowIndex = 1;
            for (T item : listaDatos) {
                Row row = sheet.createRow(rowIndex++);
                for (int i = 0; i < camposAtributos.length; i++) {
                    Cell cell = row.createCell(i);
                    try {
                        Field field = item.getClass().getDeclaredField(camposAtributos[i]);
                        field.setAccessible(true);
                        Object valor = field.get(item);
                        cell.setCellValue(valor != null ? valor.toString() : "");
                    } catch (Exception e) {
                        cell.setCellValue("");
                    }
                }
            }

            // Autoajustar columnas
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Error al generar el archivo Excel: " + e.getMessage());
        }
    }
}
