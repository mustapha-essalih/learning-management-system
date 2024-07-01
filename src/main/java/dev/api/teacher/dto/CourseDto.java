package dev.api.teacher.dto;

import java.util.List;

public class CourseDto {
   
    private String title;
    private String description;
    public CourseDto() {
    }
    public CourseDto(String title, String description) {
        this.title = title;
        this.description = description;
    }
    @Override
    public String toString() {
        return "CourseDto [title=" + title + ", description=" + description + "]";
    }

    
}
