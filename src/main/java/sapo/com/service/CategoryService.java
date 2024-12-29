package sapo.com.service;

import sapo.com.exception.DataConflictException;
import sapo.com.exception.ResourceNotFoundException;
import sapo.com.model.dto.request.CategoryRequest;
import sapo.com.model.dto.response.CategoryResponse;

import java.util.List;
import java.util.Set;

public interface CategoryService {
    List<CategoryResponse> getListOfCategories(Long page, Long limit, String queryString) throws ResourceNotFoundException;
    CategoryResponse getCategoryById(Long id) throws ResourceNotFoundException;
    CategoryResponse createNewCategory(CategoryRequest categoryRequest) throws DataConflictException, ResourceNotFoundException;
    CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) throws DataConflictException, ResourceNotFoundException;
    Boolean deleteCategoryById(Long id) throws ResourceNotFoundException;
}
