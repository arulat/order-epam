package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI ordersOpenApi() {
    return new OpenAPI()
        .info(new Info()
            .title("Orders API")
            .description("Orders Management API with pagination and filtering")
            .version("v1")
            .contact(new Contact()
                .name("Orders Team")
                .email("orders@example.com"))
            .license(new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
  }
}

