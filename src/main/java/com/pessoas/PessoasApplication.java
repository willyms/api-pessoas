package com.pessoas;


import com.github.javafaker.*;
import com.pessoas.mappear.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.*;
import java.util.*;

@SpringBootApplication
public class PessoasApplication {

	public static void main(String[] args) {
		SpringApplication.run(PessoasApplication.class, args);
	}

	@Bean
	public Faker getFaker() {
		return new Faker(new Locale("pt-BR"));
	}
}
