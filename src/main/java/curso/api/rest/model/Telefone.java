package curso.api.rest.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Telefone {

	@ Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String numero;
	
	@JsonIgnore /* Evita recursividade de dados do JSON */
	@org.hibernate.annotations.ForeignKey(name = "usuario_id")
	@ManyToOne(optional = false) // Cria uma restricao NOT NULL  no banco de dados que e obrigatorio ter um pai
	private Usuario usuario;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
}
