package com.videomoderation.video_moderation_service.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileValidationService {
    void validate(MultipartFile file);
}
