package sapo.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sapo.com.model.entity.Role;

public interface RoleRepository extends JpaRepository<Role , Long> {
    @Query("select r from Role r where r.name = :name")
    Role findByName(String name);
}
