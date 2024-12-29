package sapo.com.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sapo.com.model.entity.Order;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "SELECT * FROM orders o WHERE o.created_on BETWEEN :startDate AND :endDate AND o.code LIKE %:code% ORDER BY o.created_on DESC", nativeQuery = true)
    List<Order> findOrdersByDateAndCode(@Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate,
                                        @Param("code") String code);

    @Query("SELECT o FROM Order o WHERE CAST(o.createdOn AS date) = :today")
    Page<Order> findOrdersToday(@Param("today") LocalDate today, Pageable pageable);

}
