package dev.api.managers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dev.api.exception.ResourceNotFoundException;
import dev.api.managers.dto.UpdateCourseStatusDto;
import dev.api.model.Courses;
import dev.api.model.Resources;
import dev.api.repository.CoursesRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ManagersService {
        
    private CoursesRepository coursesRepository;

    
    
    public ResponseEntity<?> changeCourseStatus(UpdateCourseStatusDto dto) throws ResourceNotFoundException {
        Courses course = coursesRepository.findById(dto.getCourseId()).orElseThrow(() -> new ResourceNotFoundException("course not found"));

        course.setStatus(dto.getStatus());

        coursesRepository.save(course);

        return ResponseEntity.status(204).build();
    }
    
}
