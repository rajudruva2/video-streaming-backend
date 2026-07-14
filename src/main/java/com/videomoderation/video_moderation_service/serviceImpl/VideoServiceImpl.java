package com.videomoderation.video_moderation_service.serviceImpl;

import com.videomoderation.video_moderation_service.dto.UploadResponse;
import com.videomoderation.video_moderation_service.dto.VideoResponse;
import com.videomoderation.video_moderation_service.entity.Video;
import com.videomoderation.video_moderation_service.enums.VideoStatus;
import com.videomoderation.video_moderation_service.exception.VideoNotFoundException;
import com.videomoderation.video_moderation_service.repository.VideoRepository;
import com.videomoderation.video_moderation_service.service.FileValidationService;
import com.videomoderation.video_moderation_service.service.VideoProcessingService;
import com.videomoderation.video_moderation_service.service.VideoService;
import com.videomoderation.video_moderation_service.storage.StorageService;
import com.videomoderation.video_moderation_service.storage.StoredFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final StorageService storageService;
    private final VideoProcessingService videoProcessingService;
    private final FileValidationService fileValidationService;

    @Override
    public UploadResponse uploadVideo(MultipartFile file) {

        // Validate file
        fileValidationService.validate(file);

        StoredFile storedFile = storageService.store(file);

        // Save metadata
        Video video = Video.builder()
                .originalFileName(storedFile.originalFileName())
                .storedFileName(storedFile.storedFileName())
                .filePath(storedFile.storageKey())
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

    @Override
    public VideoResponse getVideo(UUID videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() ->
                        new VideoNotFoundException(videoId));

        return new VideoResponse(
                video.getId(),
                video.getOriginalFileName(),
                video.getStatus(),
                video.getModerationReason(),
                video.getRejectedFrame(),
                video.getProcessedAt(),
                video.getCreatedAt(),
                video.getUpdatedAt()
        );
    }
}
