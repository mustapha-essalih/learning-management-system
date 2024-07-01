package dev.api.user.dto.response;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import dev.api.enums.Language;
import dev.api.enums.Level;
import dev.api.model.Chapter;
import dev.api.model.Courses;
import dev.api.model.Resources;
import dev.api.model.User;
import dev.api.repository.CoursesRepository;
import dev.api.user.dao.response.ChapterDTO;
import dev.api.user.dao.response.CourseDTO;
import dev.api.user.dao.response.ResourceDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class CoursesDAO {

    @PersistenceContext
    private EntityManager entityManager;

    private CoursesRepository coursesRepository;
    

//    @Transactional
//     public List<Courses> getCoursesByCategoryAndFilters(String category, BigDecimal price, Level level, Language language) {
//         String jpql = "SELECT c FROM Courses c JOIN c.category cat WHERE cat.category = :category AND c.status = 'PUBLISHED'";

//         // String jpql = "SELECT c.title, c.description, c.language, c.courseImage, u.name, c.lastUpdate, ch.title, r.file, r.contentType, r.title, r.isFree, r.duration " +
//         //               "FROM Courses c " +
//         //               "JOIN c.category cat " +
//         //               "JOIN c.user u " +
//         //               "JOIN c.chapters ch " +
//         //               "JOIN ch.resources r " +
//         //               "WHERE cat.category = :category";

//         boolean hasPrice = price != null;
//         boolean hasLevel = level != null;
//         boolean hasLanguage = language != null;

//         if (hasPrice) {
//             jpql += " AND c.price <= :price";
//         }
//         if (hasLevel) {
//             jpql += " AND c.level = :level";
//         }
//         if (hasLanguage) {
//             jpql += " AND c.language = :language";
//         }

//         TypedQuery<Courses> query = em.createQuery(jpql, Courses.class);
//         query.setParameter("category", category);

//         if (hasPrice) {
//             query.setParameter("price", price);
//         }
//         if (hasLevel) {
//             query.setParameter("level", level);
//         }
//         if (hasLanguage) {
//             query.setParameter("language", language);
//         }

//         return query.getResultList();
//     }


  
    public CoursesDAO(CoursesRepository coursesRepository) {
        this.coursesRepository = coursesRepository;
    }


    public List<ResourceDTO> getResourceDTOs(String category){
        String jpql = "SELECT new dev.api.user.dao.response.ResourceDTO(r.title)  FROM Courses c  JOIN c.chapters ch  JOIN ch.resources r  JOIN c.category cat  WHERE cat.category = :category  AND c.status = 'PUBLISHED'";
        
        TypedQuery<ResourceDTO> query = entityManager.createQuery(jpql, ResourceDTO.class);
        query.setParameter("category", category);

        return query.getResultList();
    }


    public List<ChapterDTO> getChaptersDTOs(String category){
        String jpql = "SELECT new dev.api.user.dao.response.ChapterDTO(chapter.title) FROM Courses c  JOIN c.chapters chapter  JOIN c.category category  WHERE category.category = :category  AND c.status = 'PUBLISHED'";
        
        TypedQuery<ChapterDTO> query = entityManager.createQuery(jpql, ChapterDTO.class);
        query.setParameter("category", category);

        return query.getResultList();
    }


    @Transactional
    public void getCoursesByCategoryAndFilters(String category, BigDecimal price, Level level, Language language) {
        
        String jpql = "SELECT new  dev.api.user.dao.response.CourseDTO(c.title , c.description)  FROM Courses c JOIN c.category cat WHERE cat.category = :category AND c.status = 'PUBLISHED'";
        
        TypedQuery<CourseDTO> query = entityManager.createQuery(jpql, CourseDTO.class);
        query.setParameter("category", category);

        List<CourseDTO> courseDTO = query.getResultList();
        List<ChapterDTO> chapterDTOs = getChaptersDTOs(category);
        List<ResourceDTO> resourceDTOs = getResourceDTOs(category);
       
        // for (int i = 0; i < courseDTO.size(); i++) 
        // {
        //     courseDTO.get(i).setChapters(chapterDTOs);
            
        // }


        for (ResourceDTO resourceDTO : resourceDTOs) {
            System.out.println(resourceDTO);
            System.out.println();
        }
        


        
        
       
    }

}



