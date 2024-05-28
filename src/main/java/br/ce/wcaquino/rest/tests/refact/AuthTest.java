package br.ce.wcaquino.rest.tests.refact;

import static io.restassured.RestAssured.given;

import org.junit.Test;

import br.ce.wcaquino.rest.core.BaseTest;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;

public class AuthTest extends BaseTest{
	
	@Test
	public void naoDeveAcessarAPISemToken() {
		
		FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
		req.removeHeader("Authorization");
		given()
		
		.when()
			.post("/contas")
		
		.then()
		  	.statusCode(401)
		;
		
	}
	
}
