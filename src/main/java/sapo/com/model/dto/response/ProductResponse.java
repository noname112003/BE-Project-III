package sapo.com.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sapo.com.model.entity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ProductResponse {
    private Long id ;
    private String name;
    private Long categoryId;
    private String categoryName;
    private Long brandId;
    private String brandName;
    private String description;
    private Long totalQuantity;
    private Boolean status ;
    private Set<String> size;
    private Set<String> color;
    private Set<String> material;
    private Set<String> imagePath;
    private LocalDateTime createdOn ;
    private LocalDateTime updatedOn ;
    private List<VariantResponse> variants;

    public ProductResponse(){

    }
}
