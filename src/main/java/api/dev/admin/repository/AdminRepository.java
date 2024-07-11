package api.dev.admin.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import api.dev.admin.model.Admin;
import api.dev.authentication.model.User;

public interface AdminRepository extends JpaRepository<Admin,Integer>{

    @Query(value = "SELECT SUM(c.price * (SELECT COUNT(*) FROM student_courses sc WHERE sc.course_id = c.course_id)) FROM courses c WHERE c.is_free = false", nativeQuery = true)
    BigDecimal calculateTotalRevenue();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM student_courses WHERE course_id IN (SELECT course_id FROM courses WHERE instructor_id = :instructorId)", nativeQuery = true)
    void deleteStudentCoursesByInstructorId(@Param("instructorId") Integer instructorId);
    
    
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM cart_courses WHERE course_id IN (SELECT course_id FROM courses WHERE instructor_id = :instructorId)", nativeQuery = true)
    void deleteCartCoursesByInstructorId(@Param("instructorId") Integer instructorId);
    
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM feedback WHERE course_id IN (SELECT course_id FROM courses WHERE instructor_id = :instructorId)", nativeQuery = true)
    void deleteFeedbackByInstructorId(@Param("instructorId") Integer instructorId);

    Optional<Admin> findByEmail(String string);

}
