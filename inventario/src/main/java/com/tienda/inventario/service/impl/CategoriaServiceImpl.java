package com.tienda.inventario.service.impl;

import com.tienda.inventario.dto.CategoriaDTO;
import com.tienda.inventario.entity.Categoria;
import com.tienda.inventario.repository.CategoriaRepository;
import com.tienda.inventario.service.CategoriaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<CategoriaDTO> obtenerTodas() {
        return categoriaRepository.findAll().stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoriaDTO obtenerPorId(Long idCategoria) {
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + idCategoria));
        return mapearADTO(categoria);
    }

    @Override
    public CategoriaDTO guardar(CategoriaDTO categoriaDTO) {
        Categoria categoria = mapearAEntidad(categoriaDTO);
        Categoria categoriaGuardada = categoriaRepository.save(categoria);
        return mapearADTO(categoriaGuardada);
    }

    @Override
    public CategoriaDTO actualizar(Long idCategoria, CategoriaDTO categoriaDTO) {
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + idCategoria));
        
        // Actualizamos los datos
        categoria.setNombreCategoria(categoriaDTO.getNombreCategoria());
        categoria.setDescripcion(categoriaDTO.getDescripcion());
        
        Categoria categoriaActualizada = categoriaRepository.save(categoria);
        return mapearADTO(categoriaActualizada);
    }

    @Override
    public void eliminar(Long idCategoria) {
        categoriaRepository.deleteById(idCategoria);
    }

    private CategoriaDTO mapearADTO(Categoria categoria) {
        return new CategoriaDTO(
                categoria.getIdCategoria(),
                categoria.getNombreCategoria(),
                categoria.getDescripcion()
        );
    }

    private Categoria mapearAEntidad(CategoriaDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(dto.getIdCategoria());
        categoria.setNombreCategoria(dto.getNombreCategoria());
        categoria.setDescripcion(dto.getDescripcion());
        return categoria;
    }
}