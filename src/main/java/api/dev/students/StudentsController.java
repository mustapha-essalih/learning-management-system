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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.exception.StripeException;

import api.dev.exceptions.ResourceNotFoundException;
import api.dev.stripe.StripeService;
import api.dev.students.dto.PayementDto;

@PreAuthorize("hasRole('STUDENT')")
@CrossOrigin("*")
@RequestMapping("/api/students")
@RestController
public class StudentsController {

    private StudentsService studentsService;
    private StripeService stripeService;



    public StudentsController(StudentsService studentsService, StripeService stripeService) {
        this.studentsService = studentsService;
        this.stripeService = stripeService;
    }

    @PostMapping("/add-course-to-cart")
    public ResponseEntity<?> addCourseToCart(@RequestParam Integer courseId, Principal principal) throws ResourceNotFoundException {        
        return studentsService.addCourseToCart(courseId, principal.getName());
    }

    @DeleteMapping("/delete-course-from-cart") // use dto
    public ResponseEntity<?> deleteCourseFromCart(@RequestParam Integer courseId, Integer cartId) throws ResourceNotFoundException{
        return studentsService.deleteCourseFromCart(courseId, cartId);
    }


    @PostMapping("/enroll-course")
    public ResponseEntity<?> createCheckoutSession(@RequestBody PayementDto dto, Principal principal) throws ResourceNotFoundException {


        String courseName = dto.getCourseName();
        Long amount = dto.getAmount();

        String successUrl = "http://localhost:8080/api/students/success-payment?sessionId={CHECKOUT_SESSION_ID}";
        String cancelUrl = "http://localhost:8080/api/checkout/error-payment";

        try {
            return stripeService.createCheckoutSession(successUrl, cancelUrl, courseName, amount, principal.getName());
             
        } catch (StripeException e) {
            System.out.println(e);
            throw new RuntimeException("Failed to create Stripe session");
        }
    }

    @GetMapping("/success-payment")
    public ResponseEntity<?> successPayment(@RequestParam String sessionId) throws ResourceNotFoundException {
        
        return stripeService.successPayment(sessionId);
    }


    @GetMapping("/error-payment")
    public ResponseEntity<?> errorPayment() {
        return ResponseEntity.badRequest().build();
    }
    
    
}
