package dev.api.managers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.api.exception.ResourceNotFoundException;
import dev.api.managers.dto.UpdateCourseStatusDto;
import lombok.AllArgsConstructor;

@PreAuthorize("hasRole('MANAGER')")
@CrossOrigin("*")
@RequestMapping("/api/managers")
@AllArgsConstructor
@RestController
public class ManagersController {
    
    private ManagersService managersService;

    @PutMapping("/changeCourseStatus")
    public ResponseEntity<?> changeCourseStatus(@RequestBody UpdateCourseStatusDto dto) throws ResourceNotFoundException{
        return managersService.changeCourseStatus(dto);
    }
    

}
