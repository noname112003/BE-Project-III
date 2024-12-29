package sapo.com.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sapo.com.exception.CustomerNotFoundException;
import sapo.com.model.dto.response.ResponseObject;
import sapo.com.model.dto.response.customer.CustomerDetailResponse;
import sapo.com.model.dto.response.customer.CustomerResponse;
import sapo.com.model.entity.Customer;
import sapo.com.service.CustomerService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/v1/customers")
public class CustomerController {

    @Autowired private CustomerService customerService;

    @GetMapping("")
    public ResponseEntity<Page<CustomerResponse>> findByKeyword(
            @RequestParam(value="pageNum", required = false, defaultValue = "0") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
            @RequestParam(value = "keyword", required = false) String keyword) throws CustomerNotFoundException {

        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "createdOn"));
        Page<CustomerResponse> customers = customerService.findByKeyword(keyword, pageable);
        return new ResponseEntity<>(customers, HttpStatus.OK); // Trả về trạng thái 200 OK nếu thành công
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> findById(@PathVariable Long customerId) throws CustomerNotFoundException {
        Customer existingCustomer = customerService.findById(customerId);
        if (existingCustomer == null) {
            return new ResponseEntity<>("Không tìm thấy khách hàng ứng với ID: " + customerId, HttpStatus.NOT_FOUND);
        }
        CustomerDetailResponse customerDetailResponse = new CustomerDetailResponse(existingCustomer);
        return new ResponseEntity<>(customerDetailResponse, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createCustomer(@RequestBody Customer customer) {

        // Check if the phone number already exists
        Customer existingCustomer = customerService.findByPhoneNumber(customer.getPhoneNumber());
        if (existingCustomer != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    ResponseObject.builder()
                            .message("Số điện thoại đã tồn tại.")
                            .status(HttpStatus.CONFLICT)
                            .data(null)
                            .build()
            );
        }

        // Save the new customer
        customerService.register(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseObject.builder()
                        .message("Tạo khách hàng mới thành công.")
                        .status(HttpStatus.CREATED)
                        .data(null)
                        .build()
        );

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable("id") Long id,
                                            @RequestBody Customer customerInForm) throws CustomerNotFoundException {
        Customer existingCustomer = customerService.findByPhoneNumber(customerInForm.getPhoneNumber());
        if (existingCustomer != null) {
            if(existingCustomer.getId() != id){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        ResponseObject.builder()
                                .message("Số điện thoại đã tồn tại.")
                                .status(HttpStatus.CONFLICT)
                                .data(null)
                                .build()
                );
            }
        }
        Customer customerInDb = customerService.findById(id);
        customerInForm.setId(id);
        Customer updatedCustomer = customerService.update(customerInForm);
        CustomerResponse customerResponse = new CustomerResponse(updatedCustomer);

        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.builder()
                        .message("Customer ID:"+id+" updated successfully")
                        .status(HttpStatus.OK)
                        .data(customerResponse)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("id") Long customerId) throws CustomerNotFoundException {

        customerService.delete(customerId);
        return new ResponseEntity<>("Customer with ID " + customerId + " has been successfully deleted.",HttpStatus.OK);


    }


}
