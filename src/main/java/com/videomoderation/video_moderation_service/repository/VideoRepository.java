package com.videomoderation.video_moderation_service.repository;

import com.videomoderation.video_moderation_service.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VideoRepository extends JpaRepository<Video, UUID> {
}
