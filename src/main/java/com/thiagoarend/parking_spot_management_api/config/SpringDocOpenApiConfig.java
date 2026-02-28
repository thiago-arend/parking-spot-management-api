package com.thiagoarend.parking_spot_management_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocOpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("REST API - Parking Spot Management")
                                .description("API for managing vehicles at a parking lot")
                                .version("v1")
                                .license(new License().name("MIT license").url("https://github.com/thiago-arend/parking-spot-management-api/blob/main/LICENSE"))
                                .contact(new Contact().name("Thiago Frazzon Arend").email("thiago.arend@gmail.com"))
                );
    }
}
