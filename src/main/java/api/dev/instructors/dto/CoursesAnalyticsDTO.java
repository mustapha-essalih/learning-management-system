package api.dev.instructors.dto;

import java.math.BigDecimal;

public class CoursesAnalyticsDTO {
    
    private Integer courseId;
    private String title;
    private Integer numberOfStudents;
    private Double averageRating;
    private Integer totalFeedbacks;
    private BigDecimal totalRevenue;
    
    public Integer getCourseId() {
        return courseId;
    }
    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Integer getNumberOfStudents() {
        return numberOfStudents;
    }
    public void setNumberOfStudents(Integer numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }
    public Double getAverageRating() {
        return averageRating;
    }
    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
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



    
}
