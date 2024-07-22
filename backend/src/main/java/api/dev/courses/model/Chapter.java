package api.dev.courses.model;






import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
 
@Entity
public class Chapter{

    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chapterId;

    @Column(nullable = false)
    private String title;


    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "chapter")
    private List<Resources> resources = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Courses course;


    public Chapter() {
    }



    public Chapter(String title, boolean isFree, List<Resources> resources) {
        this.title = title;
        this.resources = resources;
    }



    public Chapter(String title) {
        this.title = title;
    }



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

    public List<Resources> getResources() {
        return resources;
    }



    public void setResources(List<Resources> resources) {
        this.resources = resources;
    }



    public Courses getCourse() {
        return course;
    }



    public void setCourse(Courses course) {
        this.course = course;
    }

    public void removeResources() {
        for (Resources resource : resources) {
            resource.setChapter(null);
        }
        resources.clear();
    }
    
}
