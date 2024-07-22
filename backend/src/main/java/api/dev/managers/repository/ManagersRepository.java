package api.dev.managers.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import api.dev.managers.model.Managers;

public interface ManagersRepository extends JpaRepository<Managers,Integer>{
    
}
