package com.gestionSalon.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()

                .info(
                        new Info()
                                .title("API Gestion Salon de Coiffure")
                                .version("1.0.0")
                                .description(
                                        """
                                        API REST permettant :
                                        - L'authentification des utilisateurs
                                        - La gestion des rôles (Gérant, Prestataire, Client)
                                        - La gestion des prestations
                                        - La gestion des horaires
                                        - La prise de rendez-vous
                                        - La consultation des statistiques
                                        """
                                )
                                .contact(
                                        new Contact()
                                                .name("NOMBO W. Gontran")
                                                .email("contact@gestionsalon.com")
                                )
                )

                // Activation du JWT Bearer Token dans Swagger
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(SECURITY_SCHEME_NAME)
                )

                .schemaRequirement(
                        SECURITY_SCHEME_NAME,
                        new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                );
    }
}