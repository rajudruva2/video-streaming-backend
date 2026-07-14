package com.videomoderation.video_moderation_service.storage;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {
    StoredFile store(MultipartFile file);

    void delete(Path file);

    Path download(String storageKey);
}
