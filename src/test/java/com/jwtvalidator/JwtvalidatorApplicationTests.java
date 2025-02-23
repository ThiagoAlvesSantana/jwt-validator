package com.jwtvalidator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.jwtvalidator.service.JwtValidatorService;

@SpringBootTest
class JwtvalidatorApplicationTests {

	private final JwtValidatorService service = new JwtValidatorService();

	@Test
	public void testCaso1() {
		String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";
		boolean resultado = service.validate(jwt);
		assertTrue(resultado, "Caso 1: JWT deveria ser válido");
	}

	@Test
	public void testCaso2() {
		String jwt = "eyJhbGciOiJzI1NiJ9.dfsdfsfryJSr2xrIjoiQWRtaW4iLCJTZrkIjoiNzg0MSIsIk5hbrUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05fsdfsIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";
		boolean resultado = service.validate(jwt);
		assertFalse(resultado, "Caso 2: JWT deveria ser inválido");
	}

	@Test
	public void testCaso3() {
		String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiODgwMzciLCJOYW1lIjoiTTRyaWEgT2xpdmlhIn0.6YD73XWZYQSSMDf6H0i3-kylz1-TY_Yt6h1cV2Ku-Qs";
		boolean resultado = service.validate(jwt);
		assertFalse(resultado, "Caso 3: JWT deveria ser inválido, pois a claim Name contém dígitos");
	}

	@Test
	public void testCaso4() {
		String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiTWVtYmVyIiwiT3JnIjoiQlIiLCJTZWVkIjoiMTQ2MjciLCJOYW1lIjoiVmFsZGlyIEFyYW5oYSJ9.cmrXV_Flm5mfdpfNUVopY_I2zeJUy4EZ4i3Fea98zvY";
		boolean resultado = service.validate(jwt);
		assertFalse(resultado, "Caso 4: JWT deveria ser inválido, pois contém claims extras");
	}
}
