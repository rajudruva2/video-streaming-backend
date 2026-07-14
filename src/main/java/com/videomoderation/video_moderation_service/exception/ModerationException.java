package com.videomoderation.video_moderation_service.exception;

public class ModerationException extends RuntimeException {

    public ModerationException(String message) {
        super(message);
    }

    public ModerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
