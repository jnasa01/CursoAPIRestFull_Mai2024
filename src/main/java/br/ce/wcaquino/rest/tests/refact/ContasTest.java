package br.ce.wcaquino.rest.tests.refact;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import br.ce.wcaquino.rest.core.BaseTest;
import br.ce.wcaquino.rest.utils.BarrigaUtils;

public class ContasTest extends BaseTest{
	
	@Test
	public void deveInserirContaComSucesso() {
		given()
			.body("{\"nome\": \"Conta Inserida\"}")
		.when()
			.post("/contas")
				
		.then()
			.statusCode(201)
		;
	}
	
	@Test
	public void deveAlterarContaComSucesso() {
		Integer CONTA_ID = BarrigaUtils.getIdContaPeloNome("Conta para alterar");
		
		given()
			.body("{\"nome\": \"Conta Alterada\"}")
			.pathParam("id", CONTA_ID)
			
		.when()
			.put("/contas/{id}") // recebe id do pathParam
			
		.then()
			.statusCode(200)
			.body("nome", is("Conta Alterada"))
		
		;
	}
	
	@Test
	public void naoDeveInserirContaComMesmoNome() {
		given()
			.body("{\"nome\": \"Conta mesmo nome\"}")
		.when()
			.post("/contas")
				
		.then()
			.statusCode(400)
			.body("error", is("Já existe uma conta com esse nome!"))
		
		;
	}
	
	


}
