package com.tienda.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventarioDTO {
    private Long idInventario;
    private Integer cantidadBodega;
    private Integer cantidadMostrador;
    private Long idProducto;
}