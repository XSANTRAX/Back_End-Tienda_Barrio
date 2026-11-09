package com.tienda.inventario;

import com.tienda.inventario.entity.Usuario;
import com.tienda.inventario.repository.UsuarioRepository;
import com.tienda.inventario.util.PasswordUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventarioApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(UsuarioRepository usuarioRepository) {
		return args -> {
			// Solo lo crea si el usuario "admin" NO existe en la base de datos
			if (usuarioRepository.findByNombreUsuario("admin").isEmpty()) {
				Usuario admin = new Usuario();
				admin.setNombreUsuario("admin");
				admin.setNombre("Administrador");
				admin.setPassword(PasswordUtil.cifrar("123456"));
				admin.setEstado(true);
				usuarioRepository.save(admin);
				System.out.println("Usuario admin creado con éxito.");
			}
		};
	}
}