package com.codewarts.noriter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NoriterApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoriterApplication.class, args);
	}

}
