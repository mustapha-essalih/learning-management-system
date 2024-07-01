package dev.api.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;

import dev.api.exception.ResourceNotFoundException;
import dev.api.model.Courses;
import dev.api.model.Orders;
import dev.api.model.User;
import dev.api.repository.CoursesRepository;
import dev.api.repository.UserRepository;
import dev.api.students.StudentsService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

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
    
    private UserRepository userRepository;
    private CoursesRepository coursesRepository;
    private StudentsService studentsService;


    

    public StripeService(UserRepository userRepository, CoursesRepository coursesRepository,
            StudentsService studentsService) {
        this.userRepository = userRepository;
        this.coursesRepository = coursesRepository;
        this.studentsService = studentsService;
    }


    public Map<String, String> createCheckoutSession(String successUrl, String cancelUrl, String courseName, Long amount)throws StripeException 
    {
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

            return responseData;        
    }


    public ResponseEntity<?> successPayment(String sessionId) throws ResourceNotFoundException {
            
        try {
            Session session = Session.retrieve(sessionId);
            String email = session.getCustomerDetails().getEmail();
            String customerId = session.getCustomer();
            Customer customer = Customer.retrieve(customerId);
            
            String courseName = customer.getDescription();
            User student = userRepository.findByEmail(email).orElseThrow(() -> new BadCredentialsException("user not found"));
            Courses course = coursesRepository.findByTitle(courseName).orElseThrow(() -> new ResourceNotFoundException("course not found"));

            Set<Courses> listOfcourse = new HashSet<>();
            Set<Orders> listOfOrders = new HashSet<>();
            
            listOfcourse.add(course);

            Orders order = Orders.builder()
            .student(student)
            .courses(listOfcourse)
            .totalAmount(course.getPrice())
            .build();

            listOfOrders.add(order);
            if (student.getOrders().isEmpty()) // remove orger from cart
                student.setOrders(listOfOrders);
            else 
                student.getOrders().add(order);
           
            userRepository.save(student);

            if (student.getCart() != null) 
                studentsService.deleteCourseFromCart(student.getCart().getCartId(), course.getCourseId());

            return ResponseEntity.status(201).body("Payment successful and order created!");
        } catch (StripeException e) {
            System.out.println(e);
            return ResponseEntity.status(400).body("Payment failed!");

        }

    }
}