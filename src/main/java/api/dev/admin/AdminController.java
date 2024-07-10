package api.dev.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.dev.admin.dto.PlatformAnalyticsDTO;
import api.dev.admin.dto.request.CategoryDto;
import api.dev.authentication.dto.request.SignupDto;
import api.dev.exceptions.ResourceNotFoundException;
import api.dev.instructors.dto.CoursesAnalyticsDTO;
import api.dev.instructors.dto.InstructorAnalyticsDTO;

import org.springframework.web.bind.annotation.RequestParam;



@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin("*")
@RequestMapping("/api/admin")
@RestController
public class AdminController {
    

    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

 

    // use user in

    @GetMapping("/platform-analytics")
    public ResponseEntity<PlatformAnalyticsDTO> getPlatformAnalytics() {
        return adminService.getPlatformAnalytics();
    }

    

    @GetMapping("/get-instructor-analytics")
    public ResponseEntity<InstructorAnalyticsDTO> getInstructorAnalytics(@RequestParam Integer instructorId) throws ResourceNotFoundException {
        return adminService.getInstructorAnalytics(instructorId);
    }
    

    @GetMapping("/get-course-analytics")
    public ResponseEntity<CoursesAnalyticsDTO> getCourseAnalytics(@RequestParam Integer courseId, @RequestParam Integer instructorId) throws ResourceNotFoundException {
        return adminService.getCourseAnalytics(courseId, instructorId);
    }
    

    @PostMapping("/newManager")
    public ResponseEntity<?> createManager(@RequestBody SignupDto dto)
    {
        return adminService.createManager(dto);
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUsers(@RequestParam Integer userId) throws ResourceNotFoundException
    {
        return adminService.deleteUsers(userId);
    }


     @PostMapping("/add-category")
    public ResponseEntity<?> addCategory(@RequestParam String category) {
        return adminService.addCategory(category);
    }

    @PutMapping("/update-category")
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDto dto) throws ResourceNotFoundException 
    {
        return adminService.updateCategory(dto);
    }
    
    @DeleteMapping("/delete-category")
    public ResponseEntity<?> deleteCategory(@RequestParam Integer categoryId) throws ResourceNotFoundException{
        return adminService.deleteCategory(categoryId);
    }

    @GetMapping("/all-categories")
    public ResponseEntity<?> allCategories() {
        return adminService.allCategories();   
    }


}
