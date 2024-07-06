package api.dev.instructors.model;

import java.util.ArrayList;
import java.util.List;

import api.dev.authentication.model.JwtToken;
import api.dev.authentication.model.User;
import api.dev.courses.model.Courses;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Instructors extends User {
     

    

    @OneToMany(cascade = CascadeType.ALL ,  mappedBy = "instructor")
    private List<Courses> courses = new ArrayList<>();



    public Instructors() {
    }

    

    public Instructors(String email, String password, String role, String fullName) {
        
        super(email, password, role, fullName);
    }


    public Instructors(String email, String password, String role, String fullName, List<JwtToken> jwtToken) {
        super(email, password, role, fullName, jwtToken);
    }



    public List<Courses> getCourses() {
        return courses;
    }



    public void setCourses(List<Courses> courses) {
        this.courses = courses;
    }



}
