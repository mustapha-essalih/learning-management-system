package dev.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.api.model.Resources;

public interface ResourcesRepository extends JpaRepository<Resources, Integer>{
    
}
