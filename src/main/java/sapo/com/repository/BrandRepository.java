package sapo.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sapo.com.model.entity.Brand;
import sapo.com.model.entity.Category;
import sapo.com.model.entity.Product;


import java.util.Set;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    @Modifying
    @Query("update Brand d set d.status = false where d.id = :id")
    int deleteBrandById(@Param("id") Long id);

    @Query(
            value = "CALL get_list_of_brands(:page, :limit, :query)",
            nativeQuery = true
    )
    Set<Brand> getListOfBrands(Long page, Long limit, String query );

    boolean existsByCode(String code);

    @Query(value = "SELECT p FROM Product p WHERE p.category.id = :id AND p.status = true")
    Set<Product> existProduct(@Param("id") Long id);

    @Query(value = "SELECT COUNT(*) FROM Brand b WHERE (b.name LIKE %:query% OR b.code LIKE %:query%) AND b.status = true")
    Long countByNameOrCodeAndStatus(@Param("query") String query);
}

