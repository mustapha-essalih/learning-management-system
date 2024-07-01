package dev.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.api.model.Chapter;

public interface ChapterRepository extends JpaRepository<Chapter,Integer>{
    
}
