package sapo.com.model.dto.response.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sapo.com.model.dto.response.order.AllOrderResponse;
import sapo.com.model.entity.Customer;
import sapo.com.model.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerDetailResponse {
    private Long id;
    private String code;
    private String name;
    private String phoneNumber;
    private String email;
    private Date birthday;
    private boolean gender;
    private String address;
    private BigDecimal totalExpense;
    private int numberOfOrder;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private String note;
    private List<AllOrderResponse> orders;
    private LocalDateTime earliestOrderDate; // Ngày mua đầu tiên
    private LocalDateTime latestOrderDate; // Ngày mua gần nhất

    // Constructor
    public CustomerDetailResponse(Customer customer) {
        this.id = customer.getId();
        this.code = customer.getCode();
        this.name = customer.getName();
        this.phoneNumber = customer.getPhoneNumber();
        this.email = customer.getEmail();
        this.birthday = customer.getBirthday();
        this.gender = customer.isGender();
        this.address = customer.getAddress();
        this.totalExpense = customer.getTotalExpense();
        this.numberOfOrder = customer.getNumberOfOrder();
        this.createdOn = customer.getCreatedOn();
        this.updatedOn = customer.getUpdatedOn();
        this.note = customer.getNote();
        this.orders = customer.getOrders() != null ? customer.getOrders().stream()
                .sorted((o1, o2) -> o2.getCreatedOn().compareTo(o1.getCreatedOn()))
                .map(AllOrderResponse::new)
                .collect(Collectors.toList()) : new ArrayList<>();

        if (customer.getOrders() != null && !customer.getOrders().isEmpty()) {
            this.earliestOrderDate = customer.getOrders().stream()
                    .map(Order::getCreatedOn)
                    .min(LocalDateTime::compareTo) // Tìm ngày mua đầu tiên
                    .orElse(null);
            this.latestOrderDate = customer.getOrders().stream()
                    .map(Order::getCreatedOn)
                    .max(LocalDateTime::compareTo) // Tìm ngày mua gần nhất
                    .orElse(null);
        }
    }
}
