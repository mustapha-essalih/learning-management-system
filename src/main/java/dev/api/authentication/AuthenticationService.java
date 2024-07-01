package dev.api.authentication;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.api.authentication.dto.SigninDto;
import dev.api.authentication.dto.SignupDto;
import dev.api.model.JwtToken;
import dev.api.model.Roles;
import dev.api.model.User;
import dev.api.repository.JwtTokenRepository;
import dev.api.repository.UserRepository;
import dev.api.security.JwtService;
import dev.api.user.dto.response.JwtResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AuthenticationService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtTokenRepository jwtTokenRepository;
 


    public void signup(SignupDto dto) {
        
        if(userRepository.findByEmail(dto.getEmail()).isPresent())
        {
            return ;
        }
        
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        

        User newUser = User.builder()
                    .email(dto.getEmail())
                    .fullName(dto.getFullName())
                    .password(encodedPassword)
                    .roles(dto.getRole())
                    .build();

        userRepository.save(newUser);
    }


    public ResponseEntity<Cookie> signin(SigninDto dto , HttpServletResponse response ) {
        JwtResponse jwt = null;
        try {
            Authentication authenticatedUser = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(),dto.getPassword()));
            
            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
            User user = (User) authenticatedUser.getPrincipal();
            
            jwt = jwtService.generateToken(user);
            
            JwtToken jwtToken = JwtToken.
                                builder()
                                .token(jwt.getJwt())
                                .createdAt(jwt.getIssuedAt())
                                .expiresAt(jwt.getExpiration())
                                .build();
            
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


    public ResponseEntity<String> checkJwt(String jwt) {
        String userEmail;
        try {
            userEmail = jwtService.extractUserName(jwt);
            
        } catch (Exception e) {
            return ResponseEntity.status(401).body("invalid jwt");  
        }

        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new BadCredentialsException("User not found"));

        if(jwtService.isTokenValid(jwt, user ) == true)
            return ResponseEntity.ok().build();
        return ResponseEntity.status(401).body("invalid jwt");  
    }

}
