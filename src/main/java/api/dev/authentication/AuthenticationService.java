package api.dev.authentication;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import api.dev.admin.model.Admin;
import api.dev.authentication.dto.UserDto;
import api.dev.authentication.dto.request.SigninDto;
import api.dev.authentication.dto.request.SignupDto;
import api.dev.authentication.dto.response.JwtResponse;
import api.dev.authentication.mapper.UserMapper;
import api.dev.authentication.model.JwtToken;
import api.dev.authentication.model.User;
import api.dev.authentication.repository.JwtTokenRepository;
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
    private  JwtTokenRepository jwtTokenRepository; 
    private UserMapper userMapper;


    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, JwtService jwtService, JwtTokenRepository jwtTokenRepository,
            UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.jwtTokenRepository = jwtTokenRepository;
        this.userMapper = userMapper;
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
            Instructors  newInstructor = new Instructors(dto.getEmail(),encodedPassword , dto.getRole(), dto.getFullName());
            userRepository.save(newInstructor);
        }
        else
            return ResponseEntity.badRequest().build();
        
        return ResponseEntity.status(201).body("Signup successful");
    }

    public ResponseEntity<UserDto> signin(SigninDto dto) {
        JwtToken jwtToken = null;
        UserDto userDto = null;
        JwtResponse jwt = null;

        try {
            Authentication authenticatedUser = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(),dto.getPassword()));
            
            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
            User user = (User) authenticatedUser.getPrincipal();
            jwt = jwtService.generateToken(user);
            
            revokeAllTokenByUser(user.getUserId());
            jwtToken = new JwtToken(jwt.getJwt(), jwt.getIssuedAt(), jwt.getExpiration(), user);
            jwtTokenRepository.save(jwtToken);
            userDto = userMapper.toManagerDto(user);
            
            
        } catch (Exception e) {
            throw new BadCredentialsException("email or password incorrect.");
        }
        JwtResponse token = new JwtResponse(jwt.getJwt(), jwt.getIssuedAt(), jwt.getExpiration());

        userDto.setJwt(token.getJwt());


        ResponseCookie jwtCookie = ResponseCookie.from("jwt", token.getJwt())
                // .httpOnly(true)
                // .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 1 week duration
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(userDto);
    }
    
    
    private void revokeAllTokenByUser(Integer userId) 
    {
        List<JwtToken> jwtTokenOfUser = jwtTokenRepository.findAllValidTokenByUser(userId);

        if(jwtTokenOfUser.isEmpty()) {
            return;
        }

        jwtTokenOfUser.forEach(token-> {
            token.setRevoked(true);
        });

        jwtTokenRepository.saveAll(jwtTokenOfUser);
    }
}
