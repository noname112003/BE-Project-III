package sapo.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sapo.com.model.dto.response.CategoryResponse;

import sapo.com.model.entity.Category;
import sapo.com.model.entity.Product;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

//    @Query("select c.id, c.name, c.code, c.description, c.status, c.createdOn, c.updatedOn from Category c where c.id=:id")
//    CategoryResponse getCategoryById(@Param("id") Long id);

    @Modifying
    @Query("update Category c set c.status = false where c.id = :id")
    int deleteCategoryById(@Param("id") Long id);

    @Query(
            value = "CALL get_list_of_categories(:page, :limit, :query)",
            nativeQuery = true
    )
    Set<Category> getListOfCategories(Long page, Long limit, String query );

    boolean existsByCode(String code);

    @Query(value = "SELECT p FROM Product p WHERE p.category.id = :id AND p.status = true")
    Set<Product> existProduct(@Param("id") Long id);

    @Query(value = "SELECT COUNT(*) FROM Category c WHERE (c.name LIKE %:query% OR c.code LIKE %:query%) AND c.status = true")
    Long countByNameOrCodeAndStatus(@Param("query") String query);
}

