package api.dev.stripe;



import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;

import api.dev.authentication.repository.UserRepository;
import api.dev.courses.model.Courses;
import api.dev.courses.repository.CoursesRepository;
import api.dev.exceptions.ResourceNotFoundException;
import api.dev.students.StudentsService;
import api.dev.students.model.Orders;
import api.dev.students.model.Students;
import api.dev.students.repository.StudentsRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

 
@Service
public class StripeService {

    @Value("${stripe.apikey}")
    private String stripeApiKey;
    
    private StudentsRepository studentsRepository;
    private CoursesRepository coursesRepository;
    private StudentsService studentsService;


    

    public StripeService(StudentsRepository studentsRepository, CoursesRepository coursesRepository,
            StudentsService studentsService) {
        this.studentsRepository = studentsRepository;
        this.coursesRepository = coursesRepository;
        this.studentsService = studentsService;
    }


    public ResponseEntity<?> createCheckoutSession(String successUrl, String cancelUrl, String courseName, Long amount, String email)throws StripeException, ResourceNotFoundException 
    {
        Courses course = coursesRepository.findByTitle(courseName).orElseThrow(() -> new ResourceNotFoundException("course not found"));
        Students student = studentsRepository.findByEmail(email).get();
        Set<Courses> courses = new HashSet<>();
        courses.add(course);
        if (course.isFree()) 
        {
            if(student.getCourses() == null)
                student.setCourses(courses);
            else
                student.getCourses().add(course);
            studentsRepository.save(student);
            return ResponseEntity.ok().build();
        }

        Stripe.apiKey = stripeApiKey;
        CustomerCreateParams customerCreateParams = CustomerCreateParams.builder().setDescription(courseName).build();

        Customer customer = Customer.create(customerCreateParams);

        SessionCreateParams params = SessionCreateParams.builder()
        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
        .setMode(SessionCreateParams.Mode.PAYMENT)
        .setSuccessUrl(successUrl)
        .setCancelUrl(cancelUrl)
        .setCustomer(customer.getId())
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
        if(student.getCourses() == null)
            student.setCourses(courses);
        else
            student.getCourses().add(course);
        studentsRepository.save(student);
        return ResponseEntity.ok().body(responseData);        
    }


    public ResponseEntity<?> successPayment(String sessionId) throws ResourceNotFoundException {
            
        try {
            Session session = Session.retrieve(sessionId);
            String email = session.getCustomerDetails().getEmail();
            String customerId = session.getCustomer();
            Customer customer = Customer.retrieve(customerId);
            
            String courseName = customer.getDescription();
            Students student = studentsRepository.findByEmail(email).orElseThrow(() -> new BadCredentialsException("user not found"));
            Courses course = coursesRepository.findByTitle(courseName).orElseThrow(() -> new ResourceNotFoundException("course not found"));

            Set<Courses> listOfcourse = new HashSet<>();
            Set<Orders> listOfOrders = new HashSet<>();
            
            listOfcourse.add(course);

            Orders newOrder = new Orders(student,listOfcourse, true, course.getPrice());

            listOfOrders.add(newOrder);
            if (student.getOrders().isEmpty()) 
                student.setOrders(listOfOrders);
            else 
                student.getOrders().add(newOrder);

            if (student.getCart() != null) 
                studentsService.deleteCourseFromCart(student.getCart().getCartId(), course.getCourseId());
           
            studentsRepository.save(student);

            return ResponseEntity.status(201).body("Payment successful and order created!");
        } catch (StripeException e) {
            System.out.println(e);
            return ResponseEntity.status(400).body("Payment failed!");

        }

    }
}