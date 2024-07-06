package api.dev.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.dev.authentication.dto.request.SigninDto;
import api.dev.authentication.dto.request.SignupDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@CrossOrigin("*")
@RequestMapping("/api/auth")
@RestController
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;


    public AuthenticationController(AuthenticationService authenticationService) 
    {
        this.authenticationService = authenticationService;
    }


    @PostMapping("/signup")
    ResponseEntity<String> signup(@RequestBody @Valid SignupDto dto)
    { 
        return authenticationService.signup(dto);
    }

    @PostMapping("/signin")
    ResponseEntity<Cookie> signin(@RequestBody SigninDto dto, HttpServletResponse res)
    {
        return authenticationService.signin(dto , res);
    }
}
