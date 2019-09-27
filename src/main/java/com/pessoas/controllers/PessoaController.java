package com.pessoas.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pessoas.exception.ResourceNotFoundException;
import com.pessoas.models.Pessoa;
import com.pessoas.services.PessoaService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/v1/pessoas")
public class PessoaController {
	
	@Autowired
	private PessoaService service;
	
	@PostMapping
	@ApiOperation(value="Salva um pessoa")
	public ResponseEntity<Pessoa> adicionar(@Valid @RequestBody Pessoa pessoa) {		
		Pessoa save = service.save(pessoa);		
		System.out.println(save);
		return ResponseEntity.status(HttpStatus.CREATED).body(save);
	}
	
	@GetMapping
	@ApiOperation(value="Retorna uma lista de Pessoas", response = List.class)
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "Lista recuperada com sucesso"),
		    @ApiResponse(code = 404, message = "O recurso que você estava tentando acessar não foi encontrado")
		})
	public List<Pessoa> listar() {
		return service.findAll();
	}
	
	@GetMapping("/{id}")
	@ApiOperation(value="Retorna um Pessoa específico")
	public ResponseEntity<Pessoa> buscar(@ApiParam(value = "ID da pessoa para buscar o objeto", required = true) @PathVariable Long id) throws ResourceNotFoundException {
		Pessoa pessoa = service.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrado para este ID :: " + id));
		return ResponseEntity.ok(pessoa);
	}
	
	@PutMapping("/{id}")
	@ApiOperation(value="Atualizar um pessoa")
	public ResponseEntity<Pessoa> atualizar(@ApiParam(value = "ID do pessoa para atualizar o objeto", required = true) @PathVariable Long id, 
											@ApiParam(value = "Atualizar objeto da pessoa", required = true) @Valid @RequestBody Pessoa pessoa) throws ResourceNotFoundException {
		Pessoa retorno = service.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrado para este ID :: " + id));
				
		BeanUtils.copyProperties(pessoa, retorno, "id");
				
		return ResponseEntity.accepted().body(service.save(retorno));
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value="Deleta um pessoa")
	public ResponseEntity<Void> remover(
			@ApiParam(value = "ID da pessoa que será excluído", required = true) 
			@PathVariable Long id) throws Exception {
		
		Pessoa pessoa = service.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrado para este ID :: " + id));
		
		service.deleteById(pessoa.getId());
		
		return ResponseEntity.noContent().build();
	}
}
