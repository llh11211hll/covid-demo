package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CovidInlandDemoApplication //注意：所有控制器写到/controller目录下
{

	public static void main(String[] args) {
		SpringApplication.run(CovidInlandDemoApplication.class, args);
	}

}
