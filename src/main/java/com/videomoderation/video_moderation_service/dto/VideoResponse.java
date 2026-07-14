package com.videomoderation.video_moderation_service.dto;

import com.videomoderation.video_moderation_service.enums.VideoStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record VideoResponse(
        UUID videoId,

        String originalFileName,

        VideoStatus status,

        String moderationReason,

        String rejectedFrame,

        LocalDateTime processedAt,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
