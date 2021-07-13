package com.pessoas.mappear;

import com.pessoas.models.*;
import com.pessoas.models.dto.*;
import org.mapstruct.*;

@Mapper
public interface MapearPessoa {
    @Mapping(target = "dataNascimento", source = "dataNascimento", dateFormat = "dd-MM-yyyy")
    Pessoa toModel(PessoaDTO dto);

    @Mapping(target = "dataNascimento", source = "dataNascimento", dateFormat = "dd-MM-yyyy")
    PessoaDTO toDTO(Pessoa dto);
}
