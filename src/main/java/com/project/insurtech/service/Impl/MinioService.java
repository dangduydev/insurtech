package com.project.insurtech.service.Impl;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.BucketExistsArgs; // Nhập khẩu BucketExistsArgs
import io.minio.MakeBucketArgs; // Nhập khẩu MakeBucketArgs
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    private String extractFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot > 0 && lastIndexOfDot < fileName.length() - 1) {
            return fileName.substring(lastIndexOfDot);
        }
        return "";
    }

    public String uploadFile(MultipartFile file) throws Exception {
        // Kiểm tra nếu bucket không tồn tại và tạo mới
        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!isExist) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build()); // Sử dụng MakeBucketArgs
        }
        // Upload file
        try {
            String fileName = UUID.randomUUID().toString() + extractFileExtension(file.getOriginalFilename());
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName) // Generate a unique file name
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

            return fileName;
        } catch (IOException e) {
            throw new Exception("Error uploading file: " + e.getMessage());
        }
    }
}
