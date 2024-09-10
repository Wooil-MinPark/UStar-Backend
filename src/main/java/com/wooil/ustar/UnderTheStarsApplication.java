package com.wooil.ustar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UnderTheStarsApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnderTheStarsApplication.class, args);
	}

}
