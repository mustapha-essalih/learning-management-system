package api.dev.admin.dto;

import java.math.BigDecimal;

public class PlatformAnalyticsDTO {
    private Long totalCourses;
    private Long totalStudents;
    private Long totalInstructors;
    private Long totalFeedbacks;
    private BigDecimal totalRevenue;
    
    public PlatformAnalyticsDTO(Long totalCourses, Long totalStudents, Long totalInstructors,
    Long totalFeedbacks, BigDecimal totalRevenue) {
        this.totalCourses = totalCourses;
        this.totalStudents = totalStudents;
        this.totalInstructors = totalInstructors;
        this.totalFeedbacks = totalFeedbacks;
        this.totalRevenue = totalRevenue;
    }

    public Long getTotalCourses() {
        return totalCourses;
    }

    public Long getTotalStudents() {
        return totalStudents;
    }

    public Long getTotalInstructors() {
        return totalInstructors;
    }

    public Long getTotalFeedbacks() {
        return totalFeedbacks;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }
    
    
}