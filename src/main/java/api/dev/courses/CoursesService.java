package api.dev.courses;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import api.dev.authentication.model.User;
import api.dev.authentication.repository.UserRepository;
import api.dev.courses.dto.CourseDTO;
import api.dev.courses.dto.request.GetResourceDto;
import api.dev.courses.mapper.CourseMapper;
import api.dev.courses.model.Courses;
import api.dev.courses.repository.CoursesRepository;
import api.dev.courses.repository.FeedbackRepository;
import api.dev.enums.Language;
import api.dev.enums.Level;
import api.dev.enums.Status;
import api.dev.exceptions.ResourceNotFoundException;
import api.dev.students.model.Students;
import api.dev.students.repository.StudentsRepository;
import api.dev.utils.FileUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Service
public class CoursesService {

    @PersistenceContext
    private EntityManager entityManager;

    private CoursesRepository coursesRepository;
    private FeedbackRepository feedbackRepository;
    private UserRepository userRepository;
    private CourseMapper courseMapper;

    


    public CoursesService(EntityManager entityManager, CoursesRepository coursesRepository,
            FeedbackRepository feedbackRepository, UserRepository userRepository,
            CourseMapper courseMapper) {
        this.entityManager = entityManager;
        this.coursesRepository = coursesRepository;
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
        this.courseMapper = courseMapper;
    }

 


    @Transactional
    public List<CourseDTO> getFilteredCourses(String category, BigDecimal minPrice, BigDecimal maxPrice,Language language, Level level, Double minFeedback, Double maxFeedback, Boolean isFree){//}, double minFeedback, double maxFeedback) {
        StringBuilder queryStr = new StringBuilder("SELECT DISTINCT c FROM Courses c " +
                "JOIN c.category cat " +
                "JOIN c.chapters ch " +
                "JOIN ch.resources r " +
                "WHERE c.status = :status ");

        if (category != null) {
            queryStr.append("AND cat.category = :category ");
        }
        if (minPrice != null) {
            queryStr.append("AND c.price >= :minPrice ");
        }
        if (maxPrice != null) {
            queryStr.append("AND c.price <= :maxPrice ");
        }
        if (language != null) {
            queryStr.append("AND c.language = :language ");
        }
        if (level != null) {
            queryStr.append("AND c.level = :level ");
        }
        if (minFeedback != null) {
            queryStr.append("AND c.feedback >= :minFeedback ");
        }
        if (maxFeedback != null) {
            queryStr.append("AND c.feedback <= :maxFeedback ");
        }
        if (isFree != null) {
            queryStr.append("AND c.isFree = :isFree ");
        }

        TypedQuery<Courses> query = entityManager.createQuery(queryStr.toString(), Courses.class);
        query.setParameter("status", Status.PUBLISHED);

        if (category != null) {
            query.setParameter("category", category);
        }
        if (minPrice != null) {
            query.setParameter("minPrice", minPrice);
        }
        if (maxPrice != null) {
            query.setParameter("maxPrice", maxPrice);
        }
        if (language != null) {
            query.setParameter("language", language);
        }
        if (level != null) {
            query.setParameter("level", level);
        }
        if (minFeedback != null) {
            query.setParameter("minFeedback", minFeedback);
        }
        if (maxFeedback != null) {
            query.setParameter("maxFeedback", maxFeedback);
        }

        if (isFree != null) {
            query.setParameter("isFree", isFree);
        }

        List<Courses> courses = query.getResultList();

        System.out.println(courses.size());
        // return courses.stream()
        //         .map(CourseMapper.INSTANCE::courseToCourseDTO)
        //         .collect(Collectors.toList());
        return null;
    }


    public ResponseEntity<?> getCoursesFiltered(String category, BigDecimal minPrice, BigDecimal maxPrice,Language language, Level level,Double minFeedback,  Double maxFeedback, Boolean isFree) {
        

        getFilteredCourses(category,  minPrice,  maxPrice, language,  level ,minFeedback,  maxFeedback , isFree);

        // fetch how many students 

        return null;
    }


    public ResponseEntity<?> getFeedbacksOfCourse(Integer courseId) throws ResourceNotFoundException {
        
        coursesRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("course is not published or not free or not found"));

        Double averageRating = feedbackRepository.getAverageRatingByCourseId(courseId);

        return ResponseEntity.ok().body(averageRating != null ? averageRating : 0.0);
    }

    public ResponseEntity<?> getCourseDetails(Integer courseId) throws ResourceNotFoundException { // get course without resources

        Courses  course = coursesRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("course not found"));
        CourseDTO courseDTO = courseMapper.courseToCourseDTOWithDetails(course, false);
        return ResponseEntity.ok(courseDTO);
    }

    public ResponseEntity<?> findCourseByName(String courseName) {
        
        List<Courses> courses = coursesRepository.findCourseByName(courseName);
        List<CourseDTO> courseDTOs = courseMapper.coursesToCourseDTOsWithDetails(courses, false);

        if (courses.isEmpty()) {
            return ResponseEntity.status(204).body("course not found");
        }
        return ResponseEntity.ok(courseDTOs);
    }


    public ResponseEntity<?> getAllCourses() {

        List<Courses> allCourse = coursesRepository.findAll();
        List<CourseDTO> courseDTOs = courseMapper.coursesToCourseDTOsWithDetails(allCourse, true);
        return ResponseEntity.ok(courseDTOs);
    }


    public ResponseEntity<?> getResource( Integer courseId , String filePath, String contentType, String email ) throws ResourceNotFoundException, AccessDeniedException {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("user not found"));
        Courses  course = coursesRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("course not found"));

        if (user.getRole().equals("STUDENT")) 
        {
            if (!course.getStudents().contains(user)) 
            {
                throw new RuntimeException("you don't have permission to access this resource");
            }
        }
        else if (user.getRole().equals("INSTRUCTOR")) 
        {
            if (course.getInstructor().getUserId() != user.getUserId()) {
                throw new RuntimeException("you don't have permission to access this resource");                
            }
        }

        byte[] file = FileUtils.returnFileFromStorage( System.getProperty("user.dir") + "/uploads/" + filePath);
        
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf(contentType)).body(file);
    }




   

   
 





}
