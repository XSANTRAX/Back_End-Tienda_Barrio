package com.tienda.inventario.service;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface ExcelService {
    <T> ByteArrayInputStream exportarDatos(List<T> listaDatos, String[] columnas, String nombreHoja, String[] camposAtributos);
}