package com.comerbien.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.comerbien.backend.service.FileStorageService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = { "http://localhost:8080", "https://comerbien.com.mx" })
public class FileUploadController {

    private final FileStorageService fileStorageService;

    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/image")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El archivo está vacío"));
            }

            // Validar tipo de archivo
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Solo se permiten archivos de imagen"));
            }

            // Validar tamaño (max 10MB)
            if (file.getSize() > 10 * 1024 * 1024) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El archivo es demasiado grande (máximo 10MB)"));
            }

            String fileName = fileStorageService.storeFile(file);

            Map<String, String> response = new HashMap<>();
            response.put("fileName", fileName); // Cambiado de filePath a fileName
            response.put("fileUrl", "/api/files/" + fileName); // URL para acceder a la imagen
            response.put("message", "Imagen subida exitosamente");

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error al subir la imagen: " + e.getMessage()));
        }
    }
}
// public class FileUploadController {

// private final FileStorageService fileStorageService;

// public FileUploadController(FileStorageService fileStorageService) {
// this.fileStorageService = fileStorageService;
// }

// @PostMapping("/image")
// public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file")
// MultipartFile file) {
// try {
// if (file.isEmpty()) {
// return ResponseEntity.badRequest().body(Map.of("error", "El archivo está
// vacío"));
// }

// // Validar tipo de archivo
// String contentType = file.getContentType();
// if (contentType == null || !contentType.startsWith("image/")) {
// return ResponseEntity.badRequest().body(Map.of("error", "Solo se permiten
// archivos de imagen"));
// }

// // Validar tamaño (max 10MB)
// if (file.getSize() > 10 * 1024 * 1024) {
// return ResponseEntity.badRequest().body(Map.of("error", "El archivo es
// demasiado grande (máximo 10MB)"));
// }

// String filePath = fileStorageService.storeFile(file);

// Map<String, String> response = new HashMap<>();
// response.put("filePath", filePath);
// response.put("message", "Imagen subida exitosamente");

// return ResponseEntity.ok(response);

// } catch (IOException e) {
// return ResponseEntity.internalServerError().body(Map.of("error", "Error al
// subir la imagen: " + e.getMessage()));
// }
// }
// }