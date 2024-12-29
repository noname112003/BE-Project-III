package sapo.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sapo.com.exception.CustomerNotFoundException;
import sapo.com.model.dto.response.customer.CustomerResponse;
import sapo.com.model.entity.Customer;
import sapo.com.model.entity.Order;
import sapo.com.repository.CustomerRepository;
import sapo.com.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired private CustomerRepository customerRepository;
    @Autowired private OrderRepository orderRepository;

    public Page<CustomerResponse> findByKeyword(String keyword, Pageable pageable) throws CustomerNotFoundException {
        Page<Customer> customersByKeyword;

        if (keyword != null) {
            customersByKeyword = customerRepository.findByKeyword(keyword, pageable);
        } else {
            // Chuyển sang trả về Page với sắp xếp từ Pageable
            customersByKeyword = customerRepository.findAll(pageable);
        }

        // Kiểm tra nếu không tìm thấy khách hàng nào
        if (customersByKeyword.isEmpty()) {
            throw new CustomerNotFoundException("Không tìm thấy khách hàng nào.");
        }

        // Chuyển đổi từ Page<Customer> sang Page<CustomerResponse>
        List<CustomerResponse> customerResponses = customersByKeyword.getContent().stream()
                .map(CustomerResponse::new) // Chuyển đổi mỗi Customer thành CustomerResponse
                .collect(Collectors.toList());

        return new PageImpl<>(customerResponses, pageable, customersByKeyword.getTotalElements());
    }



    public Customer findByPhoneNumber(String phoneNumber) {

        Customer existingCustomer = customerRepository.findByPhoneNumber(phoneNumber);
        return existingCustomer;


    }

    public Customer findById(Long id) throws CustomerNotFoundException{
        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isPresent()) {
            Customer customerById = customer.get();
            return customerById;
        } else {
            throw new CustomerNotFoundException("Customer not found with ID: " + id);
        }
    }

    public void register(Customer customer) {

        // Lưu customer lần đầu để lấy ID
        Customer savedCustomer = customerRepository.save(customer);

        // Tạo mã code dựa trên ID
        String customerCode = String.format("CUS%05d", savedCustomer.getId()); // Tạo mã với 5 chữ số
        savedCustomer.setCode(customerCode);

        // Lưu lại customer với mã code
        customerRepository.save(savedCustomer);
    }

    public Customer update(Customer customerInForm) throws CustomerNotFoundException {
        Optional<Customer> customerInDB = customerRepository.findById(customerInForm.getId());
        if(customerInDB.isPresent()){
            Customer customer = customerInDB.get();
            customer.setName(customerInForm.getName());
            customer.setAddress(customerInForm.getAddress());
            customer.setPhoneNumber(customerInForm.getPhoneNumber());
            customer.setGender(customerInForm.isGender());
            customer.setEmail(customerInForm.getEmail());
            customer.setBirthday(customerInForm.getBirthday());
            customer.setNote(customerInForm.getNote());

            return customerRepository.save(customer);
        }else{
            throw new CustomerNotFoundException("Customer not found with ID: " + customerInForm.getId());
        }
    }

    public void delete(Long id) throws CustomerNotFoundException{

        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isPresent()){
            customerRepository.deleteById(id);

        }else{
            throw new CustomerNotFoundException("Không tìm thấy thông tin khách hàng ID:"+id);
        }

    }

}
