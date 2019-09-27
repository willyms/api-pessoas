package com.pessoas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pessoas.models.Pessoa;
import com.pessoas.repository.PessoaRepository;

@Service
public class PessoaService {
	
	@Autowired
	private PessoaRepository pessoaRepository;

    public List<Pessoa> findAll() {
        return pessoaRepository.findAll();
    }

    public Optional<Pessoa> findById(Long id) {
        return pessoaRepository.findById(id);
    }

    public Pessoa save(Pessoa pessoa) {
    	 Pessoa pessoa2 = null;
    	try {
    		 pessoa2 = pessoaRepository.save(pessoa);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
        return pessoa2;
    }

    public void deleteById(Long id) {
        pessoaRepository.deleteById(id);
    }
}
