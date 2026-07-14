package com.videomoderation.video_moderation_service.client;

import com.videomoderation.video_moderation_service.model.ModerationResult;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class MockModerationClient implements ModerationClient {
    @Override
    public ModerationResult scan(Path frame) {
        String fileName = frame.getFileName().toString();

        if (fileName.equals("frame_0003.jpg")) {

            return new ModerationResult(
                    true,
                    0.96,
                    0.02,
                    0.00,
                    "Adult content detected"
            );
        }

        return new ModerationResult(
                false,
                0.02,
                0.01,
                0.00,
                "SAFE"
        );
    }
}
