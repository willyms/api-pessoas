package com.pessoas.models;


import lombok.*;
import org.hibernate.validator.constraints.br.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.*;
import java.util.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pessoas")
public class Pessoa implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String nome;
		
	@CPF
	@NotBlank
	private String cpf;
	
	@NotNull
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Endereco> enderecos;

	public void addEnderecos(Endereco endereco) {
		if(this.enderecos == null)
			this.enderecos = new HashSet<>();
		
		this.enderecos.add(endereco);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		
		Pessoa pessoa = (Pessoa) o;
		
		return cpf != null ? cpf == pessoa.cpf : false;
	}

}
