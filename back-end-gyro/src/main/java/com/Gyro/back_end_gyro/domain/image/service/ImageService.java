package com.Gyro.back_end_gyro.domain.image.service;

import com.Gyro.back_end_gyro.domain.image.dto.ImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final String IMAGE_DIRECTORY = "/app/infra/bucket";

    public String saveImage(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("o arquivo enviado está vazio");
            }
            Files.createDirectories(Paths.get(IMAGE_DIRECTORY));
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(IMAGE_DIRECTORY, fileName);
            Files.write(path, file.getBytes());
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ImageResponse loadImage(String fileName) {
        try {
            Path path = Paths.get(IMAGE_DIRECTORY, fileName);
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("Imagem não encontrada ou não pode ser lida");
            }

            MediaType contentType = determineContentType(fileName);

            return new ImageResponse(resource, contentType);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao recuperar a imagem", e);
        }
    }



    private MediaType determineContentType(String fileName) {
        if (fileName.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (fileName.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

}
