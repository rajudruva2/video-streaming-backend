package com.videomoderation.video_moderation_service.service;

import com.videomoderation.video_moderation_service.entity.Video;

import java.nio.file.Path;

public interface FrameExtractorService {

    Path extractFrames(Video video);
}
