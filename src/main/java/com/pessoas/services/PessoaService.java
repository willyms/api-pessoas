package com.pessoas.services;

import com.pessoas.exception.*;
import com.pessoas.mappear.*;
import com.pessoas.models.*;
import com.pessoas.models.dto.*;
import com.pessoas.repository.*;
import lombok.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.stream.*;

@Service
@RequiredArgsConstructor
public class PessoaService {
	
	private final PessoaRepository pessoaRepository;
	private final MapearPessoa mapear;

    public List<PessoaDTO> listaTodos() {
        return pessoaRepository.findAll().stream()
                                .map(mapear::toDTO)
                                .collect(Collectors.toList());
    }

    public PessoaDTO buscarPeloId(Long id) throws ResourceNotFoundException {
        Pessoa pessoa = pessoaRepository.findById(id)
                                        .orElseThrow(() -> new ResourceNotFoundException(String.format("Pessoa com ID %d não encontrada!", id)));

        return mapear.toDTO(pessoa);
    }

    public MensagemRespostaDTO inserir(PessoaDTO dto) {
        Pessoa pessoa = mapear.toModel(dto);
        Pessoa pessoaSalvo = pessoaRepository.save(pessoa);
        return MensagemRespostaDTO.builder().mensagem(String.format("Pessoa inserido com sucesso com o ID %d", pessoaSalvo.getId())).build();
    }

    public MensagemRespostaDTO atualizar(Long id, PessoaDTO pessoaDTO) throws ResourceNotFoundException {
        pessoaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("Pessoa com ID %d não encontrada!", id)));

        Pessoa atualizadaPessoa = pessoaRepository.save(mapear.toModel(pessoaDTO));

        return MensagemRespostaDTO.builder().mensagem(String.format("Pessoa atualizando com sucesso com o ID %d", atualizadaPessoa.getId())).build();
    }

    public void excluirPeloId(Long id) throws ResourceNotFoundException {
        pessoaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Pessoa com ID %d não encontrada!", id)));
        pessoaRepository.deleteById(id);
    }
}
