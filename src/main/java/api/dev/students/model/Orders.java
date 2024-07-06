package api.dev.students.model;

import java.math.BigDecimal;
import java.util.Set;

import api.dev.courses.model.Courses;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Orders {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Students student;
    
    @ManyToMany(cascade = CascadeType.ALL )
    @JoinTable(
        name = "order_courses",
        joinColumns = @JoinColumn(
            name = "order_id", referencedColumnName = "orderId"
        ),
            inverseJoinColumns = @JoinColumn(
                name = "course_id", referencedColumnName = "courseId"
        )
    )
    private Set<Courses> courses;
 
    
    private Boolean enrollerd;

    @Column( columnDefinition = "Decimal(10, 2)")
    private BigDecimal totalAmount;

    

    public Orders() {
        this.enrollerd = false;
    }

    

  
    

    public Orders(Students student, Set<Courses> courses, Boolean enrollerd, BigDecimal totalAmount) {
        this.student = student;
        this.courses = courses;
        this.enrollerd = enrollerd;
        this.totalAmount = totalAmount;
    }






    public Long getOrderId() {
        return orderId;
    }


    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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


    public Boolean getEnrollerd() {
        return enrollerd;
    }


    public void setEnrollerd(Boolean enrollerd) {
        this.enrollerd = enrollerd;
    }






    public BigDecimal getTotalAmount() {
        return totalAmount;
    }






    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }


    
}