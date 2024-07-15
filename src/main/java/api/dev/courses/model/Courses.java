package api.dev.courses.model;

import jakarta.persistence.EnumType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import api.dev.enums.Language;
import api.dev.enums.Level;
import api.dev.enums.Status;
import api.dev.instructors.model.Instructors;
import api.dev.students.model.Cart;
import api.dev.students.model.Students;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Courses { 
    
    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;

    @Column(nullable = false , unique = true)
    private String title;

    @Column(columnDefinition="TEXT" , nullable = false)
    private String description;


    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Level level;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Language language;

    @Column( columnDefinition = "Decimal(10, 2)")
    private BigDecimal price;
  
    @Column(nullable = false)
    private String courseImage;

    private String contentType;
    
    private boolean isFree;
    
    @CreationTimestamp  // is triggered only once when the entity is first created.
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp // is triggered every time an entity is updated.
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToMany
    @JoinTable(
            name = "course_categories",
            joinColumns = @JoinColumn(
                    name = "course_id", referencedColumnName = "courseId"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "category_id", referencedColumnName = "categoryId"
            )
    )
    @Column(nullable = false)
    private Set<Categories> category; 

    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "course")
    private List<Chapter> chapters = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private Instructors instructor;


    @ManyToMany(mappedBy = "courses")
    private Set<Students> students ;
 
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course" , orphanRemoval = true)
    private List<Feedback> feedback = new ArrayList<>();;

    @ManyToMany(mappedBy = "courses")
    private Set<Cart> cart;

    public Courses() {}


    public Courses(Set<Categories> category, String description, String title, Language language, Level level,BigDecimal price, Status status, boolean isFree) {

        this.category = category;
        this.description = description;
        this.title = title;
        this.language = language;
        this.level = level;
        this.price = price;
        this.status = status;
        this.isFree = isFree;
    }


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



    public Status getStatus() {
            return status;
    }



    public void setStatus(Status status) {
            this.status = status;
    }



    public Level getLevel() {
            return level;
    }



    public void setLevel(Level level) {
            this.level = level;
    }



    public Language getLanguage() {
            return language;
    }



    public void setLanguage(Language language) {
            this.language = language;
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



    public String getContentType() {
            return contentType;
    }



    public void setContentType(String contentType) {
            this.contentType = contentType;
    }



    public boolean isFree() {
            return isFree;
    }



    public void setFree(boolean isFree) {
            this.isFree = isFree;
    }



    public LocalDateTime getCreatedAt() {
            return createdAt;
    }



    public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
    }



    public LocalDateTime getUpdatedAt() {
            return updatedAt;
    }



    public void setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
    }



    public Set<Categories> getCategory() {
            return category;
    }



    public void setCategory(Set<Categories> category) {
            this.category = category;
    }



    public List<Chapter> getChapters() {
            return chapters;
    }



    public void setChapters(List<Chapter> chapters) {
            this.chapters = chapters;
    }



    public Instructors getInstructor() {
            return instructor;
    }



    public void setInstructor(Instructors instructor) {
            this.instructor = instructor;
    }



    public Set<Students> getStudents() {
            return students;
    }



    public void setStudents(Set<Students> students) {
            this.students = students;
    }


    public List<Feedback> getFeedback() {
        return feedback;
    }



    public void setFeedback(List<Feedback> feedback) {
        this.feedback = feedback;
    }

    public Double calculateAverageRating() {
        if (feedback.isEmpty()) {
            return 0.0; // or handle as needed
        }

        double sum = 0.0;
        for (Feedback f : feedback) {
            sum += f.getRating();
        }

        return sum / feedback.size();
    }



    public Set<Cart> getCart() {
            return cart;
    }

    public void deleteCats(Set<Cart> carts){
        carts.forEach((cart) -> {
            cart.deleteCourses(cart.getCourses());
            
        });
        this.cart.removeAll(carts);    
        this.cart.clear();
    }

    public void deleteCategories(Set<Categories> categories){
        categories.forEach((category) -> category.deleteCourses(category.getCourses()));
        this.category.removeAll(categories);    
        this.category.clear();
    }
 
     
 
}
