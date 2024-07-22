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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "Manager Operations", description = "verify the course and managing course statuses")
@PreAuthorize("hasRole('MANAGER')")
@CrossOrigin("*")
@RequestMapping("/api/managers")
@RestController
public class ManagersController {
    
    private ManagersService managersService;


    public ManagersController(ManagersService managersService) {
        this.managersService = managersService;
    }


    @Operation(
        summary = "Change Course Status",
        description = "Changes the status of a course identified by courseId(PUBLISH OR REJECTED)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Course status updated successfully."),
        @ApiResponse(responseCode = "404", description = "Course not found."),
        @ApiResponse(responseCode = "401", description = "ivalid jwt.")
    })
    @PutMapping("/change-course-status")
    public ResponseEntity<?> changeCourseStatus(@RequestBody UpdateCourseStatusDto dto) throws ResourceNotFoundException{
        return managersService.changeCourseStatus(dto);
    }
    

}
