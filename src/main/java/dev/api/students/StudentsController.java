package dev.api.students;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;

import dev.api.exception.ResourceNotFoundException;
import dev.api.model.Courses;
import dev.api.model.Orders;
import dev.api.model.User;
import dev.api.payment.PayementDto;
import dev.api.payment.StripeService;
import dev.api.repository.CoursesRepository;
import dev.api.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;


// @PreAuthorize("hasRole('STUDENT')")
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping("/api/students")
@RestController
public class StudentsController {

    private StudentsService studentsService;
    private StripeService stripeService;
    
    
    @PutMapping("/toTeacher")
    public ResponseEntity<?> switchToTeacherRole(@RequestParam Integer userId) {
 
        return studentsService.switchToTeacherRole(userId);
    }


    @PostMapping("/add-course-to-cart")
    public ResponseEntity<?> addCourseToCart(@RequestParam Integer courseId, @RequestParam Integer userId) throws ResourceNotFoundException {
        
        return studentsService.addCourseToCart(courseId, userId);
    }
    

    @DeleteMapping("/delete-course-from-cart") // use dto
    public ResponseEntity<?> deleteCourseFromCart(@RequestParam Integer courseId, @RequestParam Integer cartId) throws ResourceNotFoundException{
        return studentsService.deleteCourseFromCart(cartId,courseId);
    }
    

    public PaymentIntent createPaymentIntent(Long amount, String currency) throws StripeException {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency(currency)
                .build();

            return PaymentIntent.create(params);
        }

    @PostMapping("/buy-course")
    public Map<String, String> createCheckoutSession(@RequestBody PayementDto dto) {

        String courseName = dto.getCourseName();
        Long amount = dto.getAmount();

        String successUrl = "http://localhost:8080/api/students/successPayment?sessionId={CHECKOUT_SESSION_ID}";
        String cancelUrl = "http://localhost:8080/api/checkout/errorPayment";

        try {
            return stripeService.createCheckoutSession(successUrl, cancelUrl, courseName, amount);
             
        } catch (StripeException e) {
            System.out.println(e);
            throw new RuntimeException("Failed to create Stripe session");
        }
    }

    @GetMapping("/successPayment")
    public ResponseEntity<?> successPayment(@RequestParam String sessionId) throws ResourceNotFoundException {
        
        return stripeService.successPayment(sessionId);
    }

    @GetMapping("/errorPayment")
    public ResponseEntity<?> errorPayment() {
        return ResponseEntity.badRequest().build();
    }
    

}
// Courses courses = coursesRepository.findById(1).get();
                // byte[] file = FileUtils.returnFileFromStorage( System.getProperty("user.dir") + "\\uploads\\" + courses.getCourseImage());
        // return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf(courses.getContentType())).body(file);

        