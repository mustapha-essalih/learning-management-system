package api.dev.instructors;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

import api.dev.exceptions.ResourceNotFoundException;
import api.dev.instructors.dto.request.DeleteChapterDto;
import api.dev.instructors.dto.request.UpdateChapterTitleDto;
import jakarta.servlet.ServletRequest;
import org.springframework.web.bind.annotation.GetMapping;



@CrossOrigin("*")
@PreAuthorize("hasRole('INSTRUCTOR')")
@RequestMapping("/api/instructor")
@RestController
public class InstructorsController {
    

    private InstructorsService instructorsService;
    
    public InstructorsController(InstructorsService instructorsService) {
        this.instructorsService = instructorsService;
    }


    @PostMapping("/upload-course")
    public ResponseEntity<?> uploadCourse(MultipartHttpServletRequest request)
    { 
        return instructorsService.uploadCourse(request);
    }

    @PutMapping("/update-course")
    public ResponseEntity<?> updateCourse(ServletRequest request , @RequestParam(required = false) MultipartFile courseImage, Principal principal) throws NumberFormatException, ResourceNotFoundException{
        return instructorsService.updateCourse(request,courseImage,principal);  
    }

    @DeleteMapping("/delete-course")
    public ResponseEntity<?> deleteCourse(@RequestParam Integer courseId,  Principal principal) throws ResourceNotFoundException
    {
        return instructorsService.deleteCourse(courseId,principal);
    }

    @PutMapping("/add-chapter")
    public ResponseEntity<?> addChapter(@RequestParam String chapterTitle, @RequestParam List<MultipartFile> files , @RequestParam List<String> resourceTitle , @RequestParam Integer courseId,  Principal principal) throws ResourceNotFoundException{
        return instructorsService.addChapter(chapterTitle, files, resourceTitle, courseId,principal);
    }

    @PutMapping("/update-chapter-title")
    public ResponseEntity<?> updateChapterTitle(@RequestBody UpdateChapterTitleDto dto,  Principal principal) throws ResourceNotFoundException{

        return instructorsService.updateChapter(dto , principal);
    }

    @DeleteMapping("/delete-chapter")
    public ResponseEntity<?> deleteChapter(@RequestBody DeleteChapterDto dto, Principal principal) throws ResourceNotFoundException
    {
        return instructorsService.deleteChapter(dto, principal);
    }


    @PutMapping("/add-resourse")
    public ResponseEntity<?> addResourse(@RequestParam MultipartFile file , @RequestParam String resourceTitle, @RequestParam Integer chapterId, Principal principal) throws ResourceNotFoundException{
        return instructorsService.addResourse(file,resourceTitle, chapterId, principal);        
    }


    @PutMapping("/update-resourse")
    public ResponseEntity<?> updateResourse(@RequestParam(required = false) MultipartFile file , @RequestParam(required = false) String resourceTitle, @RequestParam(required = false) Integer resourseId, Principal principal ) throws ResourceNotFoundException{

        return instructorsService.updateResourse(file,resourceTitle , resourseId, principal);        
    }

    @DeleteMapping("/delete-resource")
    public ResponseEntity<?> deleteResource(@RequestParam Integer resourseId, Principal principal) throws ResourceNotFoundException
    {
        return instructorsService.deleteResource(resourseId, principal);
    }

    @GetMapping("/get-all-courses")
    public ResponseEntity<?> getCourses(Principal principal) {
        return instructorsService.getCourses(principal.getName());
    }
    

}
