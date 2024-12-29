package sapo.com.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import sapo.com.model.entity.Product;
import sapo.com.model.entity.Variant;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class VariantResponse {
    private Long id ;
    private String name;
    private Long productId;
    private String productName;
    private String sku;
    private String size;
    private String color;
    private String material;
    private Long quantity;
    private BigDecimal initialPrice;
    private BigDecimal priceForSale;
    private Boolean status ;
    private String imagePath ;
    private LocalDateTime createdOn ;
    private LocalDateTime updateOn ;

    public VariantResponse(){

    }
}
