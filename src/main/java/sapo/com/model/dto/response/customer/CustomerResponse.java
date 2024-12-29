package sapo.com.model.dto.response.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sapo.com.model.entity.Customer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
    private Long id;
    private String code;
    private String name;
    private String phoneNumber;
    private String email;
    private boolean gender;
    private String address;
    private BigDecimal totalExpense;
    private int numberOfOrder;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private String note;
    private Date birthday;

    public CustomerResponse(Customer customer){
        this.id = customer.getId();
        this.code = customer.getCode();
        this.name = customer.getName();
        this.phoneNumber = customer.getPhoneNumber();
        this.email = customer.getEmail();
        this.gender = customer.isGender();
        this.address = customer.getAddress();
        this.totalExpense = customer.getTotalExpense();
        this.numberOfOrder = customer.getNumberOfOrder();
        this.createdOn = customer.getCreatedOn();
        this.updatedOn = customer.getUpdatedOn();
        this.note = customer.getNote();
        this.birthday = customer.getBirthday();
    }
}
