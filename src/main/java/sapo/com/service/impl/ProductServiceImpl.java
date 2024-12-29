package sapo.com.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import sapo.com.exception.DataConflictException;
import sapo.com.exception.ResourceNotFoundException;
import sapo.com.model.dto.request.ProductRequest;
import sapo.com.model.dto.request.VariantRequest;
import sapo.com.model.dto.response.ProductResponse;
import sapo.com.model.dto.response.VariantResponse;
import sapo.com.model.entity.*;
import sapo.com.repository.*;
import sapo.com.service.ProductService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ImagePathRepository imagePathRepository;

    public List<ProductResponse> getListOfProducts(Long page, Long limit, String queryString) {
        Set<Product> products = productRepository.getListOfProducts(page + 1, limit, queryString);
        List<ProductResponse> productsResponse = new ArrayList<>();
        for (Product product : products) {
            productsResponse.add(product.transferToResponse());
        }
        if (!productsResponse.isEmpty())
            return productsResponse;
        else throw new ResourceNotFoundException("Sản phẩm không tồn tại");
    }

    public Long getNumberOfProducts(String queryString) {
        return productRepository.countByNameContainingAndStatus(queryString, true);
    }

    public List<VariantResponse> getListOfVariants(Long page, Long limit, String queryString) {
        Set<Variant> variants = variantRepository.getListOfVariants(page+1, limit, queryString);
        List<VariantResponse> variantsResponse = new ArrayList<>();
        for (Variant variant : variants) {
            variantsResponse.add(variant.transferToResponse());
        }
        if (!variantsResponse.isEmpty())
            return variantsResponse;
        else throw new ResourceNotFoundException("Phiên bản không tồn tại");
    }

    public Long getNumberOfVariants(String queryString) {
        return variantRepository.countByNameContainingAndStatus(queryString, true);
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm không tồn tại hoặc đã bị xóa"));
        if (product.getStatus()) {
            product.getVariants().removeIf(variant -> !variant.getStatus());
            ProductResponse productResponse = product.transferToResponse();
            System.out.println(product.getId());
            statisticSizeColorMaterial(productResponse);
            return productResponse;
        } else
            throw new ResourceNotFoundException("Sản phẩm không tồn tại hoặc đã bị xóa");
    }

    public VariantResponse getVariantById(Long productId, Long variantId) {
        Variant variant = variantRepository.findByIdAndProductId(productId, variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Phiên bản không tồn tại hoặc đã bị xóa"));
        if (variant.getStatus()) {
            VariantResponse variantResponse = variant.transferToResponse();
            return variantResponse;
        } else
            throw new ResourceNotFoundException("Phiên bản không tồn tại hoặc đã bị xóa");
    }

    @Transactional
    public ProductResponse createNewProduct(ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Loại sản phẩm không tồn tại hoặc đã bị xóa"));
        Brand brand = brandRepository.findById(productRequest.getBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Nhãn hiệu không tồn tại hoặc đã bị xóa"));
        //Check sku and variant
        Set<String> variantPropertiesSet = new HashSet<>();
        for (VariantRequest variantRequest : productRequest.getVariants()) {
            String sku = variantRequest.getSku();
            if (sku != "" && sku.startsWith("PVN")) {
                throw new DataConflictException("Mã loại không được có tiền tố " + "PVN");
            }
            if (variantRepository.existsBySku(sku)) {
                throw new DataConflictException("SKU " + sku + " đã tồn tại.");
            }
            String variantKey = variantRequest.getSize() + "-" +
                    variantRequest.getColor() + "-" +
                    variantRequest.getMaterial();
            // Check if combination of size, color, and material is unique
            if (!variantPropertiesSet.add(variantKey)) {
                throw new DataConflictException("Thuộc tính đã bị trùng: " + variantKey);
            }
        }
        // Map ProductRequest to Product entity
        Product product = productRequest.transferToProduct();
        product.setBrand(brand);
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        entityManager.refresh(savedProduct);
        ProductResponse productResponse = savedProduct.transferToResponse();
        statisticSizeColorMaterial(productResponse);
        return productResponse;
    }

    @Transactional
    public VariantResponse createNewVariant(Long productId, VariantRequest variantRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm không tồn tại hoặc đã bị xóa"));
        //Check sku
        String sku = variantRequest.getSku();
        if (sku != "" && sku.startsWith("PVN")) {
            throw new DataConflictException("Mã loại không được có tiền tố " + "PVN");
        }
        if (variantRepository.existsBySku(sku)) {
            throw new DataConflictException("SKU " + sku + " đã tồn tại.");
        }
        //check variant
        String newVariantKey = variantRequest.getSize() + "-" +
                variantRequest.getColor() + "-" +
                variantRequest.getMaterial();
        for (Variant variant : product.getVariants()) {
            if(variant.getSize().isEmpty()&&variant.getColor().isEmpty()&&variant.getMaterial().isEmpty()){
                variant.updateFromRequest(variantRequest);
                Variant savedVariant = variantRepository.saveAndFlush(variant);
                product.setUpdatedOn(LocalDateTime.now());
                productRepository.saveAndFlush(product);
                entityManager.refresh(savedVariant);
                return savedVariant.transferToResponse();
            }
            if(variant.getSize().isEmpty()&&!variantRequest.getSize().isEmpty()){
                variant.setSize(variantRequest.getSize());
            }
            if(variant.getColor().isEmpty()&&!variantRequest.getColor().isEmpty()){
                variant.setColor(variantRequest.getColor());
            }
            if(variant.getMaterial().isEmpty()&&!variantRequest.getMaterial().isEmpty()){
                variant.setMaterial(variantRequest.getMaterial());
            }
            String variantKey = variant.getSize() + "-" +
                    variant.getColor() + "-" +
                    variant.getMaterial();
            if (newVariantKey.equals(variantKey)) {
                throw new DataConflictException("Thuộc tính đã bị trùng: " + variantKey);
            }
        }
        variantRequest.setProductId(productId);
        Variant variant = variantRequest.transferToVariant();
        variant.setProduct(product);
        Variant savedVariant = variantRepository.save(variant);
        product.setUpdatedOn(LocalDateTime.now());
        product.setTotalQuantity(product.getTotalQuantity() + variant.getQuantity());
        productRepository.saveAndFlush(product);
        entityManager.refresh(savedVariant);
        return savedVariant.transferToResponse();
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm không tồn tại hoặc đã bị xóa"));
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Loại sản phẩm không tồn tại hoặc đã bị xóa"));
        Brand brand = brandRepository.findById(productRequest.getBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Nhãn hiệu không tồn tại hoặc đã bị xóa"));
        //check properties of variant
        Set<String> variantPropertiesSet = new HashSet<>();
        Long totalQuantity = 0L;
        for (VariantRequest variantRequest : productRequest.getVariants()) {
            String variantKey = variantRequest.getSize() + "-" +
                    variantRequest.getColor() + "-" +
                    variantRequest.getMaterial();
            // Check if combination of size, color, and material is unique
            if (!variantPropertiesSet.add(variantKey)) {
                throw new DataConflictException("Thuộc tính đã bị trùng: " + variantKey);
            }
            totalQuantity += variantRequest.getQuantity();
        }
        product.setTotalQuantity(totalQuantity);

        product.setBrand(brand);
        product.setCategory(category);
        product.setDescription(productRequest.getDescription());
        product.setName(productRequest.getName());
        Map<Long, VariantRequest> existingVariantsMap = productRequest.getVariants().stream()
                .collect(Collectors.toMap(VariantRequest::getId, Function.identity()));
        //check sku of variants

        for (Variant variant : product.getVariants()) {
            String newSku = existingVariantsMap.get(variant.getId()).getSku();
            if (newSku != "" && !newSku.equals(variant.getSku())) {
                if (newSku.startsWith("PVN")) {
                    throw new DataConflictException("Mã loại không được có tiền tố " + "PVN");
                }
                if (variantRepository.existsBySku(newSku)) {
                    throw new DataConflictException("SKU " + newSku + " đã tồn tại.");
                }
            }
            variant.updateFromRequest(existingVariantsMap.get(variant.getId()));
        }
        product.getImagePath().clear();
        for (String imagePath : productRequest.getImagePath()) {
            ImagePath image = new ImagePath();
            image.setPath(imagePath);
            image.setProduct(product);
            product.getImagePath().add(image);
        }
        product.setUpdatedOn(LocalDateTime.now());
        Product savedProduct = productRepository.saveAndFlush(product);
        entityManager.refresh(savedProduct);
        ProductResponse productResponse = savedProduct.transferToResponse();
        statisticSizeColorMaterial(productResponse);
        return productResponse;
    }

    public void statisticSizeColorMaterial(ProductResponse productResponse) {
        productResponse.setSize(variantRepository.findDistinctSizesByProductId(productResponse.getId()));
        productResponse.setColor(variantRepository.findDistinctColorsByProductId(productResponse.getId()));
        productResponse.setMaterial(variantRepository.findDistinctMaterialsByProductId(productResponse.getId()));
    }

    @Transactional
    public Boolean deleteProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm không tồn tại hoặc đã bị xóa"));
        productRepository.deleteProductById(id);
        variantRepository.deleteAllVariantOfProduct(id);
        return true;

    }

    @Transactional
    public Boolean deleteVariantById(Long productId, Long variantId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm không tồn tại hoặc đã bị xóa"));
        Variant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Phiên bản không tồn tại hoặc đã bị xóa"));
        if (product.getVariants().size() == 1) {
            deleteProductById(productId);
        } else {
            variantRepository.deleteVariantById(variantId);
        }
        return true;

    }

    @Transactional
    public Boolean deleteVariantByProperty(Long productId, String property, String value) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm không tồn tại hoặc đã bị xóa"));
        if (product.getVariants().size() == 1) {
            deleteProductById(productId);
        } else {
            if (property.equals("size"))
                variantRepository.deleteVariantBySize(productId, value);
            else if (property.equals("color"))
                variantRepository.deleteVariantByColor(productId, value);
            else if (property.equals("material"))
                variantRepository.deleteVariantByMaterial(productId, value);
        }
        return true;

    }
}
