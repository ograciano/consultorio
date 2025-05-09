package com.clinic.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI para la documentación Swagger.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Medical Appointment API")
                        .version("1.0.0")
                        .description("Sistema de gestión de citas médicas para consultorios.")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("dev@clinic.com")
                                .url("https://clinic.com")));
    }
}
