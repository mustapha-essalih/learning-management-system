package api.dev.admin.dto.request;

public class ChangePasswordDto {

    private String oldPassword;
    private String newPassword;
    
    public String getOldPassword() {
        return oldPassword;
    }
    public String getNewPassword() {
        return newPassword;
    }

    
}
