package api.dev.courses;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.dev.enums.Language;
import api.dev.enums.Level;

import org.springframework.web.bind.annotation.GetMapping;
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



    @GetMapping("/get-courses-filtred") // withtout resources
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

    
}
