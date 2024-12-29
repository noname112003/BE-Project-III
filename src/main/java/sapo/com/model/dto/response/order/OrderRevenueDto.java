package sapo.com.model.dto.response.order;

import org.springframework.data.domain.Page;
import sapo.com.model.entity.Order;

import java.math.BigDecimal;

public class OrderRevenueDto {
    private Page<Order> orders;
    private BigDecimal totalRevenue;

    public OrderRevenueDto(Page<Order> orders, BigDecimal totalRevenue) {
        this.orders = orders;
        this.totalRevenue = totalRevenue;
    }

    public Page<Order> getOrders() {
        return orders;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }
}
