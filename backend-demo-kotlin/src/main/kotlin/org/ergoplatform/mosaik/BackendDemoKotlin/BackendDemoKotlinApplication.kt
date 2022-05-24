package org.ergoplatform.mosaik.BackendDemoKotlin

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@SpringBootApplication
class BackendDemoKotlinApplication {

	@Bean
	@Primary
	fun objectMapper(): ObjectMapper {
		// enables controller methods annotated with @ResponseBody to directly return
		// Mosaik Actions and elements that will get serialized by Spring automatically
		return org.ergoplatform.mosaik.jackson.MosaikSerializer.getMosaikMapper()
	}
}

fun main(args: Array<String>) {
	runApplication<BackendDemoKotlinApplication>(*args)
}

const val APP_VERSION = 1

