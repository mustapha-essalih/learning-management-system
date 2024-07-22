package api.dev.courses.dto;

import java.util.List;

public class ChapterDTO {
    
    private Integer chapterId;
    private String title;
    private List<ResourceDTO> resources;
    
    public Integer getChapterId() {
        return chapterId;
    }
    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<ResourceDTO> getResources() {
        return resources;
    }
    public void setResources(List<ResourceDTO> resources) {
        this.resources = resources;
    }
    @Override
    public String toString() {
        return "ChapterDTO [chapterId=" + chapterId + ", title=" + title + ", resources=" + resources + "]";
    }

    
    
}