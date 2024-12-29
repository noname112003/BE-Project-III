package sapo.com.controller.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sapo.com.exception.DataConflictException;
import sapo.com.exception.ResourceNotFoundException;
import sapo.com.model.dto.request.BrandRequest;
import sapo.com.model.dto.request.CategoryRequest;
import sapo.com.model.dto.response.BrandResponse;
import sapo.com.model.dto.response.CategoryResponse;
import sapo.com.model.dto.response.ResponseObject;
import sapo.com.service.impl.BrandServiceImpl;
import sapo.com.service.impl.CategoryServiceImpl;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/v1/products/brands")
@CrossOrigin("http://localhost:5173")
public class BrandController {

    private static final Logger log = LoggerFactory.getLogger(BrandController.class);

    @Autowired
    private BrandServiceImpl brandService;

    @GetMapping
    public ResponseEntity<ResponseObject> getListOfBrands(@RequestParam Long page, @RequestParam Long limit, @RequestParam String query) {
        try {
            List<BrandResponse> brands = brandService.getListOfBrands(page, limit, query);
            return new ResponseEntity<>(new ResponseObject("Lấy danh sách nhãn hiệu thành công", brands), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            log.error("Error: ", e);
            return new ResponseEntity<>(new ResponseObject(e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error: ", e);
            return new ResponseEntity<>(new ResponseObject(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/total-brands")
    public ResponseEntity<?> getNumberOfBrands(@RequestParam String query) {
        Long numberOfBrands = brandService.getNumberOfBrands(query);
        return new ResponseEntity<>(new ResponseObject("Lấy số lượng nhãn hiệu thành công", numberOfBrands), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getBrandById(@PathVariable Long id) {
        BrandResponse brand = brandService.getBrandById(id);
        return new ResponseEntity<>(new ResponseObject("Lấy thông tin nhãn hiệu thành công", brand), HttpStatus.OK);

    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<ResponseObject> updateBrand(@PathVariable Long id, @RequestBody BrandRequest brandRequest) {

        BrandResponse brand = brandService.updateBrand(id, brandRequest);
        return new ResponseEntity<>(new ResponseObject("Cập nhập thông tin nhãn hiệu thành công", brand), HttpStatus.OK);

    }

    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createNewBrand(@RequestBody BrandRequest brandRequest) {

        BrandResponse brand = brandService.createNewBrand(brandRequest);
        return new ResponseEntity<>(new ResponseObject("Tạo nhãn hiệu mới thành công", brand), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBrandById(@PathVariable Long id) {

        Boolean checkk = brandService.deleteBrandById(id);
        if (checkk)
            return new ResponseEntity<>(new ResponseObject("Xóa nhãn hiệu thành công",null), HttpStatus.OK);
        else
            return new ResponseEntity<>(new ResponseObject("Không thể xóa nhãn hiệu này",null), HttpStatus.BAD_REQUEST);

    }

}
