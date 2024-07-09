package api.dev.courses.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import api.dev.courses.dto.CourseDTO;
import api.dev.courses.mapper.CourseMapper;
import api.dev.courses.model.Courses;
import api.dev.enums.Status;

@Repository
public interface CoursesRepository extends JpaRepository<Courses,Integer>{
    
    @Query("SELECT course FROM Courses course WHERE LOWER(course.title) LIKE LOWER(CONCAT('%', :keyword, '%'))  AND course.status = 'PUBLISHED'")
    List<Courses> findCourseByName(@Param("keyword") String keyword);

    Optional<Courses> findByCourseIdAndStatusAndIsFree(Integer id, Status status, Boolean isFree);

    Optional<Courses> findByTitle(String courseName);

}
