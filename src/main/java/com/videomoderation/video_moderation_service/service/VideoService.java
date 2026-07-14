package com.videomoderation.video_moderation_service.service;

import com.videomoderation.video_moderation_service.dto.UploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface VideoService {
    UploadResponse uploadVideo(MultipartFile file);
}
