package api.dev.authentication;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import api.dev.admin.model.Admin;
import api.dev.authentication.dto.request.SigninDto;
import api.dev.authentication.dto.request.SignupDto;
import api.dev.authentication.dto.response.JwtResponse;
import api.dev.authentication.model.JwtToken;
import api.dev.authentication.model.User;
import api.dev.authentication.repository.UserRepository;
import api.dev.instructors.model.Instructors;
import api.dev.managers.model.Managers;
import api.dev.security.JwtService;
import api.dev.students.model.Students;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public ResponseEntity<String> signup(SignupDto dto) {
        
        if(userRepository.findByEmail(dto.getEmail()).isPresent())
        {
            return ResponseEntity.badRequest().build();
        }
        
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        
        
        if (dto.getRole().equals("STUDENT")) 
        {
            Students newStudent = new Students(dto.getEmail(),encodedPassword , dto.getRole(), dto.getFullName());
            userRepository.save(newStudent);
        }
        else if (dto.getRole().equals("INSTRUCTOR")) {
            Instructors  newManager = new Instructors(dto.getEmail(),encodedPassword , dto.getRole(), dto.getFullName());
            userRepository.save(newManager);
        
        }
        else
            return ResponseEntity.badRequest().build();// role not found
        
        return ResponseEntity.status(201).body("Signup successful");
    }

     public ResponseEntity<Cookie> signin(SigninDto dto , HttpServletResponse response ) {
        JwtResponse jwt = null;
        try {
            Authentication authenticatedUser = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(),dto.getPassword()));
            
            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
            User user = (User) authenticatedUser.getPrincipal();
            jwt = jwtService.generateToken(user);
            
            JwtToken jwtToken = new JwtToken(jwt.getJwt(), jwt.getIssuedAt(), jwt.getExpiration(), user);
            
            // revoke all tokens

            List<JwtToken> jwtTokens = new ArrayList<>();

            jwtTokens.add(jwtToken);
            jwtToken.setUser(user);
            user.getJwtToken().add(jwtToken);
            userRepository.save(user);
            
        } catch (Exception e) {
            throw new BadCredentialsException("email or password incorrect.");
        }

        Cookie cookie = new Cookie("token", jwt.getJwt());

        //  cookie.setHttpOnly(true);
        // cookie.setSecure(true); // Use this in production with HTTPS
        cookie.setMaxAge(7 * 24 * 60 * 60); // 1 week expiration

        return ResponseEntity.ok().body(cookie);     
    }
    
}
