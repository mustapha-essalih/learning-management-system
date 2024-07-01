package dev.api.teacher;

import java.security.Principal;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import dev.api.exception.ResourceNotFoundException;
import jakarta.servlet.ServletRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@CrossOrigin("*")
@PreAuthorize("hasRole('TEACHER')")
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    private TeacherService teacherService;
   
   
    
    @PostMapping("/upload-course")
    public ResponseEntity<?> uploadCourse(MultipartHttpServletRequest request)
    { 
        return teacherService.uploadCourse(request);
    }

    @PutMapping("/update-course")
    public ResponseEntity<?> updateCourse(ServletRequest request , @RequestParam(required = false) MultipartFile courseImage , @RequestParam(required = false) String courseImageTitle, Authentication user) throws NumberFormatException, ResourceNotFoundException{
        return teacherService.updateCourse(request, courseImage, courseImageTitle, user);
    }


    @DeleteMapping("/delete-course")
    public ResponseEntity<?> deleteCourse(@RequestParam Integer courseId) throws ResourceNotFoundException
    {
        return teacherService.deleteCourse(courseId);
    }
   
    @PutMapping("/add-chapter")
    public ResponseEntity<?> addChapter(@RequestParam String chapterTitle, @RequestParam List<MultipartFile> files , @RequestParam List<String> resourceTitle , @RequestParam Integer courseId,  Authentication authenticatedUser) throws ResourceNotFoundException{
        return teacherService.addChapter(chapterTitle, files, resourceTitle, courseId,authenticatedUser);
    }

    @PutMapping("/update-chapter")
    public ResponseEntity<?> updateChapter(@RequestParam Integer courseId, @RequestParam String chapterTitle, @RequestParam Integer chapterId,  Authentication authenticatedUser) throws ResourceNotFoundException{

        return teacherService.updateChapter(chapterTitle , chapterId,courseId ,authenticatedUser);
    }


    @DeleteMapping("/delete-chapter")
    public ResponseEntity<?> deleteChapter(@RequestParam Integer courseId,@RequestParam Integer chapterId, Authentication authenticatedUser) throws ResourceNotFoundException
    {
        return teacherService.deleteChapter(chapterId, courseId, authenticatedUser);
    }

    @PutMapping("/add-resourse")
    public ResponseEntity<?> addResourse(@RequestParam MultipartFile resource , @RequestParam String resourceTitle, @RequestParam Integer chapterId, @RequestParam Integer courseId, Authentication authentication) throws ResourceNotFoundException{
        return teacherService.addResourse(resource,resourceTitle , chapterId, courseId,authentication);        
    }
    
    @PutMapping("/update-resourse")
    public ResponseEntity<?> updateResourse(@RequestParam MultipartFile resource , @RequestParam String resourceTitle, @RequestParam Integer resourseId, @RequestParam Integer courseId,Authentication authentication) throws ResourceNotFoundException{

        return teacherService.updateResourse(resource,resourceTitle , resourseId, courseId, authentication);  // add also isfree , duration      
    }
    
    @DeleteMapping("/delete-resource")
    public ResponseEntity<?> deleteResource( @RequestParam Integer resourseId, @RequestParam Integer courseId,Authentication authenticatedUser) throws ResourceNotFoundException
    {
        return teacherService.deleteResource(resourseId, courseId, authenticatedUser);
    }

    @PutMapping("/to-student")
    public ResponseEntity<?> switchToStudentRole(@RequestParam Integer userId) {
        return teacherService.switchToStudentRole(userId);
    }

    // @GetMapping("/getAllCourses")
    // public ResponseEntity<?> getAllCources() 
    // {    
    //     // Courses courses = coursesRepository.findById(1).get();
        
    //     // byte[] file = FileUtils.returnFileFromStorage( System.getProperty("user.dir") + "\\uploads\\" + courses.getCourseImage());
        
    //     // return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf(courses.getContentType())).body(file);
        
    //     return teacherService.getAllCources();
        
    // }
}



