package api.dev.authentication.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class SigninDto {
     
    @Email(message = "invalid email")
    private String email;

    
    @NotEmpty(message = "password shouldn't be empty")
    @NotNull(message = "password shouldn't be null")
    @NotBlank(message = "password shouldn't be empty")// 
    private String password;


    public String getEmail() {
        return email;
    }


    public String getPassword() {
        return password;
    }
    
    
}
