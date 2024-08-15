package com.accenture.academico.Acc.Bank.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class OpenAPIConfig {

	@Value("${server.url}")
    private String serverUrl;
	
    @Bean
    public OpenAPI customOpenAPI() {
    	Server server = new Server();
    	server.setUrl(serverUrl);
    	server.setDescription("Server URL");
    	
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
                    .url("https://api.accbankbrasil.com.br"))
            .tags(
                    Arrays.asList(
                            new Tag().name("Agência").description("Controlador de Agências"),
                            new Tag().name("Cliente").description("Controlador de Clientes"),
                            new Tag().name("Conta Corrente").description("Controlador de Contas Correntes"),
                            new Tag().name("Extrato").description("Controlador de Extratos")
                    )
            )
            .servers(List.of(server));
    }
}