package com.hk.fintech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class FintechEdu20231213Application {

	public static void main(String[] args) {
		SpringApplication.run(FintechEdu20231213Application.class, args);
	}

}
