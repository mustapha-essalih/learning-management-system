package api.dev.managers.dto.request;

import api.dev.enums.Status;

public class UpdateCourseStatusDto {
    private Integer courseId;
    private Status status;
    public Integer getCourseId() {
        return courseId;
    }
    public Status getStatus() {
        return status;
    }


    
    
}
