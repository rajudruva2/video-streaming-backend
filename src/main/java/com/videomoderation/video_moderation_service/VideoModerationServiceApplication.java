package com.videomoderation.video_moderation_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class VideoModerationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoModerationServiceApplication.class, args);
	}

}
