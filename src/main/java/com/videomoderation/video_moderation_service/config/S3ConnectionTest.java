package com.videomoderation.video_moderation_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;

@Component
@RequiredArgsConstructor
public class S3ConnectionTest implements CommandLineRunner {

    private final S3Client s3Client;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("===== S3 Buckets =====");

        for (Bucket bucket : s3Client.listBuckets().buckets()) {
            System.out.println(bucket.name());
        }

        System.out.println("======================");

    }
}
