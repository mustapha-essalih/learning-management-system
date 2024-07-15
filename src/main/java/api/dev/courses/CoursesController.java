package api.dev.courses;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.dev.courses.dto.CourseDTO;
import api.dev.enums.Language;
import api.dev.enums.Level;
import api.dev.exceptions.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Tag(name = "Course Management", description = "Endpoints to manage course operations, including fetching course feedback, filtering courses, retrieving course details, finding courses by name, and accessing course resources.")
@CrossOrigin("*")
@RequestMapping("/api/courses")
@RestController
public class CoursesController {

    private CoursesService coursesService;

    public CoursesController(CoursesService coursesService) {
        this.coursesService = coursesService;
    }

    @Operation(summary = "Get feedbacks of a course", description = "Returns average rating of a course based on its feedbacks.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Average rating returned successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @GetMapping("/feedbacks-of-course/{courseId}")
    public ResponseEntity<?> getFeedbacksOfCourse(@PathVariable Integer courseId) throws ResourceNotFoundException {
        return coursesService.getFeedbacksOfCourse(courseId);
    }

    @Operation(summary = "Get filtered courses", description = "Returns a list of courses based on specified filters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered courses returned successfully")
    })
    @GetMapping("/get-courses-filtred")  
    public ResponseEntity<List<CourseDTO>> getCoursesFiltered(@RequestParam(required = false) String category ,@RequestParam(required = false) BigDecimal minPrice,@RequestParam(required = false) BigDecimal maxPrice,
    @RequestParam(required = false) Language language,@RequestParam(required = false) Level level, @RequestParam(required = false) Double minFeedback, @RequestParam(required = false) Double maxFeedback, 
    @RequestParam(required = false) Boolean isFree) 
    {
        return this.coursesService.getCoursesFiltered(category, minPrice, maxPrice,language, level, minFeedback, maxFeedback, isFree);    
    }
    

    @Operation(summary = "Get course details", description = "Returns details of a specific course.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course details returned successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @GetMapping("/get-course-details/{courseId}")
    public ResponseEntity<?> getCourseDetails(@PathVariable Integer courseId) throws ResourceNotFoundException 
    {
        return coursesService.getCourseDetails(courseId);
    }
    
    @Operation(summary = "Find course by name", description = "Returns courses matching the provided name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses found successfully"),
            @ApiResponse(responseCode = "204", description = "No course found with the given name")
    })
    @GetMapping("/find-course-by-name") // fetch courses without resources(images and videos) 
    public ResponseEntity<?> findCourseByName(@RequestParam String courseName) {
        return coursesService.findCourseByName(courseName);
    }
    

    @Operation(summary = "Get all courses", description = "Returns a list of all courses.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of courses returned successfully")
    })
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/get-all-courses")
    public ResponseEntity<?> getAllCourses() { // get courses with resources
        return coursesService.getAllCourses();
    }
    
    /*
        1.  Fetch the course details, including the paths to all videos and images.
        2.  Fetch videos and images in  as needed, instead of all at once
     */
   
    
     @Operation(summary = "Get resource", description = "Returns a specific resource of a course.")
     @ApiResponses(value = {
             @ApiResponse(responseCode = "200", description = "Resource returned successfully"),
             @ApiResponse(responseCode = "404", description = "Resource not found"),
             @ApiResponse(responseCode = "403", description = "Access denied")
     })
    @GetMapping("/get-resource/{courseId}") // fetch resource(images and videos) for just students who enroll the course + INSTRUCTOR of the ccourse + ADMIN + MANAGERS
    public ResponseEntity<?> getResource(@PathVariable Integer courseId, @RequestParam String filePath, @RequestParam String contentType, Principal principal) throws ResourceNotFoundException, AccessDeniedException  {

        return coursesService.getResource(courseId, filePath, contentType, principal.getName());
    }

    
    
}
