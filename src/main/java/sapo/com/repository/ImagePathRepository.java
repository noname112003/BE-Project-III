package sapo.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sapo.com.model.entity.Category;
import sapo.com.model.entity.ImagePath;
@Repository
public interface ImagePathRepository extends JpaRepository<ImagePath, Long>{
    @Modifying
    @Transactional
    void deleteByProductId(Long productId);
}

