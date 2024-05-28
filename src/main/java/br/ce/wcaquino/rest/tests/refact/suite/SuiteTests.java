package br.ce.wcaquino.rest.tests.refact.suite;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.ce.wcaquino.rest.core.BaseTest;
import br.ce.wcaquino.rest.tests.refact.AuthTest;
import br.ce.wcaquino.rest.tests.refact.ContasTest;
import br.ce.wcaquino.rest.tests.refact.MovimentacaoTest;
import br.ce.wcaquino.rest.tests.refact.SaldoTest;
import io.restassured.RestAssured;

@RunWith(Suite.class)
@SuiteClasses({
	ContasTest.class,
	MovimentacaoTest.class,
	SaldoTest.class,
	AuthTest.class
})
public class SuiteTests extends BaseTest{
	
	@BeforeClass
	public static void login() {
		Map<String , String> login = new HashMap<>();
		login.put("email", "jnasa01@gmail.com");
		login.put("senha", "Jnasa550301");
		
		String TOKEN = given()
			.body(login)
		
		.when()
			.post("/signin")
		
		.then()
		  	.statusCode(200)
		  	.extract().path("token");
		
		RestAssured.requestSpecification.header("Authorization", "JWT " + TOKEN);
		RestAssured.get("/reset").then().statusCode(200);
		
	}
	

}
