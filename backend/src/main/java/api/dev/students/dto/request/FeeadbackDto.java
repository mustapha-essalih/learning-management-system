package api.dev.students.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class FeeadbackDto {

    @NotNull
    private Integer courseId;
    
    @NotNull(message = "should enter comment")
    @NotBlank(message = "should enter comment")
    @NotEmpty(message = "should enter comment")
    private String comment;

    @Positive(message = "rating should be positive")
    @Min(value = 0, message = "min value is 0")
    @Max(value = 5, message = "max value is 5")
    private double rating;
    
    public Integer getCourseId() {
        return courseId;
    }
    public String getComment() {
        return comment;
    }
    public double getRating() {
        return rating;
    }

    
}
