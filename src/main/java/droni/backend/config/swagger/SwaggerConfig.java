package droni.backend.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Value("${droni.openapi-version}")
    private String openapiVersion;

    private static final String securitySchemeName = "bearer-key";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(securitySchemes())
                .info(getInfo())
                .security(List.of(new SecurityRequirement().addList(securitySchemeName)));
    }

    private Components securitySchemes() {
        return new Components()
                .addSecuritySchemes(securitySchemeName, new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"));
    }

    private Info getInfo() {
        return new Info().title("Droni Backend API").version(openapiVersion);
    }
}
