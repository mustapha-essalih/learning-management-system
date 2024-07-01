package dev.api.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Orders {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id", referencedColumnName = "userId")
    private User student;
    
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
    
    private BigDecimal totalAmount;
}