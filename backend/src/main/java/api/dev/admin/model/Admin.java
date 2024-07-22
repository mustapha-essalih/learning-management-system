package api.dev.admin.model;

import java.util.List;
import java.util.Set;

import api.dev.authentication.model.JwtToken;
import api.dev.authentication.model.User;
import api.dev.courses.model.Categories;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class Admin extends User {

    @OneToMany(cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<Categories> categories;


    public Admin() {
    }

    

    public Admin(String email, String password, String role, String fullName) {
        
        super(email, password, role, fullName);
    }

    public Admin(String email, String password, String role, String fullName, List<Categories> categories ) {
        super(email, password, role, fullName);
        this.categories = categories;
    }



    public List<Categories> getCategories() {
        return categories;
    }



    public void setCategories(List<Categories> categories) {
        this.categories = categories;
    }

    
}
