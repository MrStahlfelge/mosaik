package org.ergoplatform.mosaik.backenddemo;

import org.ergoplatform.mosaik.serialization.MosaikSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendDemoApplication {
	private MosaikSerializer mosaikSerializer = new MosaikSerializer();

	public static void main(String[] args) {
		SpringApplication.run(BackendDemoApplication.class, args);
	}

	@Bean
	public MosaikSerializer getMosaikSerializer() {
		return mosaikSerializer;
	}
}
