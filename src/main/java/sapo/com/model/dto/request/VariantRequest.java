package sapo.com.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import sapo.com.model.entity.ImagePath;
import sapo.com.model.entity.Variant;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
public class VariantRequest {
    private Long id;
    private Long productId;
    @Valid
    @NotBlank(message = "Tên phiên bản không được trống.")
    private String name;
    private String sku;
    private String size;
    private String color;
    private String material;
    @Valid
    @NotNull(message = "Giá nhập không được trống.")
    @Min(value = 0,message = "Giá tiền phải là số nguyên dương.")
    private BigDecimal initialPrice;
    @Valid
    @NotNull(message = "Giá bán không được trống")
    @Min(value = 0,message = "Giá tiền phải là số nguyên dương.")
    private BigDecimal priceForSale;
    private String imagePath;
    private Long quantity;

    // Do not have product
    public Variant transferToVariant(){
        Variant variant =new Variant();
        variant.setName(this.name);
        variant.setId(this.id);
        variant.setSku(this.sku);
        variant.setSize(this.size);
        variant.setColor(this.color);
        variant.setMaterial(this.material);
        variant.setInitialPrice(this.initialPrice);
        variant.setPriceForSale(this.priceForSale);
        variant.setImagePath(this.imagePath);
        variant.setQuantity(this.quantity);
        return variant;
    }


}
