package ms.parade.interfaces.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme apiKeyScheme = new SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .name("Authorization")
            .in(SecurityScheme.In.HEADER)
            .description("UUID 인증을 위한 Authorization 헤더");

        return new OpenAPI()
            .components(new Components().addSecuritySchemes("apiKeyScheme", apiKeyScheme))
            .addSecurityItem(new SecurityRequirement().addList("apiKeyScheme"))
            .info(new Info().title("Parade API").version("1.0").description("콘서트 좌석 예약 API"));
    }
}