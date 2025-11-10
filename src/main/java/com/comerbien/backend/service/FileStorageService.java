package com.comerbien.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    // Railway inyectará esta ruta via variable de entorno
    @Value("${file.upload.directory:/data/uploads}")
    private String uploadDir;

    public String storeFile(MultipartFile file) throws IOException {
        // Crear directorio si no existe
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            System.out.println("✅ Created upload directory: " + uploadPath.toAbsolutePath());
        }

        // Generar nombre único para el archivo
        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        String fileName = UUID.randomUUID().toString() + fileExtension;
        Path filePath = uploadPath.resolve(fileName);

        // Guardar archivo
        Files.copy(file.getInputStream(), filePath);

        System.out.println("✅ File saved: " + filePath.toAbsolutePath());
        return fileName;
    }

    public byte[] loadFile(String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + fileName);
        }
        return Files.readAllBytes(filePath);
    }

    public void deleteFile(String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        Files.deleteIfExists(filePath);
    }

    // Método para verificar la configuración
    public String getStorageInfo() {
        Path path = Paths.get(uploadDir);
        return String.format("Upload directory: %s (exists: %s)",
                path.toAbsolutePath(), Files.exists(path));
    }
}
// @Service
// public class FileStorageService {

// @Value("${file.upload.directory:/data/uploads}}")
// private String uploadDir;

// public String storeFile(MultipartFile file) throws IOException {
// // Crear directorio si no existe
// Path uploadPath = Paths.get(uploadDir);
// if (!Files.exists(uploadPath)) {
// Files.createDirectories(uploadPath);
// }

// // Generar nombre único para el archivo
// String originalFileName = file.getOriginalFilename();
// String fileExtension = "";
// if (originalFileName != null && originalFileName.contains(".")) {
// fileExtension =
// originalFileName.substring(originalFileName.lastIndexOf("."));
// }

// String fileName = UUID.randomUUID().toString() + fileExtension;
// Path filePath = uploadPath.resolve(fileName);

// // Guardar archivo
// Files.copy(file.getInputStream(), filePath);

// return "/uploads/" + fileName; // Ruta relativa para acceder desde el
// frontend
// }

// public void deleteFile(String filePath) throws IOException {
// if (filePath != null && filePath.startsWith("/uploads/")) {
// String fileName = filePath.substring("/uploads/".length());
// Path path = Paths.get(uploadDir, fileName);
// if (Files.exists(path)) {
// Files.delete(path);
// }
// }
// }
// }