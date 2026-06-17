package com.gitee.swsk33.gitdocument;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({GitDocumentAuthApp.class, GitDocumentCommonApp.class, GitDocumentCommonApp.class, GitDocumentGitApp.class, GitDocumentSystemApp.class})
public class GitDocumentApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitDocumentApplication.class, args);
	}

}