package api.dev.admin.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import api.dev.admin.model.Admin;

public interface AdminRepository extends JpaRepository<Admin,Integer>{

    Optional<Admin> findByEmail(String string);


    @Query(value = "SELECT SUM(c.price * (SELECT COUNT(*) FROM student_courses sc WHERE sc.course_id = c.course_id)) FROM courses c WHERE c.is_free = false", nativeQuery = true)
    BigDecimal calculateTotalRevenue();
    
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM course_categories WHERE category_id = :category_id", nativeQuery = true)
    void deleteCourseCategoriesJoinTableBycategory_id(@Param("category_id") Integer category_id);


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM cart WHERE user_id = :studentId", nativeQuery = true)
    void deleteCartByStudentId(@Param("studentId") Integer studentId);

 
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM student_courses WHERE user_id = :studentId", nativeQuery = true)
    void deleteCoursesByStudentId(@Param("studentId") Integer studentId);
}