package sapo.com.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import sapo.com.model.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer , Long>, PagingAndSortingRepository<Customer, Long>, CrudRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c")
    Page<Customer> findAll(Pageable pageable);
    @Query("SELECT c FROM Customer c WHERE c.name LIKE %?1% OR c.phoneNumber LIKE %?1%")
    public Page<Customer> findByKeyword(String keyword, Pageable pageable);

    public Customer findByPhoneNumber(String phoneNumber);

    //    @EntityGraph(attributePaths = {"orders"})
    public Optional<Customer> findById(Long id);
}
