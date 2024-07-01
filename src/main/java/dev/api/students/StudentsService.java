package dev.api.students;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import dev.api.enums.Status;
import dev.api.exception.ResourceNotFoundException;
import dev.api.model.Cart;
import dev.api.model.Courses;
import dev.api.model.Roles;
import dev.api.model.User;
import dev.api.repository.CartRepository;
import dev.api.repository.CoursesRepository;
import dev.api.repository.UserRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class StudentsService {

    private UserRepository userRepository;
    private CoursesRepository coursesRepository;
    private CartRepository cartRepository;

    public ResponseEntity<?> switchToTeacherRole(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadCredentialsException("user not found"));
        user.setRoles(Roles.TEACHER);
        userRepository.save(user);
        return ResponseEntity.status(204).build();        
    }

    public ResponseEntity<?> addCourseToCart(Integer courseId, Integer userId) throws ResourceNotFoundException {
        
        Set<Courses> listOfCourses = new HashSet<>();
        
        User student = userRepository.findById(userId).orElseThrow(() -> new BadCredentialsException("user not found"));
        Courses course = coursesRepository.findByCourseIdAndStatusAndIsFree(courseId,Status.PUBLISHED,false).orElseThrow(() -> new ResourceNotFoundException("course not found"));


        listOfCourses.add(course);
        
        BigDecimal totalAmount = course.getPrice();
        if (student.getCart() == null) 
        {
            Cart cart = Cart.builder()
                .student(student)
                .courses(listOfCourses)
                .totalAmout(totalAmount) 
                .build();
            student.setCart(cart);
        }
        else
        {
            BigDecimal amout = totalAmount.add(student.getCart().getTotalAmout());
            if(student.getCart().getCourses().add(course))
                student.getCart().setTotalAmout(amout);
        }
        userRepository.save(student);
        return ResponseEntity.status(201).body("course added to cart");
    }

    public ResponseEntity<?> deleteCourseFromCart(Integer cartId, Integer courseId) throws ResourceNotFoundException {

        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("cart not found"));
        Courses course = coursesRepository.findByCourseIdAndStatusAndIsFree(courseId,Status.PUBLISHED,false).orElseThrow(() -> new ResourceNotFoundException("course not found"));
        
        if (!cart.getCourses().isEmpty()) {
            
            cart.getCourses().forEach((c) -> {
                if(c.getCourseId() == course.getCourseId())
                {
                    BigDecimal totalAmount = course.getPrice();
                    cart.setTotalAmout(cart.getTotalAmout().subtract(totalAmount));
                    cart.getCourses().remove(course);
                    cartRepository.save(cart);
                }
            
            } );
        }
        
        return ResponseEntity.status(204).build();
    }




}
