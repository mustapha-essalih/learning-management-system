package api.dev.courses.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import api.dev.courses.dto.CourseDTO;
import api.dev.courses.model.Courses;
import api.dev.enums.Status;

@Repository
public interface CoursesRepository extends JpaRepository<Courses,Integer>{
    
    @Query("SELECT course FROM Courses course WHERE LOWER(course.title) LIKE LOWER(CONCAT('%', :keyword, '%'))  AND course.status = 'PUBLISHED'")
    List<Courses> findCourseByName(@Param("keyword") String keyword);

    Optional<Courses> findByCourseIdAndStatusAndIsFree(Integer id, Status status, Boolean isFree);

    Optional<Courses> findByTitle(String courseName);


    @Query(value = "SELECT c.price * (SELECT COUNT(*) FROM student_courses sc WHERE sc.course_id = c.course_id) FROM courses c WHERE c.course_id = :courseId AND c.is_free = false", nativeQuery = true)
    BigDecimal calculateCourseRevenue(@Param("courseId") Integer courseId);

    @Query(value = "SELECT SUM(c.price * (SELECT COUNT(*) FROM student_courses sc WHERE sc.course_id = c.course_id)) FROM courses c WHERE c.instructor_id = :instructorId AND c.is_free = false", nativeQuery = true)
    BigDecimal calculateTotalRevenueForInstructor(@Param("instructorId") Integer instructorId);

    @Query(value = "SELECT COUNT(*) FROM student_courses sc JOIN courses c ON sc.course_id = c.course_id WHERE c.instructor_id = :instructorId", nativeQuery = true)
    int countTotalStudentsForInstructor(@Param("instructorId") Integer instructorId);

}
