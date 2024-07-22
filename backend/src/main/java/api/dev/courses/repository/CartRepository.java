package api.dev.courses.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import api.dev.students.model.Cart;
import jakarta.transaction.Transactional;

    
@Repository
public interface CartRepository extends JpaRepository<Cart,Integer>{
 
    @Modifying
    @Transactional
    @Query(value = "UPDATE cart SET total_amount = total_amount - :coursePrice WHERE cart_id = :cartId", nativeQuery = true)
    void updateCartTotalAmount(@Param("cartId") Integer cartId, @Param("coursePrice") BigDecimal coursePrice);
}
