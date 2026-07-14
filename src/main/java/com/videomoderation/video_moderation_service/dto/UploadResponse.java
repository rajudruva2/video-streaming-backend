package com.videomoderation.video_moderation_service.dto;

import com.videomoderation.video_moderation_service.enums.VideoStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponse {
    UUID videoId;
    VideoStatus status;
}
