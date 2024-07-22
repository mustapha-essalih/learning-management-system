package api.dev.security;


import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import api.dev.authentication.repository.UserRepository;
 
@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) {

        return userRepository.findByEmail(username)
                        .orElseThrow(() -> new BadCredentialsException("User not found"));
    }
    
}
