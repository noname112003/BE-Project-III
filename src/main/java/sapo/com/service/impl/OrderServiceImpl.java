package sapo.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sapo.com.exception.OrderNotFoundException;
import sapo.com.model.dto.request.order.CreateOrderRequest;
import sapo.com.model.dto.request.order.CreateOrderDetailRequest;
import sapo.com.model.dto.response.order.AllOrderResponse;
import sapo.com.model.dto.response.order.OrderDetailResponse;
import sapo.com.model.dto.response.order.OrderRevenueDto;
import sapo.com.model.entity.*;
import sapo.com.repository.*;
import sapo.com.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public OrderDetailResponse createOrder(CreateOrderRequest createOrderRequest) {
        // Kiểm tra thông tin đầu vào
        Customer customer = customerRepository.findById(createOrderRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));
        User user = userRepository.findById(createOrderRequest.getCreatorId())
                .orElseThrow(() -> new RuntimeException("Người tạo đơn hàng không tồn tại"));
        if (createOrderRequest.getOrderLineItems().isEmpty()) {
            throw new RuntimeException("Đơn hàng không có sản phẩm");
        }
        if (createOrderRequest.getCashReceive().compareTo(createOrderRequest.getTotalPayment()) < 0) {
            throw new RuntimeException("Số tiền nhận không hợp lệ");
        }

        // Tạo đơn hàng
        Order order = new Order();
        order.setCustomer(customer);
        order.setCreator(user);
        order.setTotalQuantity(createOrderRequest.getTotalQuantity());
        order.setTotalPayment(createOrderRequest.getTotalPayment());
        order.setCashReceive(createOrderRequest.getCashReceive());
        order.setCashRepay(createOrderRequest.getCashRepay());
        order.setPaymentType(createOrderRequest.getPaymentType());
        order.setNote(createOrderRequest.getNote());
        Order newOrder = orderRepository.save(order);

        // Tạo chi tiết đơn hàng
        final Set<CreateOrderDetailRequest> orderDetails = createOrderRequest.getOrderLineItems();
        orderDetails.forEach(createOrderDetailRequest -> {
            Variant variant = variantRepository.findById(createOrderDetailRequest.getVariantId())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
            if(createOrderDetailRequest.getQuantity() < 0) {
                throw new RuntimeException("Số lượng sản phẩm không hợp lệ");
            }
            if(createOrderDetailRequest.getQuantity() > variant.getQuantity()) {
                throw new RuntimeException("Số lượng sản phẩm không đủ");
            }

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(newOrder);
            orderDetail.setVariant(variant);
            orderDetail.setQuantity(createOrderDetailRequest.getQuantity());
            orderDetail.setSubTotal(createOrderDetailRequest.getSubTotal());
            orderDetailRepository.save(orderDetail);

            // Cập nhật số lượng sản phẩm
            variant.setQuantity(variant.getQuantity() - createOrderDetailRequest.getQuantity());
            variantRepository.save(variant);

            // Cập nhật tổng số sản phẩm trong product
            Product product = productRepository.findByVariantId(variant.getId());
            product.setTotalQuantity(product.getTotalQuantity() - createOrderDetailRequest.getQuantity());
        });

        // Cập nhật thông tin khách hàng
        customer.setNumberOfOrder(customer.getNumberOfOrder() + 1);
        if(customer.getTotalExpense() == null) {
            customer.setTotalExpense(newOrder.getTotalPayment());
        } else customer.setTotalExpense(customer.getTotalExpense().add(newOrder.getTotalPayment()));

        // Thêm mã đơn hàng
        newOrder.setCode("SON" + String.format("%05d", newOrder.getId()));
        orderRepository.save(newOrder);

        return getOrderDetail(newOrder.getId());
    }

    @Override
    public List<AllOrderResponse> getAllOrder(int page, int limit, String query, LocalDate startDate, LocalDate endDate) {
        List<Order> orders = orderRepository.findOrdersByDateAndCode(startDate, endDate, query);
        // Chuyển đổi danh sách đơn hàng sang danh sách response
        List<AllOrderResponse> allOrderResponseList = orders.stream().map(AllOrderResponse::new).toList();
        // Phân trang
        return allOrderResponseList.subList(Math.max(page * limit, 0), Math.min((page + 1) * limit, allOrderResponseList.size()));
    }

    @Override
    public int getNumberOfOrders(String query, LocalDate startDate, LocalDate endDate) {
        return orderRepository.findOrdersByDateAndCode(startDate, endDate, query).size();
    }

    @Override
    public OrderDetailResponse getOrderDetail(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));
        OrderDetailResponse orderDetailResponse = new OrderDetailResponse(order);
        // Lấy chi tiết đơn hàng
        orderDetailResponse.setOrderDetails(orderDetailRepository.findAllByOrderId(orderId));
        return orderDetailResponse;
    }

    @Override
    public OrderRevenueDto getTodayOrdersAndRevenue(Pageable pageable) throws OrderNotFoundException {
        LocalDate today = LocalDate.now();

        // Lấy danh sách đơn hàng
        Page<Order> ordersToday = orderRepository.findOrdersToday(today, pageable);

        if (ordersToday.isEmpty()) {
            throw new OrderNotFoundException("Không tìm thấy đơn hàng nào cho ngày hôm nay.");
        }

        // Tính tổng doanh thu
        BigDecimal totalRevenue = ordersToday.getContent().stream()
                .map(Order::getTotalPayment)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new OrderRevenueDto(ordersToday, totalRevenue);
    }
}
