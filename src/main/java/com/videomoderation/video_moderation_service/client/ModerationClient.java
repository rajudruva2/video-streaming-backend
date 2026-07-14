package com.videomoderation.video_moderation_service.client;

import com.videomoderation.video_moderation_service.model.ModerationResult;

import java.nio.file.Path;

public interface ModerationClient {
    ModerationResult scan(Path frame);
}
