package sapo.com.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sapo.com.exception.OrderNotFoundException;
import sapo.com.model.dto.request.order.CreateOrderRequest;
import sapo.com.model.dto.response.ResponseObject;
import sapo.com.model.dto.response.order.OrderRevenueDto;
import sapo.com.model.entity.Order;
import sapo.com.service.OrderService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@RestController
@RequestMapping("/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/")
    public ResponseEntity<ResponseObject> getAllOrder(@RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "limit", defaultValue = "10") int limit,
                                                      @RequestParam(value = "query", defaultValue = "") String query,
                                                      @RequestParam(value = "startDate") String startDate,
                                                      @RequestParam(value = "endDate") String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            return ResponseEntity.ok(ResponseObject.builder().data(orderService.getAllOrder(page, limit, query, LocalDate.parse(startDate, formatter), LocalDate.parse(endDate, formatter).plusDays(1))).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder().message(e.getMessage()).status(null).build());
        }
    }

    @GetMapping("/count")
    public ResponseEntity<ResponseObject> getNumberOfOrders(@RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "limit", defaultValue = "10") int limit,
                                                      @RequestParam(value = "query", defaultValue = "") String query,
                                                      @RequestParam(value = "startDate") String startDate,
                                                      @RequestParam(value = "endDate") String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            return ResponseEntity.ok(ResponseObject.builder().data(orderService.getNumberOfOrders(query, LocalDate.parse(startDate, formatter), LocalDate.parse(endDate, formatter).plusDays(1))).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder().message(e.getMessage()).status(null).build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getOrderDetail(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ResponseObject.builder().data(orderService.getOrderDetail(id)).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder().message(e.getMessage()).status(null).build());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        try {
            return ResponseEntity.ok(ResponseObject.builder().data(orderService.createOrder(createOrderRequest)).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder().message(e.getMessage()).status(null).build());
        }
    }

    @GetMapping("/today")
    public ResponseEntity<OrderRevenueDto> getTodayOrders(@RequestParam(value = "pageNum", required = false, defaultValue = "0") int pageNum,
                                                          @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) throws OrderNotFoundException {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "createdOn"));
        OrderRevenueDto orderRevenue = orderService.getTodayOrdersAndRevenue(pageable);
        return new ResponseEntity<>(orderRevenue, HttpStatus.OK);
    }
}
