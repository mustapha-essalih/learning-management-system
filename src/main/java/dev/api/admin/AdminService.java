package dev.api.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import dev.api.admin.dto.request.CategoryDto; 
import dev.api.authentication.AuthenticationService;
import dev.api.authentication.dto.SignupDto;
import dev.api.model.Categories;
import dev.api.model.Courses;
import dev.api.model.Roles;
import dev.api.model.User;
import dev.api.repository.CategoryRepository;
import dev.api.repository.CoursesRepository;
import dev.api.repository.UserRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AdminService {
    
    private AuthenticationService authenticationService;
    private UserRepository userRepository;
    private CoursesRepository coursesRepository;
    private CategoryRepository categoryRepository;

    public ResponseEntity<?> createManager(SignupDto dto) {
        authenticationService.signup(dto);
        return ResponseEntity.status(HttpStatus.OK).body("Signup successful");
    }


    public ResponseEntity<?> deleteManager(Integer managerId) {
        User manager = userRepository.findByUserIdAndRoles(managerId, Roles.MANAGER).orElseThrow(() -> new BadCredentialsException("manager not found"));
        userRepository.delete(manager);
        return ResponseEntity.status(204).build();
    }


    public ResponseEntity<?> getManager(Integer managerId) {

        User manager = userRepository.findByUserIdAndRoles(managerId, Roles.MANAGER).orElseThrow(() -> new BadCredentialsException("manager not found"));
        return ResponseEntity.status(HttpStatus.OK).body(manager);
    }


	public ResponseEntity<?> getAllManagers() {		
        List<User> managers = userRepository.findByRoles(Roles.MANAGER);
        return ResponseEntity.status(HttpStatus.OK).body(managers);
	}


    public ResponseEntity<?> deleteStudent(Integer studentId) {
        User student = userRepository.findByUserIdAndRoles(studentId, Roles.STUDENT).orElseThrow(() -> new BadCredentialsException("student not found"));
        userRepository.delete(student);
        return ResponseEntity.status(204).build();
    }


    public ResponseEntity<?> deleteTeacher(Integer teacherId) {
        User teacher = userRepository.findByUserIdAndRoles(teacherId, Roles.TEACHER).orElseThrow(() -> new BadCredentialsException("teacher not found"));
        userRepository.delete(teacher);
        return ResponseEntity.status(204).build();
    }

     public ResponseEntity<?> getAllCources() {
       
         
        return ResponseEntity.status(HttpStatus.OK).body("allCoursesResponse");
    }


    public ResponseEntity<?> addCategory(String category) {
        User user = userRepository.findByEmail("admin@admin.com").get();
        Categories c = Categories.builder().category(category).build();
        user.getCategories().add(c);
        userRepository.save(user);
        return ResponseEntity.status(201).build();
    }


    public ResponseEntity<?> updateCategory(CategoryDto dto) 
    {
        Categories updatedCategory = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new RuntimeException("category not found"));

        updatedCategory.setCategory(dto.getCategory());
        categoryRepository.save(updatedCategory);

        return ResponseEntity.status(204).build();
    }


    public ResponseEntity<?> deleteCategory(Integer categoryId) {
        
        Categories category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("category not found"));
        categoryRepository.save(category);
        return ResponseEntity.status(204).build();
    }


    public ResponseEntity<?> allCategories() {
        
        List<Categories> categories = categoryRepository.findAll();

        return ResponseEntity.ok().body(categories);
    }
   
}
