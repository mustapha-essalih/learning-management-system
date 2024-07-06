package api.dev.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import api.dev.admin.model.Admin;

public interface AdminRepository extends JpaRepository<Admin,Integer>{
    
}
