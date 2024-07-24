package api.dev.students.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import api.dev.instructors.model.Instructors;
import api.dev.students.model.Students;

public interface StudentsRepository extends JpaRepository<Students,Integer>{
    Optional<Students> findByEmail(String email);
     

    @Modifying
    @Transactional // for delete all carts course of student
    @Query(value = "DELETE FROM cart_courses WHERE cart_id IN (SELECT cart_id FROM cart WHERE user_id = :studentId)", nativeQuery = true)
    void deleteCoursesFromCartByStudentId(@Param("studentId") Integer studentId);

    @Modifying
    @Transactional // delete course cart of a student
    @Query(value = "DELETE FROM cart_courses WHERE cart_id = :cartId AND course_id = :courseId", nativeQuery = true)
    void deleteCartCourseByCartIdAndCourseId(@Param("cartId") Integer cartId, @Param("courseId") Integer courseId);
}