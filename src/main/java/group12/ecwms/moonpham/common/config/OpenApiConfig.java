package group12.ecwms.moonpham.common.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI moonphamOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Moonpham API")
                        .description("API documentation for SE2 Spring Boot + Thymeleaf project")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("SE2 Team")
                                .email("team@moonpham.local"))
                        .license(new License()
                                .name("Internal Academic Use")))
                .externalDocs(new ExternalDocumentation()
                        .description("Project repository")
                        .url("https://github.com/moonphan/SE2"));
    }

    @Bean
    public GroupedOpenApi publicApiGroup() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch(
                        "/auth/**",
                        "/products/**",
                        "/sub-categories/**",
                        "/product-detail/**",
                        "/recently-viewed",
                        "/cart/**",
                        "/checkout/**",
                        "/orders/**",
                        "/payment/**"
                )
                .build();
    }
}

