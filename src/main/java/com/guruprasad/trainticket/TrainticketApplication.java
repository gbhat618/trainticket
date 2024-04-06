package com.guruprasad.trainticket;

import com.guruprasad.trainticket.snippets.StaticCodeSnippets;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrainticketApplication {

	public static void main(String[] args) {
		StaticCodeSnippets.setLiquibaseNetUtilsLocalHost();
		SpringApplication.run(TrainticketApplication.class, args);
	}

}
