package api.dev.instructors.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import api.dev.instructors.model.Instructors;

public interface InstructorsRepository extends JpaRepository<Instructors,Integer>{
    Optional<Instructors> findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM course_categories WHERE course_id IN (SELECT c.course_id FROM courses c WHERE c.instructor_id = :instructorId)", nativeQuery = true)
    void deleteCourseCategoriesJoinTable(@Param("instructorId") Integer instructorId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM cart_courses WHERE course_id IN (SELECT c.course_id FROM courses c WHERE c.instructor_id = :instructorId)", nativeQuery = true)
    void deleteCartCoursesJoinTable(@Param("instructorId") Integer instructorId);


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM student_courses WHERE course_id IN (SELECT c.course_id FROM courses c WHERE c.instructor_id = :instructorId)", nativeQuery = true)
    void deleteStudentCoursesJoinTable(@Param("instructorId") Integer instructorId);
}
