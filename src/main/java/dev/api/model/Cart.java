package dev.api.model;



import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

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
public class Cart {
    
    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id" , referencedColumnName="userId")
    private User student;
    
    @ManyToMany(cascade = CascadeType.ALL )
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
    private BigDecimal totalAmout;

    @Override
    public String toString() {
        return "Cart [totalAmout=" + totalAmout + "]";
    }


    
}
