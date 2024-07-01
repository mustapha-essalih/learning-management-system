package dev.api.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.api.admin.dto.request.CategoryDto;
import dev.api.authentication.dto.SignupDto;
import dev.api.model.Roles;
import lombok.AllArgsConstructor;

@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin("*")
@RequestMapping("/api/admin")
@AllArgsConstructor
@RestController
public class AdminController {
    
    private AdminService adminService;

    @PostMapping("/newManager")
    public ResponseEntity<?> createManager(@RequestBody SignupDto dto)
    {
        dto.setRole(Roles.MANAGER);
        return adminService.createManager(dto);
    }

    @GetMapping("/getAllManagers")
    public ResponseEntity<?> getAllManagers()
    {
        return adminService.getAllManagers();
    }

    @GetMapping("/getManager")
    public ResponseEntity<?> getManager(@RequestParam Integer managerId)
    {
        return adminService.getManager(managerId);
    }

    @DeleteMapping("/deleteManager")
    public ResponseEntity<?> deleteManager(@RequestParam Integer managerId){
        return adminService.deleteManager(managerId);
    }

    @DeleteMapping("/deleteStudent")
    public ResponseEntity<?> deleteStudent(@RequestParam Integer studentId){
        return adminService.deleteStudent(studentId);
    }

    @DeleteMapping("/deleteTeacher")
    public ResponseEntity<?> deleteTeacher(@RequestParam Integer teacherId){
        return adminService.deleteTeacher(teacherId);
    }

    @GetMapping("/getAllCourses")// dont return the resources
    public ResponseEntity<?> getAllCources() 
    {    
        return adminService.getAllCources();
    }
   
    @PostMapping("/addCategory")
    public ResponseEntity<?> addCategory(@RequestParam String category) {
        return adminService.addCategory(category);
    }

    @PutMapping("/updateCategory")
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDto dto) 
    {
        return adminService.updateCategory(dto);
    }
    
    @DeleteMapping("/deleteCategory")
    public ResponseEntity<?> deleteCategory(@RequestParam Integer categoryId){
        return adminService.deleteCategory(categoryId);
    }

    @GetMapping("/allCategories")
    public ResponseEntity<?> allCategories() {
        return adminService.allCategories();   
    }
    
    

}
