package api.dev.admin.dto;

import java.math.BigDecimal;

public class PlatformAnalyticsDTO {
    private Integer totalCourses;
    private Integer totalStudents;
    private Integer totalInstructors;
    private Integer totalFeedbacks;
    private BigDecimal totalRevenue;
    private Double averageRating;
    
    public Integer getTotalCourses() {
        return totalCourses;
    }
    public void setTotalCourses(Integer totalCourses) {
        this.totalCourses = totalCourses;
    }
    public Integer getTotalStudents() {
        return totalStudents;
    }
    public void setTotalStudents(Integer totalStudents) {
        this.totalStudents = totalStudents;
    }
    public Integer getTotalInstructors() {
        return totalInstructors;
    }
    public void setTotalInstructors(Integer totalInstructors) {
        this.totalInstructors = totalInstructors;
    }
    public Integer getTotalFeedbacks() {
        return totalFeedbacks;
    }
    public void setTotalFeedbacks(Integer totalFeedbacks) {
        this.totalFeedbacks = totalFeedbacks;
    }
    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }
    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
    public Double getAverageRating() {
        return averageRating;
    }
    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }


    
}