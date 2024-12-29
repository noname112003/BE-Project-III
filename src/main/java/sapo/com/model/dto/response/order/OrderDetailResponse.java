package sapo.com.model.dto.response.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import sapo.com.model.entity.Order;
import sapo.com.model.entity.OrderDetail;
import sapo.com.repository.OrderDetailRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDetailResponse {
    private String code;
    private Long customerId;
    private Long creatorId;
    private List<OrderDetail> orderDetails;
    private String note;
    private String paymentType;
    private BigDecimal cashReceive;
    private BigDecimal cashRepay;
    private BigDecimal totalPayment;
    private int totalQuantity;
    private LocalDateTime createdOn;

    public OrderDetailResponse(Order order) {
        this.code = order.getCode();
        this.customerId = order.getCustomer().getId();
        this.creatorId = order.getCreator().getId();
        this.note = order.getNote();
        this.paymentType = order.getPaymentType();
        this.cashReceive = order.getCashReceive();
        this.cashRepay = order.getCashRepay();
        this.totalPayment = order.getTotalPayment();
        this.totalQuantity = order.getTotalQuantity();
        this.createdOn = order.getCreatedOn();
    }
}
