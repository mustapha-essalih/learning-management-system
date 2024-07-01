package dev.api.model;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import dev.api.enums.Category;
import dev.api.enums.Language;
import dev.api.enums.Level;
import dev.api.enums.Status;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Courses { 
    
    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;

    @Column(nullable = false , unique = true)
    private String title;

    @Column(columnDefinition="TEXT" , nullable = false)
    private String description;

    @Column(nullable = false)
    private short duration;

    @ManyToMany(cascade = CascadeType.ALL )
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
    private Set<Categories> category = new HashSet<>(); 
    
    @ManyToMany(mappedBy = "courses" , cascade = CascadeType.ALL)
    private Set<Orders> orders;

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
  
    @Column(columnDefinition="TEXT" , nullable = false)
    private String courseImage;

    private String contentType;
    
    private boolean isFree;
    
    @OneToMany( cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id", referencedColumnName = "courseId")
    private List<Chapter> chapters = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    @CreationTimestamp  // is triggered only once when the entity is first created.
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp // is triggered every time an entity is updated.
    private LocalDateTime updatedAt = LocalDateTime.now();

@Override
public String toString() {
        return "Courses [courseId=" + courseId + ", title=" + title + "]";
}

    
}
