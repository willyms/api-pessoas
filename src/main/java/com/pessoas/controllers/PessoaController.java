package com.pessoas.controllers;


import com.pessoas.exception.*;
import com.pessoas.models.*;
import com.pessoas.models.dto.*;
import com.pessoas.services.*;
import io.swagger.annotations.*;
import lombok.*;
import org.springframework.beans.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pessoas")
public class PessoaController {
	
	private final PessoaService service;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value="Salva um pessoa")
	public MensagemRespostaDTO adicionar(@Valid @RequestBody PessoaDTO pessoaDTO) {
		return service.inserir(pessoaDTO);
	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value="Retorna uma lista de Pessoas", response = List.class)
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "Lista recuperada com sucesso"),
		    @ApiResponse(code = 404, message = "O recurso que voce estava tentando acessar nao foi encontrado")
		})
	public List<PessoaDTO> listar() {
		return service.listaTodos();
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value="Retorna um Pessoa especifico")
	public PessoaDTO buscar(@ApiParam(value = "ID da pessoa para buscar o objeto", required = true) @PathVariable Long id) throws ResourceNotFoundException {
		return service.buscarPeloId(id);
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value="Atualizar um pessoa")
	public MensagemRespostaDTO atualizar(@ApiParam(value = "ID do pessoa para atualizar o objeto", required = true) @PathVariable Long id,
										 @ApiParam(value = "Atualizar objeto da pessoa", required = true) @Valid @RequestBody PessoaDTO pessoaDTO) throws ResourceNotFoundException {
		return service.atualizar(id, pessoaDTO);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value="Excluir um pessoa")
	public void remover(
			@ApiParam(value = "ID da pessoa que sera excluido", required = true)
			@PathVariable Long id) throws Exception {
		service.excluirPeloId(id);
	}
}
