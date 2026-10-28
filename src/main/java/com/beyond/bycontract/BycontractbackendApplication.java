package com.beyond.bycontract;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BycontractbackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BycontractbackendApplication.class, args);
	}

}
