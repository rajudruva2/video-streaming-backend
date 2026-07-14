package com.videomoderation.video_moderation_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI streamNestApi() {

        return new OpenAPI()
                .info(new Info()
                        .title("StreamNest Moderation API")
                        .description("Content moderation platform for video uploads")
                        .version("v1.0.0"));
    }
}
