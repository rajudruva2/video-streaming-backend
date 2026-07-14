package com.videomoderation.video_moderation_service.serviceImpl;

import com.videomoderation.video_moderation_service.exception.ValidationException;
import com.videomoderation.video_moderation_service.service.FileValidationService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Service
public class FileValidationServiceImpl implements FileValidationService {

    private static final long MAX_FILE_SIZE = 100 * 1024 * 1024; //100 MB

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "mp4",
            "mov",
            "avi",
            "mkv",
            "webm"
    );

    @Override
    public void validate(MultipartFile file) {

        validateEmptyFile(file);

        validateFileSize(file);

        validateExtension(file);
    }

    private void validateEmptyFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new ValidationException("Uploaded file is empty.");
        }
    }

    private void validateFileSize(MultipartFile file) {

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ValidationException("Maximum upload size is 100 MB.");
        }
    }

    private void validateExtension(MultipartFile file) {

        String fileName = file.getOriginalFilename();

        if (fileName == null || !fileName.contains(".")) {
            throw new ValidationException("Invalid file name.");
        }

        String extension = fileName.substring(fileName.lastIndexOf('.') + 1)
                .toLowerCase();

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new ValidationException(
                    "Unsupported file type. Allowed types: "
                            + String.join(", ", ALLOWED_EXTENSIONS)
            );
        }
    }
}
