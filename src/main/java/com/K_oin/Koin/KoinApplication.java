package com.K_oin.Koin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KoinApplication {

	public static void main(String[] args) {
		SpringApplication.run(KoinApplication.class, args);
	}

}
