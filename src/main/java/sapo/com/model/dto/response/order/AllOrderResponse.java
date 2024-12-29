package sapo.com.model.dto.response.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sapo.com.model.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AllOrderResponse {
    private Long id;
    private String code;
    private String customerName;
    private LocalDateTime createdOn;
    private int totalQuantity;
    private BigDecimal totalPayment;

    public AllOrderResponse(Order order) {
        this.id = order.getId();
        this.code = order.getCode();
        this.customerName = order.getCustomer().getName();
        this.createdOn = order.getCreatedOn();
        this.totalQuantity = order.getTotalQuantity();
        this.totalPayment = order.getTotalPayment();
    }
}
