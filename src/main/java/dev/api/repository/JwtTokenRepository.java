package dev.api.repository;

 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dev.api.model.JwtToken;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken , Integer>{
    JwtToken findByToken(String token);

    // @Query(value = """
    //   select t from JwtToken t inner join User u\s
    //   on t.user.id = u.id\s
    //   where u.id = :id and (t.is_logged_out = false)\s
    //   """)
    // List<JwtToken> findAllValidTokenByUser(Integer id);
}
