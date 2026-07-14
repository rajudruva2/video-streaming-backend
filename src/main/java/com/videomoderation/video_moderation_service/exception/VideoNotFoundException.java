package com.videomoderation.video_moderation_service.exception;

import java.util.UUID;

public class VideoNotFoundException extends RuntimeException {

    public VideoNotFoundException(UUID videoId) {
        super("Video not found: " + videoId);
    }
}
