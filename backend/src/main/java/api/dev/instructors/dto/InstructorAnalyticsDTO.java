package api.dev.instructors.dto;

import java.math.BigDecimal;
import java.util.List;

public class InstructorAnalyticsDTO {

    private BigDecimal totalRevenue;
    private int totalStudents;
    private int totalFeedback;
    private List<CourseTitleDTO> courses;
    
    

    
    public InstructorAnalyticsDTO(BigDecimal totalRevenue, int totalStudents, int totalFeedback,
            List<CourseTitleDTO> courses) {
        this.totalRevenue = totalRevenue;
        this.totalStudents = totalStudents;
        this.totalFeedback = totalFeedback;
        this.courses = courses;
    }
    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }
    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
    public int getTotalStudents() {
        return totalStudents;
    }
    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }
    public int getTotalFeedback() {
        return totalFeedback;
    }
    public void setTotalFeedback(int totalFeedback) {
        this.totalFeedback = totalFeedback;
    }
    public List<CourseTitleDTO> getCourses() {
        return courses;
    }
    public void setCourses(List<CourseTitleDTO> courses) {
        this.courses = courses;
    }

    
}
