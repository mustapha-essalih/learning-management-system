package api.dev.admin;

import java.security.Principal;
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
import api.dev.admin.dto.request.ChangePasswordDto;
import api.dev.admin.dto.request.UpdateManagerDto;
import api.dev.admin.dto.response.ManagerDto;
import api.dev.authentication.dto.request.SignupDto;
import api.dev.exceptions.ResourceNotFoundException;
import api.dev.instructors.dto.InstructorAnalyticsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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

 
    @Operation(summary = "Get platform analytics", description = "Returns analytics data for the entire platform.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Platform analytics returned successfully")
    })
    @GetMapping("/platform-analytics")
    public ResponseEntity<PlatformAnalyticsDTO> getPlatformAnalytics() {
        return adminService.getPlatformAnalytics();
    }
    

    @Operation(summary = "Get instructor analytics", description = "Returns analytics data for a specific instructor.")
    @ApiResponses(value = {
       @ApiResponse(responseCode = "200", description = "Instructor analytics returned successfully"),
       @ApiResponse(responseCode = "404", description = "Instructor not found")
    })
    @GetMapping("/get-instructor-analytics/{id}")
    public ResponseEntity<InstructorAnalyticsDTO> getInstructorAnalytics(@PathVariable Integer id) throws ResourceNotFoundException {
        return adminService.getInstructorAnalytics(id);
    }
    

    @Operation(summary = "Get course analytics", description = "Returns analytics data for a specific course of instructor.")
    @ApiResponses(value = {
       @ApiResponse(responseCode = "200", description = "Course analytics returned successfully"),
       @ApiResponse(responseCode = "404", description = "Instructor or course not found")
    })
    @GetMapping("/get-course-analytics/{courseId}/{instructorId}")
    public ResponseEntity<?> getCourseAnalytics(@PathVariable Integer courseId, @PathVariable Integer instructorId) throws ResourceNotFoundException {
        return adminService.getCourseAnalytics(courseId, instructorId);
    }
    

    @Operation(summary = "Create a new manager", description = "Allows admin to create a new manager.")
    @ApiResponses(value = {
       @ApiResponse(responseCode = "201", description = "Manager created successfully"),
       @ApiResponse(responseCode = "400", description = "Email already exists")
    })
    @PostMapping("/new-manager")
    public ResponseEntity<?> createManager(@RequestBody SignupDto dto)
    {
        return adminService.createManager(dto);
    }


   @Operation(summary = "Update manager information", description = "Allows admin to update manager details.")
   @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Manager updated successfully"),
      @ApiResponse(responseCode = "404", description = "Manager not found")
   })
    @PutMapping("/update-manager/{id}")
    public ResponseEntity<Void> updateManager(@PathVariable Integer id, @RequestBody UpdateManagerDto dto) throws ResourceNotFoundException 
    {
        return adminService.updateManager(id, dto);
    }

    @Operation(summary = "Delete manager", description = "Allows admin to delete a manager.")
    @ApiResponses(value = {
       @ApiResponse(responseCode = "204", description = "Manager deleted successfully"),
       @ApiResponse(responseCode = "404", description = "Manager not found")
    })
    @DeleteMapping("/delete-manager/{id}")
    public ResponseEntity<Void> deleteManager(@PathVariable Integer id) throws ResourceNotFoundException{    
        return adminService.deleteManager(id);
    }
    
    @Operation(summary = "Get manager by ID", description = "Returns manager details based on the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Manager details returned successfully"),
            @ApiResponse(responseCode = "404", description = "Manager not found")
    })
    @GetMapping("/get-manager/{id}")
    public ResponseEntity<ManagerDto> getMegetManager(@PathVariable Integer id) throws ResourceNotFoundException {
        return adminService.getMegetManager(id);
    }

    @Operation(summary = "Get all managers", description = "Returns a list of all managers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of managers returned successfully")
    })
    @GetMapping("/get-all-managers")
    public ResponseEntity<List<ManagerDto>> getAllManagers() {
        return adminService.getAllManagers();
    }

    @Operation(summary = "Delete instructor", description = "Allows admin to delete an instructor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Instructor deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Instructor not found")
    })
    @DeleteMapping("/delete-instructor/{id}")
    public ResponseEntity<Void> deleteInstructor(@PathVariable Integer id) throws ResourceNotFoundException {
        return adminService.deleteInstructor(id);
    }

    @Operation(summary = "Delete student", description = "Allows admin to delete a student.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Student deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @DeleteMapping("/delete-student/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer id) throws ResourceNotFoundException {
        return adminService.deleteStudent(id);
    }

    @Operation(summary = "Add a new category", description = "Allows admin to add a new category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category added successfully")
    })
    @PostMapping("/add-category")
    public ResponseEntity<?> addCategory(@RequestParam String category) {
        return adminService.addCategory(category);
    }

    @Operation(summary = "Update category", description = "Allows admin to update an existing category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category updated successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/update-category")
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDto dto) throws ResourceNotFoundException {
        return adminService.updateCategory(dto);
    }

    @Operation(summary = "Delete category", description = "Allows admin to delete an existing category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/delete-category")
    public ResponseEntity<?> deleteCategory(@RequestParam Integer categoryId) throws ResourceNotFoundException {
        return adminService.deleteCategory(categoryId);
    }

    @Operation(summary = "Get all categories", description = "Returns a list of all categories.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of categories returned successfully")
    })
    @GetMapping("/all-categories")
    public ResponseEntity<?> allCategories() {
        return adminService.allCategories();
    }

    @Operation(summary = "Change password", description = "Allows users to change their password.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Password changed successfully"),
        @ApiResponse(responseCode = "400", description = "Old password does not match"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDto dto, Principal principal) {
        return adminService.changePassword(dto, principal.getName());
    }
    

}
