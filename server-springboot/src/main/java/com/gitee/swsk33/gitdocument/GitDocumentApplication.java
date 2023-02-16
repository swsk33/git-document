package com.gitee.swsk33.gitdocument;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class GitDocumentApplication {

	public static void main(String[] args) {
		System.setProperty("spring.config.location", "file:config/");
		SpringApplication.run(GitDocumentApplication.class, args);
	}

}