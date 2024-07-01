package dev.api.managers.dto;

import dev.api.enums.Status;
import lombok.Getter;

@Getter
public class UpdateCourseStatusDto {
    private Integer courseId;
    private Status status;
}
