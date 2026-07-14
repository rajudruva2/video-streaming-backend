package com.videomoderation.video_moderation_service.serviceImpl;

import com.videomoderation.video_moderation_service.entity.Video;
import com.videomoderation.video_moderation_service.enums.VideoStatus;
import com.videomoderation.video_moderation_service.model.VideoModerationResult;
import com.videomoderation.video_moderation_service.repository.VideoRepository;
import com.videomoderation.video_moderation_service.service.FrameExtractorService;
import com.videomoderation.video_moderation_service.service.FrameModerationService;
import com.videomoderation.video_moderation_service.service.VideoProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoProcessingServiceImpl implements VideoProcessingService {

    private final VideoRepository videoRepository;
    private final FrameExtractorService frameExtractorService;
    private final FrameModerationService frameModerationService;

    @Async
    @Override
    public void processVideo(UUID videoId) {

        Video video = videoRepository.findById(videoId)
                .orElseThrow(() ->
                        new RuntimeException("Video not found : " + videoId));

        try {
            log.info("Processing started for {}", videoId);

            video.setStatus(VideoStatus.PROCESSING);
            videoRepository.save(video);

            Path frameDirectory =
                    frameExtractorService.extractFrames(video);

            log.info("Frame extraction completed for {}", videoId);

            VideoModerationResult moderationResult =
                    frameModerationService.moderate(video, frameDirectory);

            video.setStatus(moderationResult.status());

            videoRepository.save(video);

            log.info(
                    "Processing completed for {} with status {}",
                    videoId,
                    moderationResult.status()
            );
        }catch (Exception ex) {

            log.error("Processing failed for {}", videoId, ex);

            video.setStatus(VideoStatus.FAILED);

            videoRepository.save(video);
        }
    }
}
