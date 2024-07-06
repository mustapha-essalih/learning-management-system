package api.dev.courses.dto;

public class ResourceDTO {
    private Integer resourceId;
    private String file;
    private String title;

    
    public Integer getResourceId() {
        return resourceId;
    }
    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }
    public String getFile() {
        return file;
    }
    public void setFile(String file) {
        this.file = file;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
        return "ResourceDTO [resourceId=" + resourceId + ", file=" + file + ", title=" + title + "]";
    }

    
}
