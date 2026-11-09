package com.tienda.inventario.service.impl;

import com.tienda.inventario.dto.LoginResponseDTO;
import com.tienda.inventario.dto.UsuarioDTO;
import com.tienda.inventario.entity.Usuario;
import com.tienda.inventario.repository.UsuarioRepository;
import com.tienda.inventario.service.UsuarioService;
import com.tienda.inventario.util.PasswordUtil;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<UsuarioDTO> obtenerTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UsuarioDTO> obtenerActivos() {
        return usuarioRepository.findByEstadoTrue().stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioDTO obtenerPorId(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + idUsuario));
        return mapearADTO(usuario);
    }

    @Override
    public UsuarioDTO guardar(UsuarioDTO usuarioDTO) {
        Usuario usuario = mapearAEntidad(usuarioDTO);
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return mapearADTO(usuarioGuardado);
    }

    @Override
    public UsuarioDTO actualizar(Long idUsuario, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setNombreUsuario(usuarioDTO.getNombreUsuario());
        
        if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isBlank()) {
            usuario.setPassword(PasswordUtil.cifrar(usuarioDTO.getPassword()));
        }
        
        if (usuarioDTO.getEstado() != null) {
            usuario.setEstado(usuarioDTO.getEstado());
        }
        
        return mapearADTO(usuarioRepository.save(usuario));
    }

    @Override
    public void cambiarEstado(Long idUsuario, Boolean estado) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + idUsuario));
        usuario.setEstado(estado);
        usuarioRepository.save(usuario);
    }

   @Override
    public LoginResponseDTO login(String nombreUsuario, String password) {
        // 1. Buscamos al usuario
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas")); 
                
        // 2. Verificamos contraseña con PasswordUtil
        if (!PasswordUtil.verificar(password, usuario.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }
        
        // 3. Validamos que esté activo
        if (!usuario.getEstado()) {
            throw new RuntimeException("El usuario está inactivo en el sistema");
        }
        
        // 4. Generamos el token seguro
        String tokenSeguro = java.util.UUID.randomUUID().toString();
        
        // 5. Retornamos el DTO combinado con el token y el usuario mapeado
        return new LoginResponseDTO(tokenSeguro, mapearADTO(usuario));
    }


    private UsuarioDTO mapearADTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getIdUsuario(),
                usuario.getNombre(),         
                usuario.getNombreUsuario(),
                null, 
                usuario.getEstado()
        );
    }

    private Usuario mapearAEntidad(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(dto.getIdUsuario());
        usuario.setNombre(dto.getNombre());               
        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setPassword(PasswordUtil.cifrar(dto.getPassword()));
        usuario.setEstado(dto.getEstado() != null ? dto.getEstado() : true);
        return usuario;
    }
}