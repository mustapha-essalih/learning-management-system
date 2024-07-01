package dev.api.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Resources {
    
    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer resourceId;

    @Column(columnDefinition="TEXT" , nullable = false)
    private String file;

    private String contentType;

    @Column(nullable = false)
    private String title;

    private boolean isFree;

    private short duration; // if null it is not video
 

    @Override
    public String toString() {
        return "Resources [title=" + title + "]";
    }
 
    
}
