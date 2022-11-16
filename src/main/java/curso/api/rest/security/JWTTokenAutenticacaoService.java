package curso.api.rest.security;

import java.util.Date;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.config.annotation.rsocket.RSocketSecurity.JwtSpec;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import curso.api.rest.ApplicationContexLoad;
import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service		// Anotacao para camada de servico do spring.
@Component		// Informa componente de classe para fazer injecao de dependencia.
public class JWTTokenAutenticacaoService {

	// Parametro que tem conteudo fixo sem anteracao.
	/* Tempo de validade do Token 2 dias */
	private static final long EXPIRATION_TIME = 172800000;
	
	/*Senha unica para compor autenticacao e ajudar na seguranca*/
	private static final String SECRET = "*SenhaExtremamenteSecreta";
	
	/*Prefixo padrao de Token*/
	private static final String TOKEN_PREFIX = "Bearer";
	
	/* Prefixo que retorna ao cabecalho do Token */
	private static final String HEADER_STRING = "Autorization";
	
	/* Gerando token de autenticacao e adicionando resposta ao HEADER como resposta HTTP */
	/* O HttpServletRequest representa a requisição do cliente e contém as informações ao seu respeito. 
	 * O HttpServletResponse representa a resposta do Servlet, */
	public void addAutentication(HttpServletResponse response, String username) throws Exception {
		
		/* Montagem do Token */
		String JWT = Jwts.builder() /* Chama gerador de token */
				.setSubject(username) /* Adiciona usuario */
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) /* Tempo de expiracao */
				.signWith(SignatureAlgorithm.HS512, SECRET).compact() /* Compactacao e algoritmo de geracao de senha */
				;
		/* Junta Token com Prefixo*/
		String token = TOKEN_PREFIX + " " + JWT; /*Bearer 4353245345wqe562fgytru6854aest54656*/
		
		/* Adiciona cabecalho HTTP */
		response.addHeader(HEADER_STRING, token);
		
		/* Escreve token como resposta no corpo do HTTP */
		response.getWriter().write("{\"Autorization\": \""+token+"\"}");
	
	}
	
	/* Retorna usuario validado com token ou caso nao seja valido retorna null */

	public Authentication getAuthentication(HttpServletRequest request) {
	
		/* Pega o token enviado no cabecalho HTTP*/
		String token = request.getHeader(HEADER_STRING);
		
		if(token != null) {
			/* Faz a validacao do token do usuario da requisicao */
			String user = Jwts.parser().setSigningKey(SECRET) 			/*Bearer 4353245345wqe562fgytru6854aest54656*/
					.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))	/*4353245345wqe562fgytru6854aest54656*/
					.getBody().getSubject(); /*Retorna somente usuario. Joao Silva*/
			if(user != null) {
				
				Usuario usuario = ApplicationContexLoad.getApplicationContext()
						.getBean(UsuarioRepository.class)
						.findUserByLogin(user);
				
				/* Retornar usuario logado */
				if(usuario != null) {
					
				}else {
					return null; /*Usuario nao autorizado*/
				}
				
			}else {
				return null; /*Usuario nao autorizado*/
			}
			
		}else {
			return null; /* Nao autorizado */
		}
	}
	
	
	
}
