package dev.api.utils;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import static java.io.File.separator;
import static java.lang.System.currentTimeMillis;

@Service
public class FileStorageService {

    public String storeFileInLocaleStorage(MultipartFile file) throws IOException {
        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String fileUploadPath = System.getProperty("user.dir") + "/uploads";
        Path uploadPath = Path.of(fileUploadPath);
        Path filePath = uploadPath.resolve(uniqueFileName);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    public String deleteFile(String imageDirectory, String file) throws IOException {
        Path filePath = Path.of( System.getProperty("user.dir") + "/uploads", file);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
            return "Success";
        } else {
            return "Failed"; // Handle missing images
        }
    }
}
