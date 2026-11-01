package com.tienda.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoInventarioDTO {
    private Long idMovimiento;
    private Long idInventario;
    private String tipoMovimiento;
    private Integer cantidad;
    private LocalDateTime fechaMovimiento;
    private String descripcion;
}