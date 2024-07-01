package dev.api.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

 
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter 
@Setter
@Entity
public class Categories {
    
     
    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    private String category; 
}
