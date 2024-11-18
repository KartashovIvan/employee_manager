package ru.kartashov.employee_manager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI init() {
        Info info = new Info();
        info.title("Employee manager");
        info.version("1.0");
        info.description("Сервис по управлению списком сотрудников");

        return new OpenAPI().info(info);
    }
}
