package com.videomoderation.video_moderation_service.storage;

import lombok.Builder;

@Builder
public record StoredFile(
        String originalFileName,

        String storedFileName,

        String storageKey
) {
}
