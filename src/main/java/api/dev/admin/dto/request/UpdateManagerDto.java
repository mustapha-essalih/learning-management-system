package api.dev.admin.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UpdateManagerDto {
    @Email(message = "invalid email")
    private String email;

    @NotEmpty(message = "full name shouldn't be empty")
    @NotNull(message = "full name shouldn't be null")
    @NotBlank(message = "full name  shouldn't be empty")// 
    private String fullName;
    
    @NotEmpty(message = "password shouldn't be empty")
    @NotNull(message = "password shouldn't be null")
    @NotBlank(message = "password shouldn't be empty")// 
    private String password;

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }


    

}
