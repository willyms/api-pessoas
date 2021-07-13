package com.pessoas.controllers;


import com.pessoas.exception.*;
import com.pessoas.models.*;
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
	@ApiOperation(value="Salva um pessoa")
	public ResponseEntity<Pessoa> adicionar(@Valid @RequestBody Pessoa pessoa) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(pessoa));
	}
	
	@GetMapping
	@ApiOperation(value="Retorna uma lista de Pessoas", response = List.class)
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "Lista recuperada com sucesso"),
		    @ApiResponse(code = 404, message = "O recurso que voce estava tentando acessar nao foi encontrado")
		})
	public List<Pessoa> listar() {
		return service.findAll();
	}
	
	@GetMapping("/{id}")
	@ApiOperation(value="Retorna um Pessoa especifico")
	public ResponseEntity<Pessoa> buscar(@ApiParam(value = "ID da pessoa para buscar o objeto", required = true) @PathVariable Long id) throws ResourceNotFoundException {
		Pessoa pessoa = service.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pessoa nao encontrado para este ID :: " + id));
		return ResponseEntity.ok(pessoa);
	}
	
	@PutMapping("/{id}")
	@ApiOperation(value="Atualizar um pessoa")
	public ResponseEntity<Pessoa> atualizar(@ApiParam(value = "ID do pessoa para atualizar o objeto", required = true) @PathVariable Long id, 
											@ApiParam(value = "Atualizar objeto da pessoa", required = true) @Valid @RequestBody Pessoa pessoa) throws ResourceNotFoundException {
		Pessoa retorno = service.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pessoa nao encontrado para este ID :: " + id));
				
		BeanUtils.copyProperties(pessoa, retorno, "id");
				
		return ResponseEntity.accepted().body(service.save(retorno));
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value="Excluir um pessoa")
	public ResponseEntity<Void> remover(
			@ApiParam(value = "ID da pessoa que sera excluido", required = true)
			@PathVariable Long id) throws Exception {
		
		Pessoa pessoa = service.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pessoa nao encontrado para este ID :: " + id));
		
		service.deleteById(pessoa.getId());
		
		return ResponseEntity.noContent().build();
	}
}
