package com.Gyro.back_end_gyro.domain.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final S3Client s3Client;

    private final String bucketName = "gyro-aws-bucket";


    public String saveImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("O arquivo enviado está vazio");
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(this.bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            this.s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            return getImageUrl(fileName);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar a imagem no S3", e);
        }
    }


    private String getImageUrl(String fileName) {
        return String.format("https://%s.s3.amazonaws.com/%s", this.bucketName, fileName);
    }
}
