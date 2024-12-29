package sapo.com.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sapo.com.model.entity.Role;
import sapo.com.repository.RoleRepository;
import sapo.com.service.RoleService;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository ;

    @Override
    public Page<Role> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    @Override
    public Optional<Role> findById(Long id) throws Exception {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()){
            return role;
        }else {
            throw new Exception("Id not found");
        }
    }

    @Override
    public Role findByName(String name) throws Exception {
        Role role = roleRepository.findByName(name);
        if (role !=null){
            return role;
        }else {
            throw new Exception("Name not found");
        }

    }
}
