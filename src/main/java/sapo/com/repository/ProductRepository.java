package sapo.com.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

import sapo.com.model.entity.Product;

import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query("update Product p set p.status = false where p.id = :id")
    int deleteProductById(@Param("id") Long id);

    @Query(
            value = "CALL get_list_of_products(:page, :limit, :query)",
            nativeQuery = true
    )
    Set<Product> getListOfProducts(Long page, Long limit, String query );

    Long countByNameContainingAndStatus(String name, boolean status);
    @Query("SELECT p FROM Product p JOIN p.variants v WHERE v.id = :variantId")
    Product findByVariantId(Long variantId);
}

