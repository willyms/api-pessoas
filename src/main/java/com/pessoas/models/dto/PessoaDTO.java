package com.pessoas.models.dto;

import lombok.*;
import org.hibernate.validator.constraints.br.*;

import javax.validation.constraints.*;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PessoaDTO {
    private Long id;

    @NotEmpty
    @Size(min = 2, max = 100)
    private String nome;

    @NotEmpty
    @Size(min = 2, max = 100)
    private String sobreNome;

    @NotEmpty
    @CPF
    private String cpf;

    @NotNull
    private String dataNascimento;

    private Set<TelefoneDTO> telefones;

    private Set<EnderecoDTO> enderecos;

    public void addEnderecos(EnderecoDTO endereco) {
        if(this.enderecos == null)
            this.enderecos = new HashSet<>();

        this.enderecos.add(endereco);
    }

    public void addTelefones(TelefoneDTO telefone) {
        if(this.telefones == null)
            this.telefones = new HashSet<>();

        this.telefones.add(telefone);
    }
}
