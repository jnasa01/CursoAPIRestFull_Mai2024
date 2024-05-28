package br.ce.wcaquino.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import static org.junit.Assert.*;


import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.http.ContentType;

public class VerbosHTTP {

	@Test
	public void deveSalvarUsuario() {

		given()
			.log().all().contentType("application/json")
			.body("{\"name\":\"Jenocliudes Silva\", \"age\":\"33\", \"salary\":\"1999\"}")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all().statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Jenocliudes Silva"))
			.body("age", is("33"))
			.body("salary", is("1999"))

		;
	}

	@Test
	public void naoDeveSalvarUsuarioSemNome() {

		given()
			.log().all().contentType("application/json")
			.body("{\"age\":\"33\", \"salary\":\"1999\"}")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then().log().all().statusCode(400)
			.body("id", is(nullValue()))
			.body("error", is("Name é um atributo obrigatório"))

		;

	}

	@Test
	public void deveSalvarUsuarioViaXML() {

		given()
			.log().all().contentType(ContentType.XML)
			.body("<user><name>Jailton</name><age>18</age></user>")
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all().statusCode(201)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("Jailton"))
			.body("user.age", is("18"))

		;
	}

	@Test
	public void devoAlterarUsuario() {

		given()
			.log().all().contentType("application/json")
			.body("{\"name\":\"Alter User\", \"age\":\"60\"}")
		.when()
			.put("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all().statusCode(200)
			.body("id", is(1))
			.body("name", is("Alter User"))
			.body("age", is("60"))
			.body("salary", is(1234.5678f))

		;
	}

	@Test
	public void devoCustomizarURLParte1() {

		given()
			.log().all().contentType("application/json")
			.body("{\"name\":\"Alter User\", \"age\":\"60\"}")
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{userId}", "users", "1")
		.then()
			.log().all().statusCode(200)
			.body("id", is(1))
			.body("name", is("Alter User"))
			.body("age", is("60"))
			.body("salary", is(1234.5678f))

		;
	}

	@Test
	public void devoCustomizarURLParte2() {

		given()
			.log().all().contentType("application/json")
			.body("{\"name\":\"Alter User\", \"age\":\"60\"}")
			.pathParam("entidade", "users").pathParam("userId", 1)
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{userId}")
		.then().log().all().statusCode(200)
			.body("id", is(1))
			.body("name", is("Alter User"))
			.body("age", is("60"))
			.body("salary", is(1234.5678f))

		;
	}

	@Test
	public void deveRemoverUsuario() {
		given()
			.log().all().contentType("application/json")
			.pathParam("entidade", "users")
			.pathParam("userId", 1)
		.when()
			.delete("https://restapi.wcaquino.me/{entidade}/{userId}")
		.then()
			.log().all().statusCode(204)

		;
	}

	@Test
	public void naoDeveRemoverUsuarioInexistente() {
		given().
			log().all().contentType("application/json")
			.pathParam("entidade", "users")
			.pathParam("userId", 1000)
		.when()
			.delete("https://restapi.wcaquino.me/{entidade}/{userId}")
		.then()
			.log().all().statusCode(400)
			.body("error", is("Registro inexistente"))

		;
	}
	
	@Test
	public void deveSalvarUsuarioUsandoMAP() {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "Usuario Via MAP");
		params.put("age", 45);
		params.put("salary", "3000");

		given()
			.log().all().contentType("application/json")
			.body(params)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all().statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Usuario Via MAP"))
			.body("age", is(45))
			.body("salary", is("3000"))

		;
	}
	
	@Test
	public void deveSalvarUsuarioUsandoObjeto() {
		User user = new User("Usuario via objeto", 35, 2400);
		
		given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Usuario via objeto"))
			.body("age", is(35))

		;
	}
	
	
	@Test
	public void deveDeserealizarObjetoAoSalvarUsuario() {
		
		User user = new User("Usuario deserializado", 42, 5423);
		
		User usuarioInserido = given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class);

		;
		
		System.out.println(usuarioInserido);
		Assert.assertThat(usuarioInserido.getId(), notNullValue());
		assertEquals("Usuario deserializado", usuarioInserido.getName());
		assertThat(usuarioInserido.getAge(), is(42));
		assertThat(usuarioInserido.getSalary(), is(5423d));
		
	}
	
	
	@Test
	public void deveSalvarUsuarioViaXMLUsandoObjeto() {
		
		User user = new User("Usuario XML", 45, 1000);

		given()
			.log().all().contentType(ContentType.XML)
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all().statusCode(201)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("Usuario XML"))
			.body("user.age", is("45"))
			.body("user.salary", is("1000.0"))

		;
	}
	
	@Test
	public void deveDeserializarXMLAoSalvarUsuario() {
		
		User user = new User("Usuario XML", 50, 1000);

		User UsuarioXML = given()
			.log().all().contentType(ContentType.XML)
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all().statusCode(201)
			.extract().body().as(User.class);

//			.body("user.@id", is(notNullValue()))
//			.body("user.name", is("Usuario XML"))
//			.body("user.age", is("45"))
//			.body("user.salary", is("1000.0"))

		;
		
		assertThat(UsuarioXML.getId(), notNullValue());
		assertThat(UsuarioXML.getName(), is("Usuario XML"));
		assertThat(UsuarioXML.getAge(), is(50));
		assertThat(UsuarioXML.getSalary(), is(1000d));
		
		;
	}


}
