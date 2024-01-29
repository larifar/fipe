package br.com.alura.fipe;

import br.com.alura.fipe.exceptions.ExceptionHandler;
import br.com.alura.fipe.principal.Principal;
import br.com.alura.fipe.service.ConsumoApi;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class FipeApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(FipeApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Principal principal = new Principal();
		try {
			principal.exibeMenu();
		} catch (RuntimeException | IOException e){
			ExceptionHandler.treatException("Erro ao tentar conectar com a API",e);
		} catch (Exception e) {
			ExceptionHandler.treatException("Erro desconhecido: ",e);
		}
	}
}
