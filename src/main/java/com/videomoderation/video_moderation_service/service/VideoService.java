package com.videomoderation.video_moderation_service.service;

import com.videomoderation.video_moderation_service.dto.UploadResponse;
import com.videomoderation.video_moderation_service.dto.VideoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface VideoService {
    UploadResponse uploadVideo(MultipartFile file);
    VideoResponse getVideo(UUID videoId);
}
