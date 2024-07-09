package api.dev.admin.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import api.dev.admin.model.Admin;

public interface AdminRepository extends JpaRepository<Admin,Integer>{

    @Query(value = "SELECT SUM(c.price * (SELECT COUNT(*) FROM student_courses sc WHERE sc.course_id = c.course_id)) FROM courses c WHERE c.is_free = false", nativeQuery = true)
    BigDecimal calculateTotalRevenue();
}