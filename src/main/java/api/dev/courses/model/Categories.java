package api.dev.courses.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Categories {
    
     
    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    private String category;

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
    
}
