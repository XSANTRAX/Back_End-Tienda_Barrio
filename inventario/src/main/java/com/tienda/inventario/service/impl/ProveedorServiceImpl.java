package com.tienda.inventario.service.impl;

import com.tienda.inventario.dto.ProveedorDTO;
import com.tienda.inventario.entity.Proveedor;
import com.tienda.inventario.repository.ProveedorRepository;
import com.tienda.inventario.service.ProveedorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorServiceImpl(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    public List<ProveedorDTO> obtenerTodos() {
        return proveedorRepository.findAll().stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProveedorDTO obtenerPorId(Long idProveedor) {
        Proveedor proveedor = proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id: " + idProveedor));
        return mapearADTO(proveedor);
    }

    @Override
    public ProveedorDTO guardar(ProveedorDTO proveedorDTO) {
        Proveedor proveedor = mapearAEntidad(proveedorDTO);
        Proveedor proveedorGuardado = proveedorRepository.save(proveedor);
        return mapearADTO(proveedorGuardado);
    }

    @Override
    public ProveedorDTO actualizar(Long idProveedor, ProveedorDTO proveedorDTO) {
        Proveedor proveedorActual = proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id: " + idProveedor));

        proveedorActual.setNombre(proveedorDTO.getNombre());
        proveedorActual.setContacto(proveedorDTO.getContacto());
        proveedorActual.setTelefono(proveedorDTO.getTelefono());
        proveedorActual.setEmail(proveedorDTO.getEmail());

        Proveedor proveedorActualizado = proveedorRepository.save(proveedorActual);
        return mapearADTO(proveedorActualizado);
    }

    @Override
    public void eliminar(Long idProveedor) {
        proveedorRepository.deleteById(idProveedor);
    }

    private ProveedorDTO mapearADTO(Proveedor proveedor) {
        return new ProveedorDTO(
                proveedor.getIdProveedor(),
                proveedor.getNombre(),
                proveedor.getContacto(),
                proveedor.getTelefono(),
                proveedor.getEmail()
        );
    }

    private Proveedor mapearAEntidad(ProveedorDTO dto) {
        Proveedor proveedor = new Proveedor();
        proveedor.setIdProveedor(dto.getIdProveedor());
        proveedor.setNombre(dto.getNombre());
        proveedor.setContacto(dto.getContacto());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setEmail(dto.getEmail());
        return proveedor;
    }
}