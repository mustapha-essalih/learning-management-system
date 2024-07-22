package api.dev.admin.dto.response;

public class ManagerDto {

    private Integer managerId;

    private String email;

    private String fullName;
    
    private String role;


    public ManagerDto(Integer managerId, String email, String fullName, String role) {
        this.managerId = managerId;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "ManagerDto [managerId=" + managerId + ", email=" + email + ", fullName=" + fullName + ", role=" + role
                + "]";
    }

    

    
}
