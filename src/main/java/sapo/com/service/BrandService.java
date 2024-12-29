package sapo.com.service;

import sapo.com.exception.DataConflictException;
import sapo.com.exception.ResourceNotFoundException;
import sapo.com.model.dto.request.BrandRequest;
import sapo.com.model.dto.response.BrandResponse;

import java.util.List;
import java.util.Set;

public interface BrandService {

    List<BrandResponse> getListOfBrands(Long page, Long limit, String queryString) throws ResourceNotFoundException;
    BrandResponse getBrandById(Long id) throws ResourceNotFoundException;
    BrandResponse createNewBrand(BrandRequest brandRequest) throws ResourceNotFoundException, DataConflictException;
    BrandResponse updateBrand(Long id, BrandRequest brandRequest) throws ResourceNotFoundException, DataConflictException;
    Boolean deleteBrandById(Long id) throws ResourceNotFoundException;

}
