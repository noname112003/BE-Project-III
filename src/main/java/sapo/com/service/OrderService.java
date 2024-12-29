package sapo.com.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sapo.com.exception.OrderNotFoundException;
import sapo.com.model.dto.request.order.CreateOrderRequest;
import sapo.com.model.dto.response.order.AllOrderResponse;
import sapo.com.model.dto.response.order.OrderDetailResponse;
import sapo.com.model.dto.response.order.OrderRevenueDto;
import sapo.com.model.entity.Order;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface OrderService {
    OrderDetailResponse createOrder(CreateOrderRequest createOrderRequest);
    List<AllOrderResponse> getAllOrder(int page, int limit, String query, LocalDate startDate, LocalDate endDate);
    OrderDetailResponse getOrderDetail(Long orderId);
    OrderRevenueDto getTodayOrdersAndRevenue(Pageable pageable) throws OrderNotFoundException;
    int getNumberOfOrders(String query, LocalDate startDate, LocalDate endDate);
}
