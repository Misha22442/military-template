package ua.edu.viti.military.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Система управління військовим транспортом")
                        .version("1.0.0")
                        .description("""
                                REST API для управління військовим транспортом, водіями та категоріями транспорту.
                                
                                ## Основні можливості:
                                - **Категорії транспорту** - управління типами військової техніки
                                - **Транспортні засоби** - облік та моніторинг технічного стану
                                - **Водії** - реєстр водіїв з відстеженням посвідчень
                                
                                ## Бізнес-логіка:
                                - Валідація сумісності категорії прав водія та типу транспорту
                                - Автоматичне відстеження технічного обслуговування
                                - Контроль термінів дії посвідчень водіїв
                                """)
                        .contact(new Contact()
                                .name("Military Transport System")
                                .email("support@military-transport.ua")
                                .url("https://github.com/military-transport"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Локальний сервер розробки")
                ));
    }
}
