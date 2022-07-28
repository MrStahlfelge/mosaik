package org.ergoplatform.mosaik.backenddemo;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.ergoplatform.mosaik.jackson.MosaikSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@SpringBootApplication
public class BackendDemoApplication {
    public static final int APP_VERSION = 1;

    public static void main(String[] args) {
        SpringApplication.run(BackendDemoApplication.class, args);
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        // enables controller methods annotated with @ResponseBody to directly return
        // Mosaik Actions and elements that will get serialized by Spring automatically
        return MosaikSerializer.getMosaikMapper();
    }
}
