package api.dev.courses.dto;

public class ResourceDTO {

    private Integer resourceId;
    private String title;
    private String file;
    private String contentType;

    
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
    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    @Override
    public String toString() {
        return "ResourceDTO [resourceId=" + resourceId + ", title=" + title + "]";
    }

    
}
