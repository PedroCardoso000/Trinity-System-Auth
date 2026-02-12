package com.trinity.manneger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MannegerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MannegerApplication.class, args);
		System.out.println("----------------------");
		System.out.println("| Trinity Auth |");
		System.out.println("----------------------");
	}

}
