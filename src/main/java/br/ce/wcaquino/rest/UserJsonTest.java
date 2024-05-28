package br.ce.wcaquino.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.lang.Integer;


import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UserJsonTest {

	@Test
	public void deveVerificarPrimeiroNivel() {
		given()
		.when()
			.get("https://restapi.wcaquino.me/users/1")
		.then()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("João da Silva"))
			.body(containsString("Silva"))
			.body("age", is(30))
			.body("age", greaterThan(25))
			.body("age", Matchers.lessThan(32))
			.body("salary", is(1234.5678F))

		;
	}


	@SuppressWarnings("removal")
	@Test
	public void deveVerificarPrimeiroNivelOutrasFormas() {
		Response response = RestAssured.request(Method.GET, ("https://restapi.wcaquino.me/users/1"));

		// path
		// response.path("id")
		// System.out.println(response.path("id"));
		Assert.assertEquals(new Integer(1), response.path("id"));
		Assert.assertEquals(new Integer(1), response.path("%s", "id"));

		// jsonpath
		JsonPath jpath = new JsonPath(response.asString());
		Assert.assertEquals(1, jpath.getInt("id"));

		// from
		int id = JsonPath.from(response.asString()).getInt("id");
		Assert.assertEquals(1, id);
	}
	
	@Test
	public void deveVerificarSegundoNivel() {
		
		given()
		.when()
			.get("https://restapi.wcaquino.me/users/2")
		.then()
			.statusCode(200)
			.body(containsString("Joaquina"))
			.body("name", is("Maria Joaquina"))
			.body("endereco.rua", is("Rua dos bobos"))
			.body("endereco.rua", containsString("bobos"))
			;
		
	}
	
	@Test
	public void deveVerificarLista() {
		given()
		.when()
			.get("https://restapi.wcaquino.me/users/3")
		.then()
			.statusCode(200)
			.body(containsString("Ana"))
			.body("filhos", hasSize(2))
			.body("filhos[0].name", containsString("Zezinho"))
			.body("filhos[1].name", containsString("Luizinho"))
			.body("filhos.name", hasItem("Luizinho"))
			.body("filhos.name", hasItem("Zezinho"))
			.body("filhos.name", hasItems("Zezinho", "Luizinho"))
			
			;
		
	}
	
	@Test
	public void deveRetornarErrorUsuarioInexistente() {
		
		given()
		.when()
			.get("https://restapi.wcaquino.me/users/4")
		.then()
			.statusCode(404)
			.body(containsString("error"))
			.body("error", is("Usuário inexistente"))
			
			;
	}
	
	@Test
	public void deveVerificarListaRaiz() {
		
		given()
		.when()
			.get("https://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.body("$", hasSize(3)) //$ é somente uma convenção
			.body("", hasSize(3)) // também pode deixar em branco
			.body("name", hasItems("João da Silva", "Maria Joaquina", "Ana Júlia"))
			.body("age[1]", is(25))
			.body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho")))
			.body("salary", contains(1234.5678f, 2500, null))
		
			;
	}
	
	@Test
	public void devoFazerVerificacoesAvancadas() {
		
		given()
		.when()
			.get("https://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.body("$", hasSize(3)) // também pode deixar em branco
			.body("age.findAll{it <= 25}.size()", is(2)) // criado um objeto {} com it(metodo do groove) que armazena os valores retornados pelas clausula e compara nos asserts
			.body("age.findAll{it <= 25 && it > 20}.size()", is(1))
			.body("findAll{it.age <= 25 && it.age > 20}.name", hasItem("Maria Joaquina"))
			.body("findAll{it.age <= 25 }[0].name", is("Maria Joaquina"))
			.body("findAll{it.age <= 25 }[-1].name", is("Ana Júlia"))
			.body("find{it.age <= 25 }.name", is("Maria Joaquina"))
			.body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina", "Ana Júlia"))
			.body("findAll{it.name.length() > 10}.name", hasItems("Maria Joaquina", "João da Silva"))
			.body("name.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()", allOf(arrayContaining("MARIA JOAQUINA")))
			//.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()", allOf(arrayContaining("MARIA JOAQUINA"), arrayWithSize(1)))
			.body("age.collect{it * 2}", hasItems(50, 60, 40))
			.body("id.max()", is(3))
			.body("salary.min()", is(1234.5678f))
			.body("salary.findAll{it != null}.sum()", is(closeTo(3734.5678f, 0.001)))
			.body("salary.findAll{it != null}.sum()", allOf(greaterThan(3000d), lessThan(5000d)))
			;
			
	}

	
	@Test
	public void deveUnirJsonPathComJAVA() {
		ArrayList<String> names = 
			given()
			.when()
				.get("https://restapi.wcaquino.me/users")
			.then()
				.statusCode(200)
				.extract().path("name.findAll{it.startsWith('Maria')}")	
				;
		assertEquals(1, names.size());
		assertTrue(names.get(0).equalsIgnoreCase("MaRia joaquina"));
		assertEquals(names.get(0).toUpperCase(), "MARIA JOAQUINA");
		assertEquals(names.get(0).toUpperCase(), "maria joaquina".toUpperCase());
		
	}

}
