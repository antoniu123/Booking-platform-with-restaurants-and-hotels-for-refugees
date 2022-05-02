package com.backend.auth0springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class Auth0SpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(Auth0SpringBootApplication.class, args);
	}

}
