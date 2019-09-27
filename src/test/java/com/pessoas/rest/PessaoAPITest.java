package com.pessoas.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pessoas.models.Endereco;
import com.pessoas.models.Pessoa;
import com.pessoas.services.PessoaService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PessaoAPITest {
	private static final String PATH_API_PESSOAS = "/api/v1/pessoas/";
	private static final String PATH_API_PESSOAS_WITH_ID = "/api/v1/pessoas/{id}";
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
    private PessoaService service;
	
	
	@Before
	public void getContext() {
		assertNotNull(service);
		assertNotNull(mockMvc);
		
		Pessoa pessoa = new Pessoa();
        pessoa.setCpf("489.970.870-02");
        pessoa.setNome("William Sales");
       
        Endereco endereco = new Endereco();
        endereco.setBairro("Recanto das Emas");
        endereco.setCep("72630-218");
        endereco.setCidade("Brasilia");
        endereco.setComplemento("620");
        endereco.setEndereco("Avenida Ponte Alta Quadra 402");

        pessoa.addEnderecos(endereco);
        
        service.save(pessoa);
	}
	
	@Test
    public void todos_ok() throws Exception {
        mockMvc.perform(get(PATH_API_PESSOAS))
        		.andDo(print())
        		.andExpect(status()
        		.isOk())
        		.andExpect(jsonPath("$.[*].id").isNotEmpty());
    }
	
	@Test
	public void createEmployeeAPI() throws Exception {
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
        		.contentType(MediaType.APPLICATION_JSON_UTF8)
        		.accept(MediaType.APPLICATION_JSON))
		      	.andExpect(status().isCreated())
		      	.andExpect(jsonPath("$.id").exists());
	}
	
	@Test
	public void atualizar_ok() throws Exception {
		Optional<Pessoa> optional = service.findById(1L);
		
		assertNotNull(optional);
		
		Pessoa p = optional.get();
			   p.setNome("nome alterado");
		
		mockMvc.perform(MockMvcRequestBuilders
						.put(PATH_API_PESSOAS_WITH_ID, p.getId())
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(asJsonString(p)))
						.andExpect(status().isAccepted())
						.andExpect(jsonPath("$.nome", is("nome alterado")));
	}
	
	@Test
	public void delete_ok() throws Exception{
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
