package sapo.com.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String code;
    @Column(nullable = false)
    private String name;
    @Column(name = "phone_number", nullable = false, length = 10)
    private String phoneNumber;
    @Column
    private String email;
    @Column(nullable = false)
    private boolean gender;
    @Column(columnDefinition = "TEXT")
    private String note;
    @Column
    private boolean status;

    @Column(columnDefinition = "TEXT")
    private String address;
    @Column
    private Date birthday;
    @Column(name = "created_on")
    @CreationTimestamp
    private LocalDateTime createdOn;
    @Column(name = "updated_on")
    @UpdateTimestamp
    private LocalDateTime updatedOn;
    @Column(name = "total_expense", columnDefinition = "Decimal(10,2) default '0.00'")
    private BigDecimal totalExpense;
    @Column(name = "number_of_order")
    private int numberOfOrder;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "customer-orders")
    private List<Order> orders;


}
