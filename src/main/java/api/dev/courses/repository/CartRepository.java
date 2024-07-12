package api.dev.courses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import api.dev.students.model.Cart;
import jakarta.transaction.Transactional;

    
@Repository
public interface CartRepository extends JpaRepository<Cart,Integer>{

    
}
