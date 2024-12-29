package sapo.com.controller.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sapo.com.exception.DataConflictException;
import sapo.com.exception.ResourceNotFoundException;
import sapo.com.model.dto.request.CategoryRequest;
import sapo.com.model.dto.response.CategoryResponse;
import sapo.com.model.dto.response.ResponseObject;
import sapo.com.model.entity.Category;
import sapo.com.service.impl.CategoryServiceImpl;
import sapo.com.service.impl.ProductServiceImpl;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/v1/products/categories")
@CrossOrigin("http://localhost:5173")
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping
    public ResponseEntity<ResponseObject> getListOfCategories(@RequestParam Long page, @RequestParam Long limit, @RequestParam String query) {
        List<CategoryResponse> categories = categoryService.getListOfCategories(page, limit, query);
        return new ResponseEntity<>(new ResponseObject("Lấy danh sách loại sản phẩm thành công", categories), HttpStatus.OK);
    }

    @GetMapping("/total-categories")
    public ResponseEntity<?> getNumberOfCategories(@RequestParam String query) {
        Long numberOfCategories = categoryService.getNumberOfCategories(query);
        return new ResponseEntity<>(new ResponseObject("Lấy số lượng loại sản phẩm thành công", numberOfCategories), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getCategoryById(@PathVariable Long id) {

        CategoryResponse category = categoryService.getCategoryById(id);
        return new ResponseEntity<>(new ResponseObject("Lấy thông tin loại sản phẩm thành công", category), HttpStatus.OK);

    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<ResponseObject> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {

        CategoryResponse category = categoryService.updateCategory(id, categoryRequest);
        return new ResponseEntity<>(new ResponseObject("Cập nhật thông tin loại sản phẩm thành công", category), HttpStatus.OK);

    }

    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createNewCategory(@RequestBody CategoryRequest categoryRequest) {

        CategoryResponse category = categoryService.createNewCategory(categoryRequest);
        return new ResponseEntity<>(new ResponseObject("Tạo loại sản phẩm mới thành công", category), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Long id) {

        Boolean checkk = categoryService.deleteCategoryById(id);
        if (checkk)
            return new ResponseEntity<>(new ResponseObject("Xóa loại sản phẩm thành công",null), HttpStatus.OK);
        else
            return new ResponseEntity<>(new ResponseObject("Không thể xóa loại sản phẩm này.",null), HttpStatus.BAD_REQUEST);

    }

}
