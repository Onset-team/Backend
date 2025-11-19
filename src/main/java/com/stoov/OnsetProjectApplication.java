package com.stoov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OnsetProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnsetProjectApplication.class, args);
	}

}
