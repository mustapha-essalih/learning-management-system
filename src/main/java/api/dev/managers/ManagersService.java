package api.dev.managers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import api.dev.courses.model.Courses;
import api.dev.courses.repository.CoursesRepository;
import api.dev.exceptions.ResourceNotFoundException;
import api.dev.managers.dto.request.UpdateCourseStatusDto;

@Service
public class ManagersService {

    
    private CoursesRepository coursesRepository;

    
    public ManagersService(CoursesRepository coursesRepository) {
        this.coursesRepository = coursesRepository;
    }


    public ResponseEntity<?> changeCourseStatus(UpdateCourseStatusDto dto) throws ResourceNotFoundException {
        Courses course = coursesRepository.findById(dto.getCourseId()).orElseThrow(() -> new ResourceNotFoundException("course not found"));

        course.setStatus(dto.getStatus());

        coursesRepository.save(course);

        return ResponseEntity.status(204).build();
    }

}
