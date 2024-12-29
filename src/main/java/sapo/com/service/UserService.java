package sapo.com.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sapo.com.exception.UserException;
import sapo.com.model.dto.request.PasswordRequest;
import sapo.com.model.dto.request.RoleRequest;
import sapo.com.model.dto.request.UpdateUserRequest;
import sapo.com.model.dto.request.UserRequest;
import sapo.com.model.dto.response.UserResponse;
import sapo.com.model.entity.Role;
import sapo.com.model.entity.User;



public interface UserService {
    User register(User user) throws Exception;
    UserResponse login (UserRequest userRequest) throws Exception;

    User resetPassword (Long id) throws Exception;
    Page<User> findAll(Pageable pageable);

    Page<User> findAllByRolesName(Pageable pageable , String role) ;

    Page<User> findAllBySearch(Pageable pageable , String search);

    User update (Long id , User user) throws Exception;

    User updateRole (Long id , Role role) throws Exception;

    User findById(Long id) throws UserException;

    User findByName(String name) throws Exception;

    User findByPhoneNumber(String phoneNumber) throws Exception;

    User findByEmail(String email) throws Exception;

    void existPhoneNumber (String phoneNumber) throws Exception;

    void existEmail(String email) throws Exception ;

    User changPassword (Long id , PasswordRequest passwordRequest) throws Exception;

    void deleteById (Long id) throws Exception;

    User findUserProfileByJwt(String jwt) throws UserException;
}
