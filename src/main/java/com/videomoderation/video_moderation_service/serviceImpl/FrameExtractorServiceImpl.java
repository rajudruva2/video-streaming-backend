package com.videomoderation.video_moderation_service.serviceImpl;

import com.videomoderation.video_moderation_service.config.AppProperties;
import com.videomoderation.video_moderation_service.entity.Video;
import com.videomoderation.video_moderation_service.service.FrameExtractorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Slf4j
public class FrameExtractorServiceImpl implements FrameExtractorService {
    private final AppProperties appProperties;

    @Override
    public Path extractFrames(Video video) {
        try {

            Path frameDirectory = Paths.get(
                    appProperties.getUploadPath(),
                    "frames",
                    video.getId().toString()
            );

            Files.createDirectories(frameDirectory);

            ProcessBuilder builder = new ProcessBuilder(

                    appProperties.getFfmpegPath(),

                    "-i",

                    video.getFilePath(),

                    "-vf",

                    "fps=1",

                    frameDirectory.resolve("frame_%04d.jpg").toString()

            );

            builder.redirectErrorStream(true);

            Process process = builder.start();

            int exitCode = process.waitFor();

            if (exitCode != 0) {

                throw new RuntimeException("FFmpeg failed with exit code " + exitCode);

            }

            log.info("Frames extracted successfully : {}", frameDirectory);

            return frameDirectory;

        } catch (IOException | InterruptedException e) {

            throw new RuntimeException("Frame extraction failed", e);

        }

    }
}
