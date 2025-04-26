package org.pavelvachacz.yetanotherweatherapp.configurations;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI weatherAppOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Yet Another Weather App API")
                        .description("API for managing cities and weather information")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Pavel Vacha")
                                .email("pavel.vacha@tul.cz")));
    }
}