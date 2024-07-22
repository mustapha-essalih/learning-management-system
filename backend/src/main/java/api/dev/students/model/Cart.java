package api.dev.students.model;

import java.math.BigDecimal;
import java.util.Set;

import api.dev.courses.model.Courses;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Cart {
    
    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Students student;
    
    @ManyToMany
    @JoinTable(
        name = "cart_courses",
        joinColumns = @JoinColumn(
            name = "cart_id", referencedColumnName = "cartId"
        ),
            inverseJoinColumns = @JoinColumn(
                name = "course_id", referencedColumnName = "courseId"
        )
    )
    private Set<Courses> courses; 

    @Column( columnDefinition = "Decimal(10, 2)")
    private BigDecimal totalAmount;


    
    public Cart() {
    }

    public Cart(Students student, Set<Courses> courses, BigDecimal totalAmount) {
        this.student = student;
        this.courses = courses;
        this.totalAmount = totalAmount;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Students getStudent() {
        return student;
    }

    public void setStudent(Students student) {
        this.student = student;
    }

    public Set<Courses> getCourses() {
        return courses;
    }

    public void setCourses(Set<Courses> courses) {
        this.courses = courses;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    
    public void deleteCourses(Set<Courses> course){
        this.courses.removeAll(course);
        this.courses.clear();
    }
}
