package dev.api.model;

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
public class Chapter{

    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chapterId;

    @Column(nullable = false)
    private String title;

    private boolean isFree;

    

    @OneToMany( cascade = CascadeType.ALL)
    @JoinColumn(name = "chapter_id", referencedColumnName = "chapterId")
    private List<Resources> resources = new ArrayList<>();
 
    @Override
    public String toString() {
        return "Chapter [title=" + title + ", resources=" + resources + "]";
    }

    
}
