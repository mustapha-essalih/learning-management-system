package api.dev.courses.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import api.dev.courses.model.Resources;

public interface ResourcesRepository extends JpaRepository<Resources,Integer>{
    
}
