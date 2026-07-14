package com.videomoderation.video_moderation_service.service;

import com.videomoderation.video_moderation_service.entity.Video;
import com.videomoderation.video_moderation_service.model.VideoModerationResult;

import java.nio.file.Path;

public interface FrameModerationService {
    VideoModerationResult moderate(Video video, Path frameDirectory);
}
