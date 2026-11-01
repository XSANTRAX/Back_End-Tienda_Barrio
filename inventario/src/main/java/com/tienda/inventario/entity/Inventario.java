package com.tienda.inventario.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventario")
@Data
@NoArgsConstructor 
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario")
    private Long idInventario;

    @Column (name = "cantidad_bodega", nullable = false)
    private Integer cantidadBodega = 0;

    @Column(name = "cantidad_mostrador", nullable = false)
    private Integer cantidadMostrador = 0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_producto", nullable = false, unique = true)
    private Producto producto;

    public Inventario(Long idInventario, Integer cantidadBodega, Integer cantidadMostrador, Producto producto) {
        this.idInventario = idInventario;
        this.cantidadBodega = cantidadBodega;
        this.cantidadMostrador = cantidadMostrador;
        this.producto = producto;
    }
}
