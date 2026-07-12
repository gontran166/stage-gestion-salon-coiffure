package com.gestionSalon.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService{

    private static final String UPLOAD_DIR =
            "uploads/prestations/";

    public String uploadPrestationImage(
            MultipartFile file,
            Long prestationId
    ) {

        try {

            Path uploadPath =
                    Paths.get(UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename =
                    file.getOriginalFilename();

            String extension = "";

            if (originalFilename != null
                    && originalFilename.contains(".")) {

                extension =
                        originalFilename.substring(
                                originalFilename.lastIndexOf(".")
                        );
            }

            String fileName =
                    prestationId
                            + "_"
                            + UUID.randomUUID()
                            + extension;

            Path destination =
                    uploadPath.resolve(fileName);

            Files.copy(
                    file.getInputStream(),
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return "/uploads/prestations/" + fileName;

        } catch (IOException e) {

            throw new RuntimeException(
                    "Erreur lors de l'upload de l'image."
            );
        }
    }

    public void deleteFile(String imageUrl) {

        if (imageUrl == null || imageUrl.isBlank()) {
            return;
        }

        try {

            String relativePath =
                    imageUrl.replaceFirst("^/", "");

            Path filePath =
                    Paths.get(relativePath);

            Files.deleteIfExists(filePath);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Erreur lors de la suppression du fichier."
            );
        }
    }
}