package api.dev.students.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import api.dev.authentication.model.JwtToken;
import api.dev.authentication.model.User;
import api.dev.courses.model.Courses;
import api.dev.courses.model.Feedback;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Students extends User {
    
    

    @ManyToMany
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(
                name = "user_id"
                ),
                inverseJoinColumns = @JoinColumn(
                    name = "course_id"
            )
    )
    private Set<Courses> courses;


    @OneToOne(mappedBy = "student" , cascade = CascadeType.ALL, orphanRemoval = true) 
    private Cart cart;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student", orphanRemoval = true)
    private List<Feedback> feedbacks;

    public Students() {
    }

    

    public Students(String email, String password, String role, String fullName) {
        
        super(email, password, role, fullName);
    }


    public Students(String email, String password, String role, String fullName, List<JwtToken> jwtToken) {
        super(email, password, role, fullName, jwtToken);
    }



    public Set<Courses> getCourses() {
        return courses;
    }



    public void setCourses(Set<Courses> courses) {
        this.courses = courses;
    }



    public Cart getCart() {
        return cart;
    }



    public void setCart(Cart cart) {
        this.cart = cart;
    }




    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }



    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }
    
    public void deleteCourses(Set<Courses> courses) {
        this.courses.removeAll(courses);
        this.courses.clear();
    }

    public void deleteFeedBacks(List<Feedback> feedbacks){
        this.feedbacks.removeAll(feedbacks);
        this.feedbacks.clear();
    }
    
}
