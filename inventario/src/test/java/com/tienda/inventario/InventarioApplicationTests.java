package com.tienda.inventario;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:inventario;DB_CLOSE_DELAY=-1")
class InventarioApplicationTests {

	@Test
	void contextLoads() {
	}

}
