package br.ce.wcaquino.rest.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import br.ce.wcaquino.rest.core.BaseTest;
import br.ce.wcaquino.rest.utils.DateUtils;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;

//Aplicação Real - API RestFull
//
//Critérios de Aceite - Cenários
//
//	1- Não deve acessar API sem token - GET /contas
//	2- Deve acessar API com sucesso - POST /Signin (E-mail e Senha) - Before
//	3- Deve incluir conta com sucesso - POST /contas (Nome)
//	4- Deve alterar conta com sucesso - PUT /contas (Nome)
//	5- Não deve incluir conta com nome repetido - POST /contas
//	6- Deve inserir movimentação com sucesso - Post /transacoes (conta_id, usuario_id, descricao, envolvido, tipo (DESP / REC), data_transacao(dd_MM_YYYY), data_pagamento(dd_MM_YYYY), valor(0.00f), status(true/false)
//	7- Deve validar compos obrigatórios das transações - POST /transacoes ( testes de dominio)
//	8- Não deve cadastrar movimentação futuras - POST /transacoes - data_transacao(dd_MM_YYYY) menor ou igual data atual
//	9- Não deve remover conta com movimentação - DELETE /conta/:id
//	10- Deve calcular saldo Contas - GET /saldo
//	11- Deve remover movimentacao - DELETE /transacoes/:id

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BarrigaTest extends BaseTest{
	
	
	private static String CONTA_NAME = "Conta" + System.nanoTime();
	private static Integer CONTA_ID;
	private static Integer MOV_ID;
	
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
		
	}

	
	@Test
	public void t02_deveInserirContaComSucesso() {
		CONTA_ID = given()
			.body("{\"nome\": \""+CONTA_NAME+"\"}")
		.when()
			.post("/contas")
				
		.then()
			.statusCode(201)
			.extract().path("id")
		
		;
	}
	
	@Test
	public void t03_deveAlterarContaComSucesso() {
		given()
			.body("{\"nome\": \""+CONTA_NAME+" Alterada\"}")
			.pathParam("id", CONTA_ID)
		.when()
			
			.put("/contas/{id}") // recebe id do pathParam
				
		.then()
			.statusCode(200)
			.body("nome", is(""+CONTA_NAME+" Alterada"))
		
		;
	}
	
	@Test
	public void t04_naoDeveInserirContaComMesmoNome() {
		given()
			.body("{\"nome\": \""+CONTA_NAME+" Alterada\"}")
		.when()
			.post("/contas")
				
		.then()
			.statusCode(400)
			.body("error", is("Já existe uma conta com esse nome!"))
		
		;
	}
	
	@Test
	public void t05_deveInserirMovimentacaoComSucesso() {

		Movimentacao mov = getMovimentacaoValida();
				
		MOV_ID = given()
			.body(mov)
		.when()
			.post("/transacoes")
				
		.then()
			.statusCode(201)
			.extract().path("id")
		
		;
	}
	
	@Test
	public void t06_deveValidarCamposObrigatoriosNaMovimentacao() {

		given()
			.body("{}")
		.when()
			.post("/transacoes")
				
		.then()
			.statusCode(400)
			.body("$" , hasSize(8))
			.body("msg", hasItems(

					"Data da Movimentação é obrigatório",
					"Data do pagamento é obrigatório",
					"Descrição é obrigatório",
					"Interessado é obrigatório",
					"Valor é obrigatório",
					"Valor deve ser um número",
					"Conta é obrigatório",
					"Situação é obrigatório"
					))
		;
	}
	
	
	
	@Test
	public void t07_naodeveInserirMovimentacaoComDataFutura() {

		Movimentacao mov = getMovimentacaoValida();
		mov.setData_transacao(DateUtils.getDateDiferencaDias(3));
		
		
		given()
			.body(mov)
		.when()
			.post("/transacoes")
				
		.then()
			.statusCode(400)
			.body("$" , hasSize(1))
			.body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"))
					
		;
	}
	
	@Test
	public void t08_naoDeveRemoverContaComMovimentacao() {

				
		given()
			.pathParam("id", CONTA_ID)
		.when()
			.delete("/contas/{id}")
				
		.then()
			.statusCode(500)
			.body("constraint", is("transacoes_conta_id_foreign"))
		
		;
	}
	
	@Test
	public void t09_deveCalcularSaldoContas() {

				
		given()
		.when()
			.get("/saldo")
				
		.then()
			.statusCode(200)
			.body("find{it.conta_id == "+CONTA_ID+"}.saldo", is("321.00"))
		
		;
	}
	
	@Test
	public void t10_deveRemoverMovimentacao() {

				
		given()
			.pathParam("id", MOV_ID)
		.when()
			.delete("/transacoes/{id}")
				
		.then()
			.statusCode(204)
		
		;
	}
	
		
	@Test
	public void t11_naoDeveAcessarAPISemToken() {
		
		FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
		req.removeHeader("Authorization");
		given()
		
		.when()
			.post("/contas")
		
		.then()
		  	.statusCode(401)
		;
		
	}
	
	
	private Movimentacao getMovimentacaoValida() {
		Movimentacao mov = new Movimentacao();
		mov.setConta_id(CONTA_ID);
		//mov.setUsuario_id();
		mov.setDescricao("Inserindo Primeira Movimentacao");
		mov.setEnvolvido("Envolvido na Movimentacao");
		mov.setTipo("REC");
		mov.setData_transacao(DateUtils.getDateDiferencaDias(-1));
		mov.setData_pagamento(DateUtils.getDateDiferencaDias(5));
		mov.setValor(321f);
		mov.setStatus(true);
		return mov;
	}
	
	
}


