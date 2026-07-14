package com.videomoderation.video_moderation_service.exception;

public class FrameExtractionException extends RuntimeException {

    public FrameExtractionException(String message) {
        super(message);
    }

    public FrameExtractionException(String message, Throwable cause) {
        super(message, cause);
    }
}
