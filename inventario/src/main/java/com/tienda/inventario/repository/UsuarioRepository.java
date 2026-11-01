package com.tienda.inventario.repository;

import com.tienda.inventario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Para validar login o buscar por usuario
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    List<Usuario> findByEstadoTrue(); // Para traer solo usuarios activos
}
