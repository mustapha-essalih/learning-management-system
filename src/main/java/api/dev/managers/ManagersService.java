package api.dev.managers;

import org.springframework.http.ResponseEntity;

import api.dev.courses.model.Courses;
import api.dev.courses.repository.CoursesRepository;
import api.dev.exceptions.ResourceNotFoundException;

public class ManagersService {

    
     private CoursesRepository coursesRepository;

    
    
    public ResponseEntity<?> changeCourseStatus(UpdateCourseStatusDto dto) throws ResourceNotFoundException {
        Courses course = coursesRepository.findById(dto.getCourseId()).orElseThrow(() -> new ResourceNotFoundException("course not found"));

        course.setStatus(dto.getStatus());

        coursesRepository.save(course);

        return ResponseEntity.status(204).build();
    }

}
