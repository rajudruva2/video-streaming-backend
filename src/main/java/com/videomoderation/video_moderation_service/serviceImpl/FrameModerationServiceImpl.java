package com.videomoderation.video_moderation_service.serviceImpl;

import com.videomoderation.video_moderation_service.client.ModerationClient;
import com.videomoderation.video_moderation_service.decision.DecisionEngine;
import com.videomoderation.video_moderation_service.enums.VideoStatus;
import com.videomoderation.video_moderation_service.exception.ModerationException;
import com.videomoderation.video_moderation_service.model.ModerationResult;
import com.videomoderation.video_moderation_service.entity.Video;
import com.videomoderation.video_moderation_service.model.VideoModerationResult;
import com.videomoderation.video_moderation_service.service.FrameModerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class FrameModerationServiceImpl implements FrameModerationService {

    private final ModerationClient moderationClient;
    private final DecisionEngine decisionEngine;

    @Override
    public VideoModerationResult moderate(Video video, Path frameDirectory) {
        log.info("Moderating frames for {}", video.getId());

        try (Stream<Path> stream = Files.list(frameDirectory)) {

            List<Path> frames = stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".jpg"))
                    .sorted()
                    .toList();

            // process frames
            for (Path frame : frames) {

                ModerationResult result = moderationClient.scan(frame);

                log.info("{} -> {}", frame.getFileName(), result);

                if (decisionEngine.shouldReject(result)) {
                    log.warn("Video {} rejected because of frame {}",
                            video.getId(),
                            frame.getFileName());

                    return new VideoModerationResult(
                            VideoStatus.REJECTED,
                            "Unsafe content detected",
                            frame
                    );
                }
            }

        } catch (IOException e) {
            throw new ModerationException("Unable to read frame directory", e);
        }

        return new VideoModerationResult(
                VideoStatus.APPROVED,
                "No unsafe content detected",
                null
        );
    }
}
