package com.pessoas.models.dto;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoDTO {
	private Long id;
	private String endereco;
	private String cep;
	private String bairro;
	private String complemento;
	private String cidade;

}
