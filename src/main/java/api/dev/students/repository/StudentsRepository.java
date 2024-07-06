package api.dev.students.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import api.dev.instructors.model.Instructors;
import api.dev.students.model.Students;

public interface StudentsRepository extends JpaRepository<Students,Integer>{
    Optional<Students> findByEmail(String email);

}
