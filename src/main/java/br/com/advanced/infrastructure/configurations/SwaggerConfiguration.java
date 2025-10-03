package br.com.advanced.infrastructure.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Clientes")
                        .version("1.0.0")
                        .description("API para gerenciamento de clientes, criada no curso Java Avançado – Formação Arquiteto")
                        .contact(new Contact()
                                .name("Alisson")
                                .email("adevpr11@gmail.com")
                                .url("https://www.github.com/a-devrepo")));
    }
}
