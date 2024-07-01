package dev.api.authentication.dto;

import dev.api.model.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupDto {

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



    @NotNull(message = "role shouldn't be null")
    private Roles role;

}
