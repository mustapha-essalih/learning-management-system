package dev.api.authentication;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.api.authentication.dto.SigninDto;
import dev.api.authentication.dto.SignupDto;
import dev.api.model.Roles;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;


@CrossOrigin("*")
@RequestMapping("/api/auth")
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    ResponseEntity<String> signup(@RequestBody @Valid SignupDto dto)
    { 
        authenticationService.signup(dto);//handle if aleredy exist
        return ResponseEntity.status(HttpStatus.OK).body("Signup successful");
    }

    @PostMapping("/signin")
    ResponseEntity<Cookie> signin(@RequestBody SigninDto dto, HttpServletResponse res)
    {
        return authenticationService.signin(dto , res);
    }

    @PostMapping("/checkJwt")
    ResponseEntity<String> checkJwt(@RequestBody String jwt)
    { 
        return authenticationService.checkJwt(jwt);
    }
}


