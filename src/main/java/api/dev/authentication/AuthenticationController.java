package api.dev.authentication;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.dev.authentication.dto.UserDto;
import api.dev.authentication.dto.request.SigninDto;
import api.dev.authentication.dto.request.SignupDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;




@Tag(name = "authentication", description = "User Authentication")
@CrossOrigin("*")
@RequestMapping("/api/auth")
@RestController
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;


    public AuthenticationController(AuthenticationService authenticationService) 
    {
        this.authenticationService = authenticationService;
    }


    @Operation(summary = "Register a new user", description = "Creates a new user account based on the provided information in the SignupDto object. The user's role can be either 'STUDENT' or 'INSTRUCTOR'.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successfully registration"), 
        @ApiResponse(responseCode = "400", description = "bad request user aleredy exist or role not found")
    })
    @PostMapping("/signup")
    ResponseEntity<String> signup(@RequestBody @Valid SignupDto dto)
    { 
        return authenticationService.signup(dto);
    }


    @Operation(summary = "Log in a user", description = "Authenticates a user based on the provided email and password in the SigninDto object. If credentials are valid, a JWT token is returned.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful login. JWT token is returned in the response body."),
        @ApiResponse(responseCode = "401", description = "Invalid email or password.")
    })    
    @PostMapping("/signin")
    ResponseEntity<UserDto> signin(@RequestBody SigninDto dto)
    {
        return authenticationService.signin(dto);
    }
}
