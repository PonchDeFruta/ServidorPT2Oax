package com.proyecto.servidorpt2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServidorPt2Application {

	public static void main(String[] args) {
		SpringApplication.run(ServidorPt2Application.class, args);
	}

	public String getGreeting(String name) {
		if (name == null || name.isEmpty()) {
			return "Hello, Stranger!";
		}
		return "Hello, " + name + "!";
	}


}
