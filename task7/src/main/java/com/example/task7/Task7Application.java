package com.example.task7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Task7Application {

	public static void main(String[] args) {
		SpringApplication.run(Task7Application.class, args);
	}

}
