package com.accenture.academico.Acc.Bank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Acc Bank - API")
                .version("v1.0")
                .description("Documentação da API da Acc Bank.")
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://springdoc.org")
                ))
            .externalDocs(new ExternalDocumentation()
                    .description("Documentação Adicional da API")
                    .url("https://api.accbankbrasil.com.br"));
    }
    
}