package uk.co.prometheux.prometheuxreasoner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This is the main class of Prometheux Reasoner
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class VadalogParallelApp {

	public static void main(String[] args) {
		try {
			SpringApplication.run(VadalogParallelApp.class, args);
		} catch (Throwable e) {
            e.printStackTrace();  // Print the stack trace for more details
			throw new PrometheuxRuntimeException(e.getMessage());
		}
	}

	@Configuration
	public class CorsConfig implements WebMvcConfigurer {

		@Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/evaluateFromRepo").allowedOrigins("*");
		}

	}

}
