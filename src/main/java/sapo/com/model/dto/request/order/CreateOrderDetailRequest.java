package sapo.com.model.dto.request.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateOrderDetailRequest {
    private Long variantId;
    private int quantity;
    private BigDecimal subTotal;
}
