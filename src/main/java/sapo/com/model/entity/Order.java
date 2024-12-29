package sapo.com.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonBackReference(value = "customer-orders")
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    @ManyToOne
    @JsonBackReference(value = "user-order")
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;
    @Column(unique = true)
    private String code;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "created_on")
    @CreationTimestamp
    private LocalDateTime createdOn;
    @Column(name = "total_quantity")
    private int totalQuantity;
    private String note;
    @Column(name = "cash_receive")
    private BigDecimal cashReceive;
    @Column(name = "cash_repay")
    private BigDecimal cashRepay;
    @Column(name = "total_payment")
    private BigDecimal totalPayment;
    @Column(name = "payment_type")
    private String paymentType;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonManagedReference(value = "order-orderDetails")
    @JsonIgnore
    private Set<OrderDetail> orderDetails;
}
