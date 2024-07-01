package dev.api.user.dao.response;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

 
@Getter
@Setter
@ToString
public class ChapterDTO {

    private String chapterTitle;
    private List<ResourceDTO> resources;

    
    public ChapterDTO(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }


    public ChapterDTO(String chapterTitle, List<ResourceDTO> resources) {
        this.chapterTitle = chapterTitle;
        this.resources = resources;
    }
    
    
}
