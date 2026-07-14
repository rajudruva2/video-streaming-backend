package com.videomoderation.video_moderation_service.serviceImpl;

import com.videomoderation.video_moderation_service.entity.Video;
import com.videomoderation.video_moderation_service.enums.VideoStatus;
import com.videomoderation.video_moderation_service.exception.VideoNotFoundException;
import com.videomoderation.video_moderation_service.model.VideoModerationResult;
import com.videomoderation.video_moderation_service.repository.VideoRepository;
import com.videomoderation.video_moderation_service.service.FrameExtractorService;
import com.videomoderation.video_moderation_service.service.FrameModerationService;
import com.videomoderation.video_moderation_service.service.VideoProcessingService;
import com.videomoderation.video_moderation_service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoProcessingServiceImpl implements VideoProcessingService {

    private final VideoRepository videoRepository;
    private final FrameExtractorService frameExtractorService;
    private final FrameModerationService frameModerationService;
    private final StorageService storageService;

    @Async
    @Override
    public void processVideo(UUID videoId) {

        Video video = videoRepository.findById(videoId)
                .orElseThrow(() ->
                        new VideoNotFoundException(videoId));

        Path localVideo = null;

        try {
            log.info("Processing started for {}", videoId);

            video.setStatus(VideoStatus.PROCESSING);
            videoRepository.save(video);

            localVideo = storageService.download(video.getFilePath());

            Path frameDirectory =
                    frameExtractorService.extractFrames(
                            video,
                            localVideo
                    );

            log.info("Frame extraction completed for {}", videoId);

            VideoModerationResult moderationResult =
                    frameModerationService.moderate(video, frameDirectory);

            video.setStatus(moderationResult.status());
            video.setModerationReason(moderationResult.reason());

            if (moderationResult.rejectedFrame() != null) {
                video.setRejectedFrame(
                        moderationResult.rejectedFrame()
                                .getFileName()
                                .toString()
                );
            }

            video.setProcessedAt(LocalDateTime.now());
            videoRepository.save(video);

            log.info(
                    "Processing completed for {} with status {}",
                    videoId,
                    moderationResult.status()
            );
        }catch (Exception ex) {

            log.error("Processing failed for {}", videoId, ex);

            video.setStatus(VideoStatus.FAILED);
            video.setModerationReason(ex.getMessage());
            video.setProcessedAt(LocalDateTime.now());

            videoRepository.save(video);
        }finally {

            if (localVideo != null) {
                try {
                    Files.deleteIfExists(localVideo);
                } catch (IOException e) {
                    log.warn("Failed to delete temporary file {}", localVideo);
                }
            }
        }
    }
}
