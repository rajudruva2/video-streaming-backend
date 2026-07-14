package com.videomoderation.video_moderation_service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "aws")
public class AwsProperties {

    private String region;

    private String accessKey;

    private String secretKey;

    private String bucketName;

}
