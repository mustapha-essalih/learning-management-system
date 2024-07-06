package api.dev.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import api.dev.authentication.model.JwtToken;

public interface JwtTokenRepository extends JpaRepository<JwtToken,Integer>{
    
}
