package com.videomoderation.video_moderation_service.model;

public record ModerationResult(
        boolean rejected,
        double adultScore,
        double violenceScore,
        double drugsScore,
        String reason
) {
}
