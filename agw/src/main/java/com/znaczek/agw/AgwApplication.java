package com.znaczek.agw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.config.annotation.web.server.EnableSpringWebSession;

@SpringBootApplication
public class AgwApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgwApplication.class, args);
	}

}
