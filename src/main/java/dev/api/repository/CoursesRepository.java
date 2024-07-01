package dev.api.repository;

import java.util.List;
import java.util.Optional;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.api.enums.Status;
import dev.api.model.Courses;
import dev.api.user.dao.response.CourseDTO;

public interface CoursesRepository extends JpaRepository<Courses,Integer>{
    @Query("SELECT i FROM Courses i WHERE LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%'))  AND i.status = 'PUBLISHED'")
    List<Courses> searchByName(@Param("keyword") String keyword);

    Optional<Courses> findByCourseIdAndStatusAndIsFree(Integer id, Status status, Boolean isFree);

    Optional<Courses> findByTitle(String courseName);

    
}
