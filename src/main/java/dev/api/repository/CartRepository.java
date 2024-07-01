package dev.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.api.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer>{
    
}
