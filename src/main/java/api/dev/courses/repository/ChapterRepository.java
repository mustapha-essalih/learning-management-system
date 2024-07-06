package api.dev.courses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import api.dev.courses.model.Chapter;

public interface ChapterRepository extends JpaRepository<Chapter,Integer>{
}
