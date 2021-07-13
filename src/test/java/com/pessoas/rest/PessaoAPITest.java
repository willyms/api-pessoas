package com.pessoas.rest;

import com.fasterxml.jackson.databind.*;
import com.github.javafaker.*;
import com.pessoas.models.*;
import com.pessoas.services.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.*;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;


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
	public void getContext() {
		Pessoa pessoa = Pessoa
							.builder()
							.cpf("489.970.870-02")
							.nome(faker.name().fullName())
							.build();

		Address address = faker.address();

		pessoa.addEnderecos(
				Endereco
				.builder()
						.bairro(address.country())
						.cep(address.countryCode())
						.cidade(address.city())
						.complemento(address.buildingNumber())
						.endereco(address.streetAddress())
				.build()
		);
        service.save(pessoa);
	}
	
	@Test
    public void listar_todos_ok() throws Exception {
        mockMvc.perform(get(PATH_API_PESSOAS))
        		.andDo(print())
        		.andExpect(status()
        		.isOk())
        		.andExpect(jsonPath("$.[*].id").isNotEmpty());
    }
	
	@Test
	public void inserir_pessoa_ok() throws Exception {
		Pessoa pessoa = new Pessoa();
        pessoa.setNome("Tiago Luiz Isaac Fogaaa");
        pessoa.setCpf("442.490.091-65");
       
        Endereco endereco = new Endereco();
        endereco.setBairro("Recanto das Emas");
        endereco.setCep("72630-218");
        endereco.setCidade("Brasilia");
        endereco.setComplemento("620");
        endereco.setEndereco("Avenida Ponte Alta Quadra 402");

        pessoa.addEnderecos(endereco);
		
        mockMvc.perform(MockMvcRequestBuilders
        		.post(PATH_API_PESSOAS)
        		.content(asJsonString(pessoa))
        		.contentType(MediaType.APPLICATION_JSON_VALUE)
        		.accept(MediaType.APPLICATION_JSON))
		      	.andExpect(status().isCreated())
		      	.andExpect(jsonPath("$.id").exists());
	}
	
	@Test
	public void atualizar_pessoa_ok() throws Exception {
		Optional<Pessoa> optional = service.findById(1L);

		Assertions.assertTrue(optional.isPresent());

		Pessoa p = optional.get();
			   p.setNome("nome alterado");
		
		mockMvc.perform(MockMvcRequestBuilders
						.put(PATH_API_PESSOAS_WITH_ID, p.getId())
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(asJsonString(p)))
						.andExpect(status().isAccepted())
						.andExpect(jsonPath("$.nome", is("nome alterado")));
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
}
