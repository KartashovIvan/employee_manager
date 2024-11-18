package ru.kartashov.employee_manager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@AllArgsConstructor
public class SwaggerConfig {

    private ServerProperties serverProperties;

    @Bean
    public OpenAPI init() {
        Server afishaServer = new Server();
        afishaServer.setUrl("http://localhost:%s".formatted(serverProperties.getPort()));

        Info info = new Info();
        info.title("Employee manager");
        info.version("1.0");
        info.description("Сервис по управлению списком сотрудников");

        return new OpenAPI().info(info).servers(List.of(afishaServer));
    }
}
