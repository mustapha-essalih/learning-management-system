package api.dev.students;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import api.dev.courses.model.Courses;
import api.dev.courses.model.Feedback;
import api.dev.courses.repository.CartRepository;
import api.dev.courses.repository.CoursesRepository;
import api.dev.courses.repository.FeedbackRepository;
import api.dev.enums.Status;
import api.dev.exceptions.ResourceNotFoundException;
import api.dev.students.dto.request.FeeadbackDto;
import api.dev.students.model.Cart;
import api.dev.students.model.Students;
import api.dev.students.repository.StudentsRepository;

// if course is publish

@Service
public class StudentsService {
    
    private StudentsRepository studentsRepository;
    private CoursesRepository coursesRepository;
    private CartRepository cartRepository;
    private FeedbackRepository feedbackRepository;

    public StudentsService(StudentsRepository studentsRepository, CoursesRepository coursesRepository,
            CartRepository cartRepository, FeedbackRepository feedbackRepository) {
        this.studentsRepository = studentsRepository;
        this.coursesRepository = coursesRepository;
        this.cartRepository = cartRepository;
        this.feedbackRepository = feedbackRepository;
    }



    public ResponseEntity<?> addCourseToCart(Integer courseId, String email) throws ResourceNotFoundException {
        
        Set<Courses> listOfCourses = new HashSet<>();
        
        Students student = studentsRepository.findByEmail(email).get();
        Courses course = coursesRepository.findByCourseIdAndStatusAndIsFree(courseId, Status.PUBLISHED, false).orElseThrow(() -> new ResourceNotFoundException("course is not published or not free or not found"));

        listOfCourses.add(course);
        
        BigDecimal totalAmount = course.getPrice();
        
        if (student.getCart() == null) 
        {
            Cart cart = new Cart(student, listOfCourses, totalAmount);
            student.setCart(cart);
        }
        else
        {
            BigDecimal amount = totalAmount.add(student.getCart().getTotalAmount());
            if(student.getCart().getCourses().add(course))
                student.getCart().setTotalAmount(amount);
        }
        studentsRepository.save(student);
        return ResponseEntity.status(201).body("course added to cart");
    }


    public ResponseEntity<?> feedbackCourse(FeeadbackDto dto, String email) throws ResourceNotFoundException {
    
        Students student = studentsRepository.findByEmail(email).get();
        Courses course = coursesRepository.findById(dto.getCourseId()).orElseThrow(() -> new ResourceNotFoundException("course is not published or not free or not found"));

        if (student.getCourses().contains(course)) 
        {
            Feedback newFeedback = new Feedback(course, student, dto.getRating(), dto.getComment());
    
            student.getFeedbacks().add(newFeedback);
            course.getFeedback().add(newFeedback);
    
            feedbackRepository.save(newFeedback);
            return ResponseEntity.status(201).build();      
        }
        return ResponseEntity.status(400).body("student not enroll this course");
    }



    public ResponseEntity<Void> updateInfos(String newEmail, String fullName, String email) {
        Students instructor = studentsRepository.findByEmail(email).get(); 
        if (newEmail != null) 
            instructor.setEmail(email);
        if (fullName != null) 
            instructor.setFullName(fullName);
        studentsRepository.save(instructor);
        return ResponseEntity.noContent().build();
    }



    public ResponseEntity<?> deleteCourseFromCart(Integer courseId, Integer cartId, Integer studentId) throws ResourceNotFoundException {
        
        Courses course = coursesRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("course is not published or not free or not found"));
        cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("cart not found"));
       
        cartRepository.updateCartTotalAmount(cartId, course.getPrice());
        
        studentsRepository.deleteCartCourseByCartIdAndCourseId(cartId,course.getCourseId());

        return ResponseEntity.status(204).build();
    }
}