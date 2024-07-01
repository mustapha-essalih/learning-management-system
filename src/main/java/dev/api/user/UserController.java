package dev.api.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.api.enums.Language;
import dev.api.enums.Level;
import dev.api.model.Courses;
import dev.api.user.dto.response.CoursesDAO;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private UsersService userService;

    // @GetMapping("/findCourse")
    // public ResponseEntity<?> findCourse(@RequestParam String courseName) {
    //     return userService.findCourse(courseName);
    // }

    // @GetMapping("/filterCourse")// use dto
    // public void filterCourse(@RequestParam String category ,@RequestParam(required = false) BigDecimal minPrice,@RequestParam(required = false) BigDecimal maxPrice,@RequestParam(required = false) Language language,@RequestParam(required = false) Level level) {

    //      userService.filterCourse(category, minPrice, maxPrice, level, language);
    // }
}
