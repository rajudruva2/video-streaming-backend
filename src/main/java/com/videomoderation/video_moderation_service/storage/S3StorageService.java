package com.videomoderation.video_moderation_service.storage;

import com.videomoderation.video_moderation_service.config.AwsProperties;
import com.videomoderation.video_moderation_service.exception.StorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3StorageService implements StorageService {

    private final S3Client s3Client;
    private final AwsProperties awsProperties;

    @Override
    public StoredFile store(MultipartFile file) {
        try {

            String originalFileName =
                    Objects.requireNonNull(file.getOriginalFilename());

            String extension = "";

            int dotIndex = originalFileName.lastIndexOf('.');

            if (dotIndex != -1) {
                extension = originalFileName.substring(dotIndex);
            }

            String storedFileName = UUID.randomUUID() + extension;

            String objectKey = "videos/" + storedFileName;

            PutObjectRequest request =
                    PutObjectRequest.builder()
                            .bucket(awsProperties.getBucketName())
                            .key(objectKey)
                            .contentType(file.getContentType())
                            .build();

            s3Client.putObject(
                    request,
                    RequestBody.fromBytes(file.getBytes())
            );

            return StoredFile.builder()
                    .originalFileName(originalFileName)
                    .storedFileName(storedFileName)
                    .storageKey(objectKey)
                    .build();

        } catch (IOException ex) {

            throw new StorageException(
                    "Unable to upload file to S3",
                    ex
            );
        }
    }

    @Override
    public void delete(Path file) {

        DeleteObjectRequest request =
                DeleteObjectRequest.builder()
                        .bucket(awsProperties.getBucketName())
                        .key(String.valueOf(file))
                        .build();

        s3Client.deleteObject(request);

    }

    @Override
    public Path download(String objectKey) {

        try {

            String extension = objectKey.substring(objectKey.lastIndexOf("."));

            Path tempFile = Files.createTempFile(
                    "video-",
                    extension
            );

            GetObjectRequest request =
                    GetObjectRequest.builder()
                            .bucket(awsProperties.getBucketName())
                            .key(objectKey)
                            .build();

            ResponseInputStream<GetObjectResponse> inputStream =
                    s3Client.getObject(request);

            Files.copy(
                    inputStream,
                    tempFile,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return tempFile;

        } catch (IOException ex) {

            throw new StorageException(
                    "Unable to download video from S3",
                    ex
            );
        }
    }
}
