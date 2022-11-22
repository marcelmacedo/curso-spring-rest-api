package curso.api.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;

@CrossOrigin(origins = "*") /*Controle de acesso a API*/
@RestController /* Arquitetura REST */
@RequestMapping(value = "/usuario")
public class IndexController {
	
	/* Serviço RestFul*/
	/*
	 * @GetMapping(value = "/", produces = "application/json") 
	 * public ResponseEntity init() { 
	 * 		return new ResponseEntity("Ola Spring Rest API", HttpStatus.OK); 
	 * }
	 */

	@Autowired /*Se fosse CDI seria @Inject*/
	private UsuarioRepository usuarioRepository;

	
	/*Serviço RESTfull*/
	@GetMapping(value = "/{id}/relatoriopdf", produces = "application/pdf")
	public ResponseEntity<Usuario> relatorio(@PathVariable(value = "id") Long id) {
		
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		
		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}
		
	/*Serviço RESTfull*/
	@GetMapping(value = "/{id}", produces = "application/json", headers = "X-API-Version=v1")
	public ResponseEntity<Usuario> initV1(@PathVariable(value = "id") Long id) {
		return usuarioRepository.findById(id)
//				.map(usuario -> ResponseEntity.ok(usuario)) // Se encontrou registro retorna objeto
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build()); // Retorna objeto não encontrado (not found).

	}

	/*Serviço RESTfull*/
	@GetMapping(value = "/{id}", produces = "application/json", headers = "X-API-Version=v2")
	public ResponseEntity<Usuario> initV2(@PathVariable(value = "id") Long id) {
		//return usuarioRepository.findById(id).map(usuario -> ResponseEntity.ok(usuario)) // Se encontrou registro retorna objeto

		Optional<Usuario> usuario = usuarioRepository.findById(id);
		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/", produces = "application/json")
	@CacheEvict(value = "cacheusuarios", allEntries = true) /* @CacheEvict Remove cache nao utilizado em longo periodo*/
	@CachePut("cacheusuarios")/* Identifica que houve alteracao e colocar no cache */
	public ResponseEntity<List<Usuario>> usuario(){
		
		List<Usuario> list = (List<Usuario>) usuarioRepository.findAll();
		
		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
	}
	
	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario){
		/*- Varre a lista de telefones, amarra os telefones ao usuario.
		  - Realiza a associação da tabela filha p	ara o pai. */
		for(int pos = 0; pos < usuario.getTelefones().size(); pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		
		/* Criptografar senha do usuario */
		String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(senhaCriptografada);

		/* Salva dados do usuario */
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> atualizar(@RequestBody Usuario usuario){
		/* Varre a lista de telefones, amarra os telefones ao usuario. */
		/* Realiza a associação da tabela filha p	ara o pai. */
		for(int pos = 0; pos < usuario.getTelefones().size(); pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		
		/* Obtem login do usuario para verificar se as senhas sao diferentes */
		Usuario userTemporario = usuarioRepository.findUserByLogin(usuario.getLogin());

		/* Se senha diferentes, criptrografa nova senha usuario */
		if(!userTemporario.getSenha().equals(usuario.getSenha())) {
			String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
			usuario.setSenha(senhaCriptografada);
		}
		
		/* Atualiza dados */
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}", produces = "application/text")
	public String deletar(@PathVariable(value = "id") Long id) {
		
		usuarioRepository.deleteById(id);
		
		return "Usuario deletado!";
	}

}
