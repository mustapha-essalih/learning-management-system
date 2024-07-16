package api.dev.stripe;



import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.RedirectOnCompletion;
import com.stripe.param.checkout.SessionCreateParams.UiMode;

import api.dev.courses.model.Courses;
import api.dev.courses.repository.CoursesRepository;
import api.dev.enums.Status;
import api.dev.exceptions.ResourceNotFoundException;
import api.dev.security.JwtService;
import api.dev.students.StudentsService;
import api.dev.students.model.Students;
import api.dev.students.repository.StudentsRepository;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

 
 
@Service
public class StripeService {

    @Value("${stripe.apikey}")
    private String stripeApiKey;
    
    private StudentsRepository studentsRepository;
    private CoursesRepository coursesRepository;
    private StudentsService studentsService;
    
    public StripeService(StudentsRepository studentsRepository, CoursesRepository coursesRepository,
            StudentsService studentsService, JwtService jwtService) {
        this.studentsRepository = studentsRepository;
        this.coursesRepository = coursesRepository;
        this.studentsService = studentsService;
    }


    // if coure is free or not add it to the student
    // create the order and add it to the student
    // make the payment


    public ResponseEntity<?> createCheckoutSession(String courseName, Long amount, String email)throws StripeException, ResourceNotFoundException 
    {
        Courses course = coursesRepository.findByTitle(courseName).orElseThrow(() -> new ResourceNotFoundException("course not found"));
        Students student = studentsRepository.findByEmail(email).get();
        
        if (!course.getStatus().equals(Status.PUBLISHED)) {
            throw new ResourceNotFoundException("course in review by managers");
        }
        if(addCourseToStudent(course, student))
            return ResponseEntity.ok().body("course enrolled");        

        Stripe.apiKey = stripeApiKey;

        SessionCreateParams params = SessionCreateParams.builder()
        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
        .setMode(SessionCreateParams.Mode.PAYMENT)
        .setUiMode(UiMode.EMBEDDED)
        .setRedirectOnCompletion(RedirectOnCompletion.NEVER)
        .addLineItem(
        SessionCreateParams.LineItem.builder()
                .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(amount * 100)
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName(courseName)
                                                .build())
                                .build())
                .setQuantity(1L)
                .build())
        .build();

        Session session = Session.create(params);
        Map<String, String> responseData = new HashMap<>();
        responseData.put("sessionId", session.getId());
        responseData.put("clientSecret", session.getPaymentIntent());
        
        return ResponseEntity.ok().body(responseData);        
    }


    private boolean addCourseToStudent(Courses course, Students student) throws ResourceNotFoundException {
        
        if (student.getCourses().contains(course)) 
            return true;    

        if (student.getCart() != null) 
            studentsService.deleteCourseFromCart(course.getCourseId() ,student.getCart().getCartId() , student.getUserId());
        
        student.getCourses().add(course);
        studentsRepository.save(student);
        
        if (course.isFree()) 
            return true;
        return false;
    }
}