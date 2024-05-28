package br.ce.wcaquino.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

public class FileTest {

	@Test
	public void deveObrigarEnvioArquivo() {
		given()
			.log().all()
		
		.when()
			.post("http://restapi.wcaquino.me/upload")
			
		.then()
			.log().all()
			.statusCode(404) // O StatusCode deveria ser 400
			.body("error", is("Arquivo não enviado"))
		
		;
		
	}
	
	@Test
	public void deveFazerUploadArquivo() {
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/usuarios.pdf"))
		
		.when()
			.post("http://restapi.wcaquino.me/upload")
			
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("usuarios.pdf"))
		
		;
		
	}
	
	
	@Test
	public void naoDeveFazerUploadArquivo() {
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/ruby.pdf"))
		
		.when()
			.post("http://restapi.wcaquino.me/upload")
			
		.then()
			.log().all()
			.time(lessThan(5000L))
			.statusCode(413)
			//.body("name", is("usuarios.pdf"))
		
		;
		
	}
	
	
	@Test
	public void deveBaixarArquivo() throws IOException {
		
		byte[] image = given()
			.log().all()
		
		.when()
			.get("http://restapi.wcaquino.me/download")
		
		.then()
			//.log().all()
			.statusCode(200)
			.extract().asByteArray();
		
		File imagem = new File("downloads/foto.jpg");
		OutputStream out = new FileOutputStream(imagem);
		out.write(image);
		out.close();
		System.out.println(imagem.length());
		assertThat(imagem.length(), lessThan(1000000L));
		
		;
		
	}
}
