package api.dev.courses;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import api.dev.courses.dto.CourseDTO;
import api.dev.courses.mapper.CourseMapper;
import api.dev.courses.model.Courses;
import api.dev.courses.repository.CoursesRepository;
import api.dev.enums.Language;
import api.dev.enums.Level;
import api.dev.enums.Status;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Service
public class CoursesService {

    @PersistenceContext
    private EntityManager entityManager;

    private CoursesRepository coursesRepository;

    

    public CoursesService(EntityManager entityManager, CoursesRepository coursesRepository) {
        this.entityManager = entityManager;
        this.coursesRepository = coursesRepository;
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
    



}
