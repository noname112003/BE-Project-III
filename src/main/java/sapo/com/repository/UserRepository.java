package sapo.com.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sapo.com.model.entity.User;

public interface UserRepository extends JpaRepository<User , Long> {
    @Query("select u from User u where u.email = :email")
    User findByEmail(String email);

    @Query("select u from User u where u.name = :name")
    User findByName(String name);

    @Query("select u from User u where u.phoneNumber = :phoneNumber")
    User findByPhoneNumber(String phoneNumber);

    Page<User> findAllByRolesName(String roleName, Pageable pageable);

    // Search by name or phone number
    @Query("SELECT u FROM User u WHERE u.name LIKE %:search% OR u.phoneNumber LIKE %:search%")
    Page<User> findBySearch( String search, Pageable pageable);

    // Filter by role and search by name or phone number
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :role AND (u.name LIKE %:search% OR u.phoneNumber LIKE %:search%)")
    Page<User> findByRoleAndSearch( String role,  String search, Pageable pageable);

    // Find by role
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :role")
    Page<User> findByRole( String role, Pageable pageable);


}
