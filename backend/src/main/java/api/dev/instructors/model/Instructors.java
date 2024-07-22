package api.dev.instructors.model;

import java.util.List;

import api.dev.authentication.model.User;
import api.dev.courses.model.Courses;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Instructors extends User {
     
    @Column(columnDefinition = "TEXT")
    private String bio;

    @OneToMany(cascade = CascadeType.ALL,  mappedBy = "instructor")
    private List<Courses> courses;


    public Instructors() {
    }

    

    public Instructors(String email, String password, String role, String fullName) {
        
        super(email, password, role, fullName);
    }


    public List<Courses> getCourses() {
        return courses;
    }



    public void setCourses(List<Courses> courses) {
        this.courses = courses;
    }



    public String getBio() {
        return bio;
    }



    public void setBio(String bio) {
        this.bio = bio;
    }

    public void removeCourses() {
        
        // for (Courses course : courses) {
        //     course.removeAllAssociations();
        //     course.getCart().removeAll(course.getCart());
        // }
        // courses.clear();
    }
    
}
