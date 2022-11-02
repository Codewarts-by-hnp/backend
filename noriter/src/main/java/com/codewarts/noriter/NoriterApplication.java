package com.codewarts.noriter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class NoriterApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoriterApplication.class, args);
	}

}
