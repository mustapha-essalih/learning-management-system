package api.dev.managers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.dev.exceptions.ResourceNotFoundException;
import api.dev.managers.dto.request.UpdateCourseStatusDto;

@PreAuthorize("hasRole('MANAGER')")
@CrossOrigin("*")
@RequestMapping("/api/managers")
@RestController
public class ManagersController {
    
    private ManagersService managersService;

    @PutMapping("/change-course-status")
    public ResponseEntity<?> changeCourseStatus(@RequestBody UpdateCourseStatusDto dto) throws ResourceNotFoundException{
        return managersService.changeCourseStatus(dto);
    }
    

}
