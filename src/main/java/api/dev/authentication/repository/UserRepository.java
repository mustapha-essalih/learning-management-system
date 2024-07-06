package api.dev.authentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import api.dev.authentication.model.User;

public interface UserRepository extends JpaRepository<User,Integer>{

    Optional<User> findByEmail(String username);
    
}
