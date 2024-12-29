package sapo.com.model.dto.request.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateOrderRequest {
    private Long customerId;
    private Long creatorId;
    private int totalQuantity;
    private String note;
    private BigDecimal cashReceive;
    private BigDecimal cashRepay;
    private BigDecimal totalPayment;
    private String paymentType;
    private Set<CreateOrderDetailRequest> orderLineItems;
}
