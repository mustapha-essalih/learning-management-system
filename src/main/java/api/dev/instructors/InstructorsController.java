package api.dev.instructors;

import java.security.Principal;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import api.dev.admin.AdminService;
import api.dev.admin.dto.request.ChangePasswordDto;
import api.dev.exceptions.ResourceNotFoundException;
import api.dev.instructors.dto.InstructorAnalyticsDTO;
import api.dev.instructors.dto.request.UpdateChapterTitleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.ServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@CrossOrigin("*")
@PreAuthorize("hasRole('INSTRUCTOR')")
@RequestMapping("/api/instructor")
@RestController
public class InstructorsController {
    

    private InstructorsService instructorsService;
    private AdminService adminService;
    

    public InstructorsController(InstructorsService instructorsService, AdminService adminService) {
        this.instructorsService = instructorsService;
        this.adminService = adminService;
    }

    @Operation(summary = "Upload a new course", description = "Allows instructors to upload a new course with details, chapters, and resources. The request should be a multipart form data containing course information, chapter titles, resource files, and resource titles.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Course uploaded successfully."),
        @ApiResponse(responseCode = "400", description = "Bad request. Missing required fields or invalid data format."),
    })
    @PostMapping("/upload-course")
    public ResponseEntity<Void> uploadCourse(MultipartHttpServletRequest request) throws ResourceNotFoundException, BadRequestException
    { 
        return instructorsService.uploadCourse(request);
    }

    @Operation(summary = "Update a course", description = "Allows instructors to update the details of a course including title, description, category, etc.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Course updated successfully"),
        @ApiResponse(responseCode = "400", description = "Course is not related with this instructor"),
        @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @PutMapping("/update-course/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Integer id, ServletRequest request, @RequestParam(required = false) MultipartFile courseImage, Principal principal) throws NumberFormatException, ResourceNotFoundException, BadRequestException {
        return instructorsService.updateCourse(id, request, courseImage, principal);
    }

    @Operation(summary = "Delete a course", description = "Allows instructors to delete a course.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Course deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Course is not related with this instructor"),
        @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @DeleteMapping("/delete-course/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Integer id, Principal principal) throws ResourceNotFoundException {
        return instructorsService.deleteCourse(id, principal);
    }

    @Operation(summary = "Add a chapter to a course", description = "Allows instructors to add a new chapter with resources to a course.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Chapter added successfully"),
        @ApiResponse(responseCode = "400", description = "Course is not related with this instructor"),
        @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @PutMapping("/add-chapter")
    public ResponseEntity<?> addChapter(@RequestParam String chapterTitle, @RequestParam List<MultipartFile> files, @RequestParam List<String> resourceTitle, @RequestParam Integer courseId, Principal principal) throws ResourceNotFoundException {
        return instructorsService.addChapter(chapterTitle, files, resourceTitle, courseId, principal);
    }   

    @Operation(summary = "Update chapter title", description = "Allows instructors to update the title of a chapter.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Chapter title updated successfully"),
        @ApiResponse(responseCode = "400", description = "Course is not related with this instructor"),
        @ApiResponse(responseCode = "404", description = "Chapter not found")
    })
    @PutMapping("/update-chapter-title")
    public ResponseEntity<?> updateChapterTitle(@RequestBody UpdateChapterTitleDto dto, Principal principal) throws ResourceNotFoundException {
        return instructorsService.updateChapter(dto, principal);
    }


    @Operation(summary = "Delete a chapter", description = "Allows instructors to delete a chapter from a course.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Chapter deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Course is not related with this instructor"),
        @ApiResponse(responseCode = "404", description = "Chapter not found")
    })
    @DeleteMapping("/delete-chapter/{id}")
    public ResponseEntity<?> deleteChapter(@PathVariable Integer id, Principal principal) throws ResourceNotFoundException {
        return instructorsService.deleteChapter(id, principal);
    }


    @Operation(summary = "Add a resource to a chapter", description = "Allows instructors to add a new resource to a chapter.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Resource added successfully"),
        @ApiResponse(responseCode = "400", description = "Course is not related with this instructor"),
        @ApiResponse(responseCode = "404", description = "Chapter not found")
    })
    @PutMapping("/add-resourse/{id}")
    public ResponseEntity<?> addResourse(@PathVariable Integer id, @RequestParam MultipartFile file, @RequestParam String resourceTitle, Principal principal) throws ResourceNotFoundException {
        return instructorsService.addResourse(file, resourceTitle, id, principal);
    }


    @Operation(summary = "Update a resource", description = "Allows instructors to update the details of a resource in a chapter.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Resource updated successfully"),
        @ApiResponse(responseCode = "400", description = "Course is not related with this instructor"),
        @ApiResponse(responseCode = "404", description = "Resource not found")
    })
    @PutMapping("/update-resourse/{id}")
    public ResponseEntity<?> updateResourse(@PathVariable Integer id, @RequestParam(required = false) MultipartFile file, @RequestParam(required = false) String resourceTitle, Principal principal) throws ResourceNotFoundException {
        return instructorsService.updateResourse(file, resourceTitle, id, principal);
    }

    @Operation(summary = "Delete a resource", description = "Allows instructors to delete a resource from a chapter.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Resource deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Course is not related with this instructor"),
        @ApiResponse(responseCode = "404", description = "Resource not found")
    })
    @DeleteMapping("/delete-resource/{id}")
    public ResponseEntity<?> deleteResource(@PathVariable Integer id, Principal principal) throws ResourceNotFoundException {
        return instructorsService.deleteResource(id, principal);
    }

    @Operation(summary = "Get all courses", description = "Returns all courses associated with the instructor.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of courses returned successfully")
    })
    @GetMapping("/get-all-courses")
    public ResponseEntity<?> getCourses(Principal principal) {
        return instructorsService.getCourses(principal.getName());
    }


    @Operation(summary = "Get course analytics", description = "Returns analytics data for a specific course.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Course analytics returned successfully"),
        @ApiResponse(responseCode = "400", description = "Course is not related with this instructor"),
        @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @GetMapping("/course-analytics/{courseId}")
    public ResponseEntity<?> getCourseAnalytics(@PathVariable Integer courseId, Principal principal) throws ResourceNotFoundException {
        return instructorsService.getCourseAnalytics(courseId, principal.getName());
    }

    
    @Operation(summary = "Get instructor analytics", description = "Returns analytics data for the instructor.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Instructor analytics returned successfully")
    })
    @GetMapping("/get-instructor-analytics")
    public ResponseEntity<InstructorAnalyticsDTO> getInstructorAnalytics(Principal principal) {
        return instructorsService.getInstructorAnalytics(principal.getName());
    }


    @Operation(summary = "Update instructor information", description = "Allows instructors to update their email, full name, or bio.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Instructor information updated successfully")
    })
    @PostMapping("/update-infos")
    public ResponseEntity<Void> updateInfos(@RequestParam(required = false) String email, @RequestParam(required = false) String fullName, @RequestParam(required = false) String bio, Principal principal) {
        return instructorsService.updateInfos(email, fullName, bio, principal.getName());
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



// user update password