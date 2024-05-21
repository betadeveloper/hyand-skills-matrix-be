package com.skillsmatrixapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class SkillsMatrixApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkillsMatrixApplication.class, args);
	}

}
