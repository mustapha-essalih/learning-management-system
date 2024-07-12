package api.dev.courses.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import api.dev.courses.model.Categories;


@Repository
public interface CategoryRepository extends JpaRepository<Categories,Integer>{
    Optional<Categories> findByCategory(String category);

}
