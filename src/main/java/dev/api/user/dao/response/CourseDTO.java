package dev.api.user.dao.response;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dev.api.user.dto.response.ChapterTitle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CourseDTO {

    private String title;
    private String description;
    private List<ChapterDTO> chapters;

    
    public CourseDTO(String title, String description) {
        this.title = title;
        this.description = description;
    }


    public CourseDTO(String title, String description, List<ChapterDTO> chapters) {
        this.title = title;
        this.description = description;
        this.chapters = chapters;
    }
}
