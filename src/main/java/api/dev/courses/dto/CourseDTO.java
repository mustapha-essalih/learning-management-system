package api.dev.courses.dto;

import java.math.BigDecimal;
import java.util.List;

public class CourseDTO {
    private Integer courseId;
    private String title;
    private String description;
    private BigDecimal price;
    private String courseImage;
    private String level;
    private String language;
    private boolean isFree;
    private List<ChapterDTO> chapters;
    
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public String getCourseImage() {
        return courseImage;
    }
    public void setCourseImage(String courseImage) {
        this.courseImage = courseImage;
    }
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public boolean isFree() {
        return isFree;
    }
    public void setFree(boolean isFree) {
        this.isFree = isFree;
    }
    public List<ChapterDTO> getChapters() {
        return chapters;
    }
    public void setChapters(List<ChapterDTO> chapters) {
        this.chapters = chapters;
    }
    @Override
    public String toString() {
        return "CourseDTO [courseId=" + courseId + ", title=" + title + ", description=" + description + ", price="
                + price + ", courseImage=" + courseImage + ", level=" + level + ", language=" + language + ", isFree="
                + isFree + ", chapters=" + chapters + "]";
    }

}
