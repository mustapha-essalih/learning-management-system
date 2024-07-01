package dev.api.user;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
 
import dev.api.enums.Language;
import dev.api.enums.Level;
import dev.api.model.Chapter;
import dev.api.model.Courses;
import dev.api.repository.CoursesRepository;
import dev.api.user.dao.response.CourseDTO;
import dev.api.user.dto.response.CoursesDAO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UsersService {

    private CoursesRepository coursesRepository;
    private CoursesDAO coursesDAO;


    public ResponseEntity<?> findCourse(String courseName) {
        

         
        return ResponseEntity.status(HttpStatus.OK).body("allCoursesResponse");
    }

    public void filterCourse(String category, BigDecimal minPrice, BigDecimal maxPrice, Level level, Language language) {
        
          coursesDAO.getCoursesByCategoryAndFilters(category, maxPrice, level, language);
        
         // for (Object[] objects : courses) {
        //         System.out.println(objects[0].toString());
        // }
        // List<AllCoursesResponse> allCoursesResponse =  courses.stream().map(course ->  AllCoursesResponse.builder()
        // .courseTitle(course.getTitle())
        // .courseDescription(course.getDescription())
        // .language(course.getLanguage())
        // .courseImage(course.getCourseImage())
        // .lastUpdat(course.getUpdatedAt())
        // .createdBy(course.getUser().getFullName())
        // .chapters(course.getChapters().stream().map(chapter -> ChaptersResponse.builder()
        //         .chapterTitle(chapter.getTitle())
        //         .countResources(chapter.getResources().size())
        //         .resourcesResponses(chapter.getResources().stream().map(resource -> ResourcesResponse.builder()
        //                 .title(resource.getTitle())
        //                 .build())
        //                 .collect(Collectors.toList()))
        //         .build())
        //         .collect(Collectors.toList())) // should collect in evry list 
        // .build())
        // .collect(Collectors.toList());
        // return ResponseEntity.status(HttpStatus.OK).body("allCoursesResponse");
    }

   

    

}
