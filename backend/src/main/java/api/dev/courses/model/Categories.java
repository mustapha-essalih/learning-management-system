package api.dev.courses.model;

import java.util.Set;

import api.dev.students.model.Students;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class Categories {
    
     
    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    private String category;



    @ManyToMany(mappedBy = "category")
    private Set<Courses> courses ;
   
    public Categories() {
    }

    public Categories(String category) {
        this.category = category;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    

    @Override
    public String toString() {
        return "Categories [categoryId=" + categoryId + ", category=" + category + "]";
    }

    public Set<Courses> getCourses() {
        return courses;
    }

    public void setCourses(Set<Courses> courses) {
        this.courses = courses;
    } 
    
    public void deleteCourses(Set<Courses> courses) {
        this.courses.removeAll(courses);
        this.courses.clear();
    }
}