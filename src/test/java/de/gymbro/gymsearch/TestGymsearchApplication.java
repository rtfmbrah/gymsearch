package de.gymbro.gymsearch;

import org.springframework.boot.SpringApplication;

public class TestGymsearchApplication {

	public static void main(String[] args) {
		SpringApplication.from(GymSearchApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
