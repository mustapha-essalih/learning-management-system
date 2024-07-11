package api.dev.admin;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api.dev.admin.dto.PlatformAnalyticsDTO;
import api.dev.admin.dto.request.CategoryDto;
import api.dev.admin.model.Admin;
import api.dev.admin.repository.AdminRepository;
import api.dev.authentication.dto.request.SignupDto;
import api.dev.authentication.model.User;
import api.dev.authentication.repository.UserRepository;
import api.dev.courses.mapper.CourseMapper;
import api.dev.courses.model.Categories;
import api.dev.courses.model.Courses;
import api.dev.courses.repository.CartRepository;
import api.dev.courses.repository.CategoryRepository;
import api.dev.courses.repository.ChapterRepository;
import api.dev.courses.repository.CoursesRepository;
import api.dev.courses.repository.FeedbackRepository;
import api.dev.courses.repository.ResourcesRepository;
import api.dev.exceptions.ResourceNotFoundException;
import api.dev.instructors.InstructorsService;
import api.dev.instructors.dto.CoursesAnalyticsDTO;
import api.dev.instructors.dto.InstructorAnalyticsDTO;
import api.dev.instructors.mapper.InstructorMapper;
import api.dev.instructors.model.Instructors;
import api.dev.instructors.repository.InstructorsRepository;
import api.dev.managers.model.Managers;
import api.dev.managers.repository.ManagersRepository;
import api.dev.students.StudentsService;
import api.dev.students.model.Students;
import api.dev.students.repository.StudentsRepository;
import api.dev.utils.FileStorageService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

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
    
    
    public AdminService(EntityManager entityManager, CoursesRepository coursesRepository,
            InstructorsRepository instructorRepository, CategoryRepository categoryRepository,
            AdminRepository adminRepository, ManagersRepository managersRepository,
            StudentsRepository studentsRepository, FeedbackRepository feedbackRepository,
            InstructorsService instructorsService, PasswordEncoder passwordEncoder, UserRepository userRepository,
            CartRepository cartRepository, ChapterRepository chapterRepository, StudentsService studentsService) {
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

 
 
     
    public ResponseEntity<?> deleteUsers(Integer userId) throws ResourceNotFoundException {
        
        Instructors user = instructorRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));
    
        List<Courses> courses = user.getCourses();
      
        adminRepository.deleteCartCoursesByInstructorId(userId);
        courses.forEach((course) -> course.getStudents().forEach((student) -> {
            try {
                studentsService.deleteCourseFromCart(course.getCourseId(), student.getCart().getCartId());
            } catch (ResourceNotFoundException e) {
                e.printStackTrace();
            }
        }));

        adminRepository.deleteFeedbackByInstructorId(userId);
        adminRepository.deleteStudentCoursesByInstructorId(userId);
            









        // instructorRepository.delete(user);
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

    



    
}
