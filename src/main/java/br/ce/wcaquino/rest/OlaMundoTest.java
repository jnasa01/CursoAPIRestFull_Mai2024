package br.ce.wcaquino.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.lang.Integer;

import org.junit.Test;

import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTest {
	@Test
	public void testOlaMundo() {
		Response response = request(Method.GET, "http://restapi.wcaquino.me:80/ola");
		assertEquals(response.getBody().asString(), "Ola Mundo!");
		assertTrue((response.getBody().asString().equals("Ola Mundo!")));
		assertTrue(response.getStatusCode() == 200);
		assertTrue("O Status Code deveria ser 200", response.getStatusCode() == 200);
		assertEquals(200, response.getStatusCode());
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
	}

	@Test
	public void devoConhecerOutrasFormas() {
//		Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
//		ValidatableResponse validacao = response.then();
//		validacao.statusCode(200);
//		
		// get("http://restapi.wcaquino.me/ola").then().statusCode(200);

		given()// Pré Condições
				.when()// Ação
				.get("http://restapi.wcaquino.me/ola").then()// Assertivas
//			.assertThat()
				.statusCode(200);
	}

	@Test
	public void devoConhecerMatchersHamcrest() {
//		assertThat("Maria", Matchers.is("Maria"));
//		assertThat(128, is(128));
//		assertThat(128, isA(Integer.class));
//		assertThat(128.1, isA(Double.class));
//		assertThat(128d, greaterThan(127d));
//		assertThat(128d, lessThan(130d)); 
		@SuppressWarnings("unused")
		List<Integer> impares = Arrays.asList(1,3,5,7,9);
//		assertThat(impares, hasSize(5));
//		assertThat(impares, contains(1,3,5,7,9));
//		assertThat(impares, containsInAnyOrder(1,3,5,9,7));
//		assertThat(impares, hasItem(1));
//		assertThat(impares, hasItems(1,5));
		
		assertThat("Maria", not("João"));
		assertThat("Joaquina", anyOf(is("Maria"), is("Joaquina")));
		assertThat("Joaquina", allOf(startsWith("Joa"), endsWith("ina"), containsString("qui")));
	}
	
	@Test
	public void devoValidarBody() {
		
		given()// Pré Condições
		.when()// Ação
			.get("http://restapi.wcaquino.me/ola")
		.then()// Assertivas
			.statusCode(200)
			.body(is("Ola Mundo!"))
			.body(containsString("Mundo"))
			.body(is(not(nullValue())))
			;
			
	}
	
}