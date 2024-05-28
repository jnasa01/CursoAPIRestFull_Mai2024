package br.ce.wcaquino.rest.tests.refact;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import br.ce.wcaquino.rest.core.BaseTest;
import br.ce.wcaquino.rest.tests.Movimentacao;
import br.ce.wcaquino.rest.utils.BarrigaUtils;
import br.ce.wcaquino.rest.utils.DateUtils;

public class MovimentacaoTest extends BaseTest{
	
	@Test
	public void deveInserirMovimentacaoComSucesso() {

		Movimentacao mov = getMovimentacaoValida();
		given()
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.statusCode(201)
			.body("descricao", is("Inserindo Primeira Movimentacao"))
			.body("envolvido", is("Envolvido na Movimentacao"))
		;
	}
	
	@Test
	public void deveValidarCamposObrigatoriosNaMovimentacao() {

		given()
			.body("{}")
		.when()
			.post("/transacoes")
				
		.then()
			.statusCode(400)
			.body("$" , hasSize(8))
			.body("msg", hasItems(

					"Data da Movimenta��o � obrigat�rio",
					"Data do pagamento � obrigat�rio",
					"Descri��o � obrigat�rio",
					"Interessado � obrigat�rio",
					"Valor � obrigat�rio",
					"Valor deve ser um n�mero",
					"Conta � obrigat�rio",
					"Situa��o � obrigat�rio"
					))
		;
	}
	
	@Test
	public void naodeveInserirMovimentacaoComDataFutura() {

		Movimentacao mov = getMovimentacaoValida();
		mov.setData_transacao(DateUtils.getDateDiferencaDias(3));
		
		
		given()
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.statusCode(400)
			.body("$" , hasSize(1))
			.body("msg", hasItem("Data da Movimenta��o deve ser menor ou igual � data atual"))
					
		;
	}
	
	@Test
	public void naoDeveRemoverContaComMovimentacao() {
		
		Integer CONTA_ID = BarrigaUtils.getIdContaPeloNome("Conta com movimentacao");

				
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
	public void deveRemoverMovimentacao() {
		
		Integer MOV_ID = BarrigaUtils.getIdMovimentacaoPeloNome("Movimentacao para exclusao");
		
		//Movimentacao para exclusao
		given()
			.pathParam("id", MOV_ID)
		.when()
			.delete("/transacoes/{id}")
		.then()
			.statusCode(204)
		;
	}
	


	private Movimentacao getMovimentacaoValida() {
		Movimentacao mov = new Movimentacao();
		mov.setConta_id(BarrigaUtils.getIdContaPeloNome("Conta para movimentacoes"));
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
