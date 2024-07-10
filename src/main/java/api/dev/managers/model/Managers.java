package api.dev.managers.model;

import java.util.List;

import api.dev.authentication.model.JwtToken;
import api.dev.authentication.model.User;
import jakarta.persistence.Entity;

@Entity
public class Managers extends User {

    public Managers() {
    }

    

    public Managers(String email, String password, String role, String fullName) {
        
        super(email, password, role, fullName);
    }


    public Managers(String email, String password, String role, String fullName, List<JwtToken> jwtToken) {
        super(email, password, role, fullName, jwtToken);
    }
}
