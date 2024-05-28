package br.ce.wcaquino.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.XmlPath.CompatibilityMode;


public class AuthenticationTest {
	
	@Test
	public void deveAcessarSWAPI() {
		
		given()
			.log().all()
		.when()
			.get("https://swapi.dev/api/people/1/")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("Luke Skywalker"))
		
		;
	}
	
		
		@Test
		public void deveObterClima() {
			
			given()
				.log().all()
				.queryParam("q", "Fortaleza,BR")
				.queryParam("appid", "935bb0363defbf46ba4b84a36668a760")
				.queryParam("units", "metric")
			.when()
				.get("https://api.openweathermap.org/data/2.5/weather")
			.then()
				.log().all()
				.statusCode(200)
				.body("name", is("Fortaleza"))
				.body("coord.lon", is(-38.5247f))
				.body("main.temp", greaterThan(25f))
			
			;
		}
	
		
		@Test
		public void naoDveAcessarSemSenha() {
			
			given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(401)
		;
			
		}
		
		@Test
		public void deveFazerAutenticacaoBasica() {
			
			given()
			.log().all()
		.when()
			.get("https://admin:senha@restapi.wcaquino.me/basicauth") // enviando Usuário e senha na requisição
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;
			
		}
		
		@Test
		public void deveFazerAutenticacaoBasica2() {
			
			given()
			.log().all()
			.auth().basic("admin", "senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth") // enviando Usuário e senha no metodo .auth().basic() no given
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;
			
		}
		
		@Test
		public void deveFazerAutenticacaoBasicaChallenge() {
			
			given()
			.log().all()
			.auth().preemptive().basic("admin", "senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth2") // enviando Usuário e senha no metodo .auth().basic() no given
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		
		;
			
		}
		
		@Test
		public void deveFazerAutenticacaoComToken() {
				
			Map<String, String> login = new HashMap<String, String>();
			login.put("email", "jnasa01@gmail.com");
			login.put("senha", "Jnasa550301");
			
			
			String token = given()
			.log().all()
			.body(login)
			.contentType(io.restassured.http.ContentType.JSON)
			
		.when()
			.post("https://barrigarest.wcaquino.me/signin")
		.then()
			.log().all()
			.statusCode(200)
			.extract().path("token");
		;
		
		// Obter Contas
		 given()
			.log().all()
			.header("Authorization","JWT " + token)
		.when()
			.get("https://barrigarest.wcaquino.me/contas")
		.then()
			.log().all()
			.statusCode(200)
			.body("nome", hasItem("Conta de Teste"))

			;		
			
		}
		
		@Test
		public void deveAcessarAplicacaoWeb() {
			 String cookie = given()
				.log().all()
				.formParam("email", "jnasa01@gmail.com")
				.formParam("senha", "Jnasa550301")
				.contentType(io.restassured.http.ContentType.URLENC.withCharset("UTF-8"))
			.when()
				.post("https://seubarriga.wcaquino.me/logar")
			.then()
				.log().all()
				.statusCode(200)
				.extract().header("set-cookie")

				;
			 
			 cookie = cookie.split("=")[1].split(";")[0];
			 System.out.println(cookie);
			 
			
			 
			 String body = given()
				.log().all()
				.cookies("connect.sid", cookie)
			.when()
				.get("https://seubarriga.wcaquino.me/contas")
			.then()
				.log().all()
				.statusCode(200)
				.body("html.body.table.tbody.tr[0].td[0]", is("Conta de Teste"))
				.extract().body().asString();
				;
			System.out.println("=================================================================================================");
			XmlPath xmlpath = new XmlPath(CompatibilityMode.HTML, body);
			System.out.println(xmlpath.getString("html.body.table.tbody.tr[0].td[0]"));
			 
				
		}
		

		
		
		
}
