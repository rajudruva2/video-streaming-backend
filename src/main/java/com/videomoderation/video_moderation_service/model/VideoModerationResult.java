package com.videomoderation.video_moderation_service.model;

import com.videomoderation.video_moderation_service.enums.VideoStatus;

import java.nio.file.Path;

public record VideoModerationResult(
        VideoStatus status,

        String reason,

        Path rejectedFrame
) {
}
