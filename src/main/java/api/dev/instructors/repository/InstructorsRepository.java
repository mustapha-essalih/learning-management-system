package api.dev.instructors.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import api.dev.instructors.model.Instructors;

public interface InstructorsRepository extends JpaRepository<Instructors,Integer>{
    Optional<Instructors> findByEmail(String email);

}
