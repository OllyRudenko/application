package ua.ollyrudenko.application.universities;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@OpenAPIDefinition(info = @io.swagger.v3.oas.annotations.info.Info(title = "UNIVERSITIES", version = "v1"))
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class UniversitySpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniversitySpringBootApplication.class, args);
	}

	@Bean
	public GroupedOpenApi publicApi() {
		return GroupedOpenApi.builder().group("Spring Boot REST").pathsToMatch("/rest/universities/**").build();
	}

}