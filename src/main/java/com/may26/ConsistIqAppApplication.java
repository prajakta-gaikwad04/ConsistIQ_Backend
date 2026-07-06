package com.may26;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@SpringBootApplication
public class ConsistIqAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsistIqAppApplication.class, args);
	}

}
