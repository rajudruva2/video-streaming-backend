package com.videomoderation.video_moderation_service.service;

import com.videomoderation.video_moderation_service.entity.Video;

import java.util.UUID;

public interface  VideoProcessingService {
    void processVideo(UUID videoId);
}
