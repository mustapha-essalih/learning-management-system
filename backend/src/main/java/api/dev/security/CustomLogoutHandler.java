package api.dev.security;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import api.dev.authentication.model.JwtToken;
import api.dev.authentication.model.User;
import api.dev.authentication.repository.JwtTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final JwtTokenRepository jwtTokenRepository;

    public CustomLogoutHandler(JwtTokenRepository jwtTokenRepository) {
        this.jwtTokenRepository = jwtTokenRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        System.out.println("FFFFFFFFFFFFFF");
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        try {
            
            String jwt = authHeader.substring(7);
            JwtToken storedToken = jwtTokenRepository.findByToken(jwt).orElse(null);
            if (storedToken != null) {
                storedToken.setRevoked(true);
                jwtTokenRepository.save(storedToken);
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
}