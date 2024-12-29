package sapo.com.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sapo.com.model.entity.Role;

import java.util.Optional;

public interface RoleService {
    Page<Role> findAll(Pageable pageable);

    Optional<Role> findById(Long id) throws Exception;
    Role findByName(String name) throws Exception;



}
