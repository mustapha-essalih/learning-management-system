package api.dev.admin;

import java.util.List;

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
import api.dev.admin.dto.request.UpdateManagerDto;
import api.dev.admin.dto.response.ManagerDto;
import api.dev.authentication.dto.request.SignupDto;
import api.dev.exceptions.ResourceNotFoundException;
import api.dev.instructors.dto.CoursesAnalyticsDTO;
import api.dev.instructors.dto.InstructorAnalyticsDTO;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;




@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin("*")
@RequestMapping("/api/admin")
@RestController
public class AdminController {
    

    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

 
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
    

    @PostMapping("/new-manager")
    public ResponseEntity<?> createManager(@RequestBody SignupDto dto)
    {
        return adminService.createManager(dto);
    }


    @PutMapping("/update-manager/{id}")
    public ResponseEntity<Void> updateManager(@PathVariable Integer id, @RequestBody UpdateManagerDto dto) throws ResourceNotFoundException 
    {
        return adminService.updateManager(id, dto);
    }


    @DeleteMapping("/delete-manager/{id}")
    public ResponseEntity<Void> deleteManager(@PathVariable Integer id) throws ResourceNotFoundException{    
        return adminService.deleteManager(id);
    }
    
    @GetMapping("/get-manager/{id}")
    public ResponseEntity<ManagerDto> getMegetManager(@PathVariable Integer id) throws ResourceNotFoundException {
        return adminService.getMegetManager(id);
    }

    @GetMapping("/get-all-managers")
    public ResponseEntity<List<ManagerDto>> getAllManagers() {
        return adminService.getAllManagers();
    }
    
    
    @DeleteMapping("/delete-instructor/{id}")
    public ResponseEntity<Void> deleteInstructor(@PathVariable Integer id) throws ResourceNotFoundException
    {
        return adminService.deleteInstructor(id);
    }

    @DeleteMapping("/delete-student/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer id) throws ResourceNotFoundException{
        return adminService.deleteStudent(id);
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
