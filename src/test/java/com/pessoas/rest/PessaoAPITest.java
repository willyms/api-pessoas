package com.pessoas.rest;

import com.fasterxml.jackson.databind.*;
import com.github.javafaker.*;
import com.pessoas.models.dto.*;
import com.pessoas.services.*;
import com.pessoas.utils.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.*;
import java.util.stream.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PessaoAPITest {
	private static final String PATH_API_PESSOAS = "/api/v1/pessoas/";
	private static final String PATH_API_PESSOAS_WITH_ID = "/api/v1/pessoas/{id}";
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
    private PessoaService service;

	@Autowired
	private Faker faker;

	@BeforeEach
	public void init() {
		PessoaDTO pessoa = PessoaDTO
							.builder()
							.cpf(GeradorCPF.gerarCPFSemMascara())
							.nome(faker.name().firstName())
							.sobreNome(faker.name().lastName())
							.dataNascimento("20-10-1976")
							.build();

		Address address = faker.address();

		pessoa.addEnderecos(
				EnderecoDTO
				.builder()
						.bairro(address.country())
						.cep(address.countryCode())
						.cidade(address.city())
						.complemento(address.buildingNumber())
						.endereco(address.streetAddress())
				.build()
		);

		pessoa.addTelefones(TelefoneDTO.builder().numero(faker.phoneNumber().phoneNumber()).build());

        service.inserir(pessoa);
	}
	
	@Test
    public void listar_todos_ok() throws Exception {
        mockMvc.perform(get(PATH_API_PESSOAS))
        		.andDo(print())
        		.andExpect(status()
        		.isOk())
        		.andExpect(jsonPath("$.[*].id").isNotEmpty());
    }

	@ParameterizedTest(name = "#{index} - Nome: {0} {1}, CPF:{2}")
	@MethodSource("proverCincoPessoaAleatorias")
	public void inserir_pessoa_ok(String nome, String sobreNome, String cpf) throws Exception {

		PessoaDTO pessoaDTO = PessoaDTO.builder().nome(nome).sobreNome(sobreNome).cpf(cpf).dataNascimento("20-10-1976").build();
		pessoaDTO.addTelefones(TelefoneDTO.builder().numero(faker.phoneNumber().phoneNumber()).build());

		mockMvc.perform(MockMvcRequestBuilders
        		.post(PATH_API_PESSOAS)
        		.content(asJsonString(pessoaDTO))
        		.contentType(MediaType.APPLICATION_JSON_VALUE)
        		.accept(MediaType.APPLICATION_JSON))
		      	.andExpect(status().isCreated());
	}
	
	@Test
	public void atualizar_pessoa_ok() throws Exception {
		PessoaDTO pessoaDTO = service.buscarPeloId(1L);

		Assertions.assertNotNull(pessoaDTO);

		pessoaDTO.setNome("nome alterado");
		
		mockMvc.perform(MockMvcRequestBuilders
						.put(PATH_API_PESSOAS_WITH_ID, pessoaDTO.getId())
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(asJsonString(pessoaDTO)))
						.andExpect(status().isOk());
	}
	
	@Test
	public void deletar_pessoa_ok() throws Exception{
		mockMvc.perform(delete(PATH_API_PESSOAS_WITH_ID, 1)).andExpect(status().isNoContent());
	}
	
	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

	private static Stream<Arguments> proverCincoPessoaAleatorias() {
		Name pessoaFaker = new Faker().name();
		return Stream.of(
				Arguments.of(pessoaFaker.firstName(), pessoaFaker.lastName(), GeradorCPF.gerarCPFSemMascara()),
				Arguments.of(pessoaFaker.firstName(), pessoaFaker.lastName(), GeradorCPF.gerarCPFSemMascara()),
				Arguments.of(pessoaFaker.firstName(), pessoaFaker.lastName(), GeradorCPF.gerarCPFSemMascara()),
				Arguments.of(pessoaFaker.firstName(), pessoaFaker.lastName(), GeradorCPF.gerarCPFSemMascara()),
				Arguments.of(pessoaFaker.firstName(), pessoaFaker.lastName(), GeradorCPF.gerarCPFSemMascara())
		);
	}
}
