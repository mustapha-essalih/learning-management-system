package api.dev.courses;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.dev.courses.dto.request.GetResourceDto;
import api.dev.enums.Language;
import api.dev.enums.Level;
import api.dev.exceptions.ResourceNotFoundException;
import api.dev.utils.FileUtils;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin("*")
@RequestMapping("/api/courses")
@RestController
public class CoursesController {

    private CoursesService coursesService;

    

    // get courses filtred without resources , just chpaters title and resources titles

    public CoursesController(CoursesService coursesService) {
        this.coursesService = coursesService;
    }



    @GetMapping("/get-courses-filtred")  
    public ResponseEntity<?> getCoursesFiltered(@RequestParam String category ,@RequestParam(required = false) BigDecimal minPrice,@RequestParam(required = false) BigDecimal maxPrice,
    @RequestParam(required = false) Language language,@RequestParam(required = false) Level level, @RequestParam(required = false) Double minFeedback, @RequestParam(required = false) Double maxFeedback, 
    @RequestParam(required = false) Boolean isFree) 
    {
        return this.coursesService.getCoursesFiltered(category, minPrice, maxPrice,language, level, minFeedback, maxFeedback, isFree);    
    }
    


    // @GetMapping("/filterCourse")// use dto
    // public void filterCourse(@RequestParam String category ,@RequestParam(required = false) BigDecimal minPrice,@RequestParam(required = false) BigDecimal maxPrice,@RequestParam(required = false) Language language,@RequestParam(required = false) Level level) {

    //      userService.filterCourse(category, minPrice, maxPrice, level, language);
    // }


    @GetMapping("/feedbacks-of-course")
    public ResponseEntity<?> getFeedbacksOfCourse(@RequestParam Integer courseId) throws ResourceNotFoundException {
        return coursesService.getFeedbacksOfCourse(courseId);
    }

    @GetMapping("/get-course-details")// fetch course without resources(images and videos) 
    public ResponseEntity<?> getCourseDetails(@RequestParam Integer courseId) throws ResourceNotFoundException 
    {
        return coursesService.getCourseDetails(courseId);
    }
    
    
    @GetMapping("/find-course-by-name") // fetch courses without resources(images and videos) 
    public ResponseEntity<?> findCourseByName(@RequestParam String courseName) {
        return coursesService.findCourseByName(courseName);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/get-all-courses")
    public ResponseEntity<?> getAllCourses() { // get courses with resources
        return coursesService.getAllCourses();
    }
    
    /*
        1.  Fetch the course details, including the paths to all videos and images.
        2.  Fetch videos and images in  as needed, instead of all at once
     */
   
    
    @GetMapping("/get-resource") // fetch resource(images and videos) for just students who enroll the course + INSTRUCTOR of the ccourse + ADMIN + MANAGERS
    public ResponseEntity<?> getResource(@RequestParam Integer courseId, @RequestParam String filePath, @RequestParam String contentType, Principal principal) throws ResourceNotFoundException, AccessDeniedException  {

        return coursesService.getResource(courseId, filePath, contentType, principal.getName());
    }
    
}
