package api.dev.courses.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import api.dev.courses.model.Categories;


@Repository
public interface CategoryRepository extends JpaRepository<Categories,Integer>{
    Optional<Categories> findByCategory(String category);

}
