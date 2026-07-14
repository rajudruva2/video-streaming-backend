package com.videomoderation.video_moderation_service.decision;

import com.videomoderation.video_moderation_service.model.ModerationResult;
import org.springframework.stereotype.Component;

@Component
public class DecisionEngine {

    public boolean shouldReject(ModerationResult result) {

        return result.adultScore() >= 0.85
                || result.violenceScore() >= 0.80
                || result.drugsScore() >= 0.80;
    }
}
