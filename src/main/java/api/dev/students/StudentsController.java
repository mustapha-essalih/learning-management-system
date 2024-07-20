package api.dev.students;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.exception.StripeException;

import api.dev.admin.AdminService;
import api.dev.admin.dto.request.ChangePasswordDto;
import api.dev.exceptions.ResourceNotFoundException;
import api.dev.stripe.StripeService;
import api.dev.students.dto.PayementDto;
import api.dev.students.dto.request.FeeadbackDto;
import api.dev.students.model.Students;
import api.dev.students.repository.StudentsRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "Student Operations", description = "Endpoints for students to manage their courses, cart, feedback, and personal information.")
@PreAuthorize("hasRole('STUDENT')")
@CrossOrigin("*")
@RequestMapping("/api/students")
@RestController
public class StudentsController {

    private StudentsService studentsService;
    private StripeService stripeService;
    private AdminService adminService;
    private StudentsRepository studentsRepository;


    public StudentsController(StudentsService studentsService, StripeService stripeService, AdminService adminService,
            StudentsRepository studentsRepository) {
        this.studentsService = studentsService;
        this.stripeService = stripeService;
        this.adminService = adminService;
        this.studentsRepository = studentsRepository;
    }

    @Operation(
        summary = "Add Course to Cart",
        description = "Adds a course to the student's cart."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Course added to cart successfully."),
        @ApiResponse(responseCode = "404", description = "Course is not published, free, or not found."),
        @ApiResponse(responseCode = "401", description = "ivalid jwt.")
    })
    @PostMapping("/add-course-to-cart/{courseId}")
    public ResponseEntity<?> addCourseToCart(@PathVariable Integer courseId, Principal principal) throws ResourceNotFoundException {        
        return studentsService.addCourseToCart(courseId, principal.getName());
    }

    @Operation(
        summary = "Delete Course from Cart",
        description = "Deletes a course from the student's cart."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Course deleted from cart successfully."),
        @ApiResponse(responseCode = "404", description = "Cart or course not found."),
        @ApiResponse(responseCode = "401", description = "ivalid jwt.")
    })
    @DeleteMapping("/delete-course-from-cart/{courseId}/{cartId}") 
    public ResponseEntity<?> deleteCourseFromCart(@PathVariable Integer courseId,@PathVariable Integer cartId, Principal principal) throws ResourceNotFoundException{
        Students student = studentsRepository.findByEmail(principal.getName()).get();
        return studentsService.deleteCourseFromCart(courseId, cartId,student.getUserId() );
    }


    @Operation(
        summary = "Create Checkout Session for Course Enrollment",
        description = "Initiates a checkout session for enrolling in a course."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stripe session created successfully."),
        @ApiResponse(responseCode = "500", description = "Failed to create Stripe session."),
        @ApiResponse(responseCode = "401", description = "ivalid jwt.")
    })
    @PostMapping("/enroll-course")
    public ResponseEntity<?> createCheckoutSession(@RequestBody PayementDto dto, Principal principal) throws ResourceNotFoundException {
      
        try {
            return stripeService.createCheckoutSession( dto.getCourseName(), dto.getAmount(), principal.getName());             
        } catch (StripeException e) {
            throw new RuntimeException("Failed to create Stripe session");
        }
    }


    @Operation(
        summary = "Add Feedback to Course",
        description = "Allows students to provide feedback on enrolled courses."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Feedback added successfully."),
        @ApiResponse(responseCode = "400", description = "Student is not enrolled in the course."),
        @ApiResponse(responseCode = "401", description = "ivalid jwt.")
    })
    @PostMapping("/add-feedback-to-course")
    public ResponseEntity<?> feedbackCourse(@RequestBody FeeadbackDto dto, Principal principal) throws ResourceNotFoundException {
        
        return studentsService.feedbackCourse(dto, principal.getName());
    }
    

    @Operation(
        summary = "Update Student Information",
        description = "Allows students to update their email and full name."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Student information updated successfully."),
        @ApiResponse(responseCode = "401", description = "ivalid jwt.")
    })
    @PostMapping("/update-infos")
    public ResponseEntity<Void> updateInfos(@RequestParam(required = false) String email, @RequestParam(required = false) String fullName,Principal principal) {
        return studentsService.updateInfos(email,fullName,principal.getName());
    }


    @Operation(summary = "Change password", description = "Allows users to change their password.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Password changed successfully"),
        @ApiResponse(responseCode = "400", description = "Old password does not match"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "401", description = "ivalid jwt.")
    })
    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDto dto, Principal principal) {
        return adminService.changePassword(dto, principal.getName());
    }
     
}
