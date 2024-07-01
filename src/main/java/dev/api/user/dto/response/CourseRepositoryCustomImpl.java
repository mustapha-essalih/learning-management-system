package dev.api.user.dto.response;

import java.math.BigDecimal;
import java.util.List;

import dev.api.enums.Language;
import dev.api.model.Courses;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

public class CourseRepositoryCustomImpl  {

    @PersistenceContext
    private EntityManager entityManager;
 

   
    public List<Courses> findByCategoryAndFilters(Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, String language, String level) {
        StringBuilder queryStr = new StringBuilder("SELECT c FROM Courses c JOIN c.category cat WHERE cat.categoryId = :categoryId");

        if (minPrice != null) {
            queryStr.append(" AND c.price >= :minPrice");
        }
        if (maxPrice != null) {
            queryStr.append(" AND c.price <= :maxPrice");
        }
        if (level != null) {
            queryStr.append(" AND c.level = :level");
        }
        if (language != null) {
            queryStr.append(" AND c.language = :language");
        }

        TypedQuery<Courses> query = entityManager.createQuery(queryStr.toString(), Courses.class);
        query.setParameter("categoryId", categoryId);

        if (minPrice != null) {
            query.setParameter("minPrice", minPrice);
        }
        if (maxPrice != null) {
            query.setParameter("maxPrice", maxPrice);
        }
        if (level != null  ) {
            query.setParameter("level", level);
        }
        if (language != null) {
            query.setParameter("language", language);
        }

        return query.getResultList();
    }
 
}