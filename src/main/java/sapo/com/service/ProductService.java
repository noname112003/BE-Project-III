package sapo.com.service;

import sapo.com.exception.DataConflictException;
import sapo.com.exception.ResourceNotFoundException;
import sapo.com.model.dto.request.ProductRequest;
import sapo.com.model.dto.request.VariantRequest;
import sapo.com.model.dto.response.ProductResponse;
import sapo.com.model.dto.response.VariantResponse;

import java.util.List;
import java.util.Set;

public interface ProductService {
     List<ProductResponse> getListOfProducts(Long page, Long limit, String queryString) throws ResourceNotFoundException;
     List<VariantResponse> getListOfVariants(Long page, Long limit, String queryString) throws ResourceNotFoundException;
     ProductResponse getProductById(Long id) throws ResourceNotFoundException;
     VariantResponse getVariantById(Long productId, Long variantId) throws ResourceNotFoundException;
     ProductResponse createNewProduct(ProductRequest productRequest) throws DataConflictException,ResourceNotFoundException;
     VariantResponse createNewVariant(Long productId,VariantRequest variantRequest ) throws DataConflictException, ResourceNotFoundException;
     ProductResponse updateProduct(Long id,ProductRequest productRequest) throws DataConflictException, ResourceNotFoundException;
     Boolean deleteProductById(Long id) throws ResourceNotFoundException;
     Boolean deleteVariantById(Long productId, Long variantId) throws ResourceNotFoundException;

}
