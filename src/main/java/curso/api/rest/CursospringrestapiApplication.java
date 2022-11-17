package curso.api.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication													// Saber que é uma aplicação SpringBoot iniciar os recursos
@EntityScan(basePackages = {"curso.api.rest.model"})					// Varer as pastas de Model.
@ComponentScan(basePackages = {"curso.*"})								// Vare todas as pastas que tem no projeto
@EnableJpaRepositories(basePackages = {"curso.api.rest.repository"})	// Local do repository, interfaces de persistencias
@EnableTransactionManagement   											// Controla transação no banco de dados.
@EnableWebMvc 															// Habilita recursos MVC
@RestController															// O projeto roda rest controller e retorna Json.
@EnableAutoConfiguration												// Spring configura todo projeto
public class CursospringrestapiApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(CursospringrestapiApplication.class, args);
		/* Gerar senha no console para teste de login e senha com JWT*/
		//System.out.println(new BCryptPasswordEncoder().encode("123"));
	}
	
	/*Controle mapeamento global para restringir acesso a API*/
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
		registry.addMapping("/usuario/**") /* Liberar tudo que estiver mapeado */
		.allowedMethods("*") /* Pode restringir quais metodos POST", "PUT", "DELETE pode ou nao ser acessado */
		.allowedOrigins("*"); /* Pode restringir qual ou quais servidores podem ter acesso "www.jdevtreinamento.com.br", "localhost:8080"*/
		/* Liberando mapeamento para todas as origens */
	}

}
