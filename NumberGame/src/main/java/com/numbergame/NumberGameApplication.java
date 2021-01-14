package com.numbergame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class NumberGameApplication {

	public static void main(String[] args) {
		SpringApplication.run(NumberGameApplication.class, args);
	}

}
