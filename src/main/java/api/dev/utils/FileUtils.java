package api.dev.utils;


 
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class FileUtils {

    public static byte[] returnFileFromStorage(String fileUrl) {
        if (fileUrl.isBlank()) {
            return null;
        }
        try {
            Path filePath = new File(fileUrl).toPath();
            
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("file not found");
        }
    }
}
