package com.gitee.swsk33.gitdocument;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class GitDocumentApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitDocumentApplication.class, args);
	}

}