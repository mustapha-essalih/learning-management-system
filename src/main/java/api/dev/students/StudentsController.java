package api.dev.students;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.exception.StripeException;

import api.dev.admin.AdminService;
import api.dev.admin.dto.request.ChangePasswordDto;
import api.dev.exceptions.ResourceNotFoundException;
import api.dev.security.JwtService;
import api.dev.stripe.StripeService;
import api.dev.students.dto.PayementDto;
import api.dev.students.dto.request.FeeadbackDto;

@PreAuthorize("hasRole('STUDENT')")
@CrossOrigin("*")
@RequestMapping("/api/students")
@RestController
public class StudentsController {

    private StudentsService studentsService;
    private StripeService stripeService;
    private AdminService adminService;


    public StudentsController(StudentsService studentsService, StripeService stripeService, AdminService adminService) {
        this.studentsService = studentsService;
        this.stripeService = stripeService;
        this.adminService = adminService;
    }

    @PostMapping("/add-course-to-cart")
    public ResponseEntity<?> addCourseToCart(@RequestParam Integer courseId, Principal principal) throws ResourceNotFoundException {        
        return studentsService.addCourseToCart(courseId, principal.getName());
    }

    @DeleteMapping("/delete-course-from-cart") 
    public ResponseEntity<?> deleteCourseFromCart(@RequestParam Integer courseId, Integer cartId) throws ResourceNotFoundException{
        return studentsService.deleteCourseFromCart(courseId, cartId);
    }


    @PostMapping("/enroll-course")
    public ResponseEntity<?> createCheckoutSession(@RequestBody PayementDto dto, Principal principal) throws ResourceNotFoundException {
      
        try {
            return stripeService.createCheckoutSession( dto.getCourseName(), dto.getAmount(), principal.getName());
             
        } catch (StripeException e) {
            System.out.println(e);
            throw new RuntimeException("Failed to create Stripe session");
        }
    }

    @PostMapping("/add-feedback-to-course")// feeadback enrolled courses
    public ResponseEntity<?> feedbackCourse(@RequestBody FeeadbackDto dto, Principal principal) throws ResourceNotFoundException {
        
        return studentsService.feedbackCourse(dto, principal.getName());
    }
    

    @PostMapping("/update-infos")
    public ResponseEntity<Void> updateInfos(@RequestParam(required = false) String email, @RequestParam(required = false) String fullName,Principal principal) {
        return studentsService.updateInfos(email,fullName,principal.getName());
    }


    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDto dto, Principal principal) {
        return adminService.changePassword(dto, principal.getName());
    }
    
    

}
