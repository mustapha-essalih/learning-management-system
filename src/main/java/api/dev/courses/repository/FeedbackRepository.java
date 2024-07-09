package api.dev.courses.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import api.dev.courses.model.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    
    @Query("SELECT AVG(r.rating) FROM Feedback r WHERE r.course.courseId = :courseId")
    Double getAverageRatingByCourseId(@Param("courseId") Integer courseId);
}