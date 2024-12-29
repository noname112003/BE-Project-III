package sapo.com.service.impl;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sapo.com.exception.DataConflictException;
import sapo.com.exception.ResourceNotFoundException;
import sapo.com.model.dto.request.CategoryRequest;
import sapo.com.model.dto.response.CategoryResponse;
import sapo.com.model.entity.Category;
import sapo.com.model.entity.Product;
import sapo.com.repository.CategoryRepository;
import sapo.com.service.CategoryService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryResponse> getListOfCategories(Long page, Long limit, String queryString) {
        Set<Category> categories = categoryRepository.getListOfCategories(page+1, limit, queryString);
        List<CategoryResponse> categoriesResponse = new ArrayList<>();
        for (Category category : categories) {
            categoriesResponse.add(category.transferToResponse());
        }
        if(!categoriesResponse.isEmpty())
            return categoriesResponse;
        else throw new ResourceNotFoundException("Loại sản phẩm không tồn tại");
    }

    public Long getNumberOfCategories(String queryString) {
        return categoryRepository.countByNameOrCodeAndStatus(queryString);
    }

    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loại sản phẩm không tồn tại hoặc đã bị xóa"));
        if (category.getStatus()) {
            return category.transferToResponse();
        } else
            throw new ResourceNotFoundException("Loại sản phẩm không tồn tại hoặc đã bị xóa");
    }

    @Transactional
    public CategoryResponse createNewCategory(CategoryRequest categoryRequest) {
        String code = categoryRequest.getCode();
        if (code != ""&& code!=null && code.startsWith("PGN")) {
            throw new DataConflictException("Mã loại không được có tiền tố " + "PGN");
        }
        if (code != "" && categoryRepository.existsByCode(code)) {
            throw new DataConflictException("Code " + code + " đã tồn tại.");
        }
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setCode(code);
        category.setDescription(categoryRequest.getDescription());
        category.setStatus(true);
        category.setCreatedOn(LocalDateTime.now());
        category.setUpdatedOn(LocalDateTime.now());
        Category savedCategory = categoryRepository.save(category);
        entityManager.refresh(savedCategory);
        return savedCategory.transferToResponse();
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loại sản phẩm không tồn tại hoặc đã bị xóa"));
        String code = categoryRequest.getCode();
        if(!code.equals(category.getCode()) && code != ""){
            if (code.startsWith("PGN")) {
                throw new DataConflictException("Mã loại không được có tiền tố " + "PGN");
            }
            if (categoryRepository.existsByCode(code)) {
                throw new DataConflictException("Code " + code + " đã tồn tại.");
            }
        }
        category.setName(categoryRequest.getName());
        if (code != "") {
            category.setCode(code);
        }
        category.setDescription(categoryRequest.getDescription());
        category.setUpdatedOn(LocalDateTime.now());
        Category savedCategory = categoryRepository.saveAndFlush(category);
        entityManager.refresh(savedCategory);
        return savedCategory.transferToResponse();
    }

    @Transactional
    public Boolean deleteCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loại sản phẩm không tồn tại hoặc đã bị xóa"));
        Set<Product> products= categoryRepository.existProduct(id);
        if(products.isEmpty()){
            categoryRepository.deleteCategoryById(id);
            return true;
        }
        return false;
    }

}
