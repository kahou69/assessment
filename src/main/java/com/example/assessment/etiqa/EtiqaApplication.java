package com.example.assessment.etiqa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EtiqaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EtiqaApplication.class, args);
	}

}
