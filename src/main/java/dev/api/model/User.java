package dev.api.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter 
@Setter
@Entity
@Table(name = "user_")
public class User implements UserDetails {
    
    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
 
    private String fullName;

    @Column(unique = true)
    private String email;

    private String password;
  
    @Enumerated(EnumType.STRING)
    private Roles roles;
 
    @OneToMany(fetch = FetchType.EAGER ,cascade = CascadeType.ALL ,  mappedBy = "user")
    private List<Courses> courses = new ArrayList<>();

    
    @OneToMany(cascade = CascadeType.ALL , mappedBy = "user", orphanRemoval = true)
    private List<JwtToken> jwtToken = new ArrayList<>();

    
    @OneToMany(cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private List<Categories> categories;

    @OneToOne(mappedBy = "student" , cascade = CascadeType.ALL) 
    private Cart cart;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Orders> orders;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + roles.name()));  
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;    
    }


    @Override
    public boolean isAccountNonLocked() {
        return true;    
    }

    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    
    @Override
    public boolean isEnabled() {
        return true;
    }

  
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

}
