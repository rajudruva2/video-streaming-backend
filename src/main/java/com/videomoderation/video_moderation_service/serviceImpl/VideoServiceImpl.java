package com.videomoderation.video_moderation_service.serviceImpl;


import com.videomoderation.video_moderation_service.config.AppProperties;
import com.videomoderation.video_moderation_service.dto.UploadResponse;
import com.videomoderation.video_moderation_service.entity.Video;
import com.videomoderation.video_moderation_service.enums.VideoStatus;
import com.videomoderation.video_moderation_service.repository.VideoRepository;
import com.videomoderation.video_moderation_service.service.FrameExtractorService;
import com.videomoderation.video_moderation_service.service.VideoProcessingService;
import com.videomoderation.video_moderation_service.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final AppProperties appProperties;
    private final VideoProcessingService videoProcessingService;

    @Override
    public UploadResponse uploadVideo(MultipartFile file) {
        // Validate file
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File cannot be empty.");
        }

        // Create upload directory if it doesn't exist
        Path uploadDir = createUploadDirectory();

        // Get original filename
        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null || originalFileName.isBlank()) {
            throw new RuntimeException("Invalid file name.");
        }

        // Get file extension
        String extension = "";

        int index = originalFileName.lastIndexOf(".");

        if (index > 0) {
            extension = originalFileName.substring(index);
        }

        // Generate unique filename
        String storedFileName = UUID.randomUUID() + extension;

        // Destination path
        Path destination = uploadDir.resolve(storedFileName);

        // Copy file
        try {
            Files.copy(
                    file.getInputStream(),
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to store uploaded file.", e);
        }

        // Save metadata
         Video video = Video.builder()
                .originalFileName(originalFileName)
                .storedFileName(storedFileName)
                .filePath(destination.toString())
                .status(VideoStatus.UPLOADED)
                .build();

        video = videoRepository.save(video);

        videoProcessingService.processVideo(video.getId());

        // Return response
        return new UploadResponse(
                video.getId(),
                video.getStatus()
        );
    }

    private Path createUploadDirectory() {

        Path uploadDir = Paths.get(appProperties.getUploadPath());

        try {

            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

        } catch (IOException e) {
            throw new RuntimeException("Unable to create upload directory.", e);
        }

        return uploadDir;
    }
}
