package curso.api.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import curso.api.rest.service.ImplementacaoUserDatailsService;

/* Mapeia URL, enderecos, autoriza ou bloqueia acesso a URL*/
@SuppressWarnings("deprecation")
@Configuration 					/* Mapeia a classe e configura*/
@EnableWebSecurity 				/*Habilita todos os recursos de seguranca da aplicacao*/
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

	/*Aplicando a injecao de dependencia do service*/
	@Autowired
	private ImplementacaoUserDatailsService implementacaoUserDatailsService;
	
	/* Configura as solicitacoes de acesso via HTTP */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		/*Ativando protecao contra usuarios que nao estao validados por TOKEN*/
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		
		/*Ativando permissao de acesso para pagina inicial do sistema, EX.: sistema.com.br/index.html */
		.disable().authorizeRequests()
		.antMatchers("/").permitAll().antMatchers("/index").permitAll()
		
		/* URL de logout - Redireciona quando usuario deslogar do sistema */
		.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
		
		/* Mapeia URL de logout e invalida o usuario */
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
		
		/* Filta requisicoes de login para autenticacao */
		
		/* Filtra demais requisicoes para verificar a presenca do TOKEN JWT no HEADER HTTP */
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		/* Service que irá consultar o usuario no banco de dados */
		auth.userDetailsService(implementacaoUserDatailsService)
		/* Padao de codificacao de senha do usuario */
		.passwordEncoder(new BCryptPasswordEncoder());
	}
	
}
