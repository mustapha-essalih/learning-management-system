package api.dev.courses.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class GetResourceDto {

    @NotBlank(message = "should not be empty")
    @NotNull(message = "should not be empty")
    @NotEmpty(message = "should not be empty")
    private String fileName;


    @NotBlank(message = "should not be empty")
    @NotNull(message = "should not be empty")
    @NotEmpty(message = "should not be empty")
    private String contentType;


    public String getFileName() {
        return fileName;
    }


    public String getContentType() {
        return contentType;
    }

    
}
