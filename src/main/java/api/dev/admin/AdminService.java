package api.dev.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import api.dev.admin.dto.PlatformAnalyticsDTO;
import api.dev.admin.dto.request.CategoryDto;
import api.dev.admin.dto.request.ChangePasswordDto;
import api.dev.admin.dto.request.UpdateManagerDto;
import api.dev.admin.dto.response.ManagerDto;
import api.dev.admin.mapper.ManagersMapper;
import api.dev.admin.model.Admin;
import api.dev.admin.repository.AdminRepository;
import api.dev.authentication.dto.request.SignupDto;
import api.dev.authentication.model.User;
import api.dev.authentication.repository.UserRepository;
import api.dev.courses.model.Categories;
import api.dev.courses.model.Courses;
import api.dev.courses.repository.CartRepository;
import api.dev.courses.repository.CategoryRepository;
import api.dev.courses.repository.ChapterRepository;
import api.dev.courses.repository.CoursesRepository;
import api.dev.courses.repository.FeedbackRepository;
import api.dev.exceptions.ResourceNotFoundException;
import api.dev.instructors.InstructorsService;
import api.dev.instructors.dto.CoursesAnalyticsDTO;
import api.dev.instructors.dto.InstructorAnalyticsDTO;
import api.dev.instructors.model.Instructors;
import api.dev.instructors.repository.InstructorsRepository;
import api.dev.managers.model.Managers;
import api.dev.managers.repository.ManagersRepository;
import api.dev.students.StudentsService;
import api.dev.students.model.Students;
import api.dev.students.repository.StudentsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class AdminService {
    
    @PersistenceContext
    private EntityManager entityManager;

    private CoursesRepository coursesRepository;
    private InstructorsRepository instructorRepository;
    private CategoryRepository categoryRepository;
    private AdminRepository adminRepository;
    private ManagersRepository managersRepository;
    private StudentsRepository studentsRepository;    
    private FeedbackRepository feedbackRepository;
    private InstructorsService instructorsService;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private CartRepository cartRepository;
    private ChapterRepository chapterRepository;
    private StudentsService studentsService;
    private PasswordEncoder encoder;
    private ManagersMapper managersMapper;
    

    

    public AdminService(EntityManager entityManager, CoursesRepository coursesRepository,
            InstructorsRepository instructorRepository, CategoryRepository categoryRepository,
            AdminRepository adminRepository, ManagersRepository managersRepository,
            StudentsRepository studentsRepository, FeedbackRepository feedbackRepository,
            InstructorsService instructorsService, PasswordEncoder passwordEncoder, UserRepository userRepository,
            CartRepository cartRepository, ChapterRepository chapterRepository, StudentsService studentsService,
            PasswordEncoder encoder, ManagersMapper managersMapper) {
        this.entityManager = entityManager;
        this.coursesRepository = coursesRepository;
        this.instructorRepository = instructorRepository;
        this.categoryRepository = categoryRepository;
        this.adminRepository = adminRepository;
        this.managersRepository = managersRepository;
        this.studentsRepository = studentsRepository;
        this.feedbackRepository = feedbackRepository;
        this.instructorsService = instructorsService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.chapterRepository = chapterRepository;
        this.studentsService = studentsService;
        this.encoder = encoder;
        this.managersMapper = managersMapper;
    }

    public ResponseEntity<PlatformAnalyticsDTO> getPlatformAnalytics() {
   
        Long totalCourses = coursesRepository.count();
        Long totalStudents = studentsRepository.count();
        Long totalInstructors = instructorRepository.count();
        Long totalFeedbacks = feedbackRepository.count();
        Long totalManagers = managersRepository.count();
        BigDecimal totalRevenue = adminRepository.calculateTotalRevenue();
        
        PlatformAnalyticsDTO dto = new PlatformAnalyticsDTO(totalCourses, totalStudents, totalManagers, totalInstructors, totalFeedbacks, totalRevenue);
         
        return ResponseEntity.ok(dto);
    }

    public ResponseEntity<InstructorAnalyticsDTO> getInstructorAnalytics(Integer instructorId) throws ResourceNotFoundException {
        
        Instructors instructor =  instructorRepository.findById(instructorId).orElseThrow(() -> new ResourceNotFoundException("instructor not found"));

        return instructorsService.getInstructorAnalytics(instructor.getEmail());
    }

    public ResponseEntity<CoursesAnalyticsDTO> getCourseAnalytics(Integer courseId, Integer instructorId) throws ResourceNotFoundException {
        Instructors instructor =  instructorRepository.findById(instructorId).orElseThrow(() -> new ResourceNotFoundException("instructor not found"));

        return instructorsService.getCourseAnalytics(courseId, instructor.getEmail());
    }

    public ResponseEntity<?> createManager(SignupDto dto) {

        if(userRepository.findByEmail(dto.getEmail()).isPresent())
        {
            return ResponseEntity.badRequest().build();
        }
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        Managers  newManager = new Managers(dto.getEmail(),encodedPassword , dto.getRole(), dto.getFullName());
        userRepository.save(newManager);
        return ResponseEntity.status(201).body("Signup successful");
    }


    public ResponseEntity<Void> updateManager(Integer id, UpdateManagerDto dto) throws ResourceNotFoundException {

        Managers manager = managersRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("manager not found"));

        if (dto.getEmail() != null) 
            manager.setEmail(dto.getEmail());
        if (dto.getFullName() != null) 
            manager.setFullName(dto.getFullName());
        if (dto.getPassword() != null) 
            manager.setPassword(encoder.encode(dto.getPassword()));    

        managersRepository.save(manager);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> deleteManager(Integer id) throws ResourceNotFoundException {
        Managers manager = managersRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("manager not found"));
        managersRepository.delete(manager);
        return ResponseEntity.noContent().build();
    }


    public ResponseEntity<ManagerDto> getMegetManager(Integer id) throws ResourceNotFoundException {
        Managers manager = managersRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("manager not found"));

        return ResponseEntity.ok(new ManagerDto(manager.getUserId(),manager.getEmail(),manager.getFullName(),manager.getRole()));
    }


    public ResponseEntity<List<ManagerDto>> getAllManagers() {
    
        List<Managers> allManagers = managersRepository.findAll();

        List<ManagerDto> dtos = managersMapper.toManagerDtos(allManagers);        
        
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<Void> deleteInstructor(Integer instructorId) throws ResourceNotFoundException {
        
        Instructors instructor = instructorRepository.findById(instructorId).orElseThrow(() -> new ResourceNotFoundException("instructor not found"));
    
        List<Courses> courses = instructor.getCourses();
      
        courses.forEach((course) -> course.getStudents().forEach((student) -> {
            try {
                studentsService.deleteCourseFromCart(course.getCourseId(), student.getCart().getCartId());
            } catch (ResourceNotFoundException e) {
                e.printStackTrace();
            }
        }));

        instructorRepository.deleteCartCoursesJoinTable(instructorId);

        instructorRepository.deleteStudentCoursesJoinTable(instructorId);
            
        instructorRepository.deleteCourseCategoriesJoinTable(instructorId);

        instructorRepository.delete(instructor); // before delte the instructor delete the join tables
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> deleteStudent(Integer studentId) throws ResourceNotFoundException {
        
        Students student = studentsRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("student not found"));
 
        // delete student_courses join table
        adminRepository.deleteCoursesByStudentId(studentId);
        
        studentsRepository.delete(student);
        return ResponseEntity.noContent().build();
    }



    public ResponseEntity<?> addCategory(String category) {
        Admin admin = adminRepository.findByEmail("admin@admin.com").get();
        Categories categories = new Categories(category);
        admin.getCategories().add(categories);
        adminRepository.save(admin);
        return ResponseEntity.status(201).build();
    }


    public ResponseEntity<?> updateCategory(CategoryDto dto) throws ResourceNotFoundException 
    {
        Categories updatedCategory = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("category not found"));

        updatedCategory.setCategory(dto.getCategory());
        categoryRepository.save(updatedCategory);

        return ResponseEntity.status(204).build();
    }


    public ResponseEntity<?> deleteCategory(Integer categoryId) throws ResourceNotFoundException {
        
        Categories category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category not found"));
        categoryRepository.save(category);
        return ResponseEntity.status(204).build();
    }


    public ResponseEntity<?> allCategories() {
        
        List<Categories> categories = categoryRepository.findAll();

        return ResponseEntity.ok().body(categories);
    }

    public ResponseEntity<Void> changePassword(ChangePasswordDto dto, String email) {
        
        User user = userRepository.findByEmail(email).get();

        if(encoder.matches(dto.getOldPassword(), user.getPassword()))
            user.setPassword(encoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.status(204).build();
    }

   
  

   


    
    



    
}
