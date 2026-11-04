package com.retail.order.controller;
import com.retail.order.dto.*;
import com.retail.order.service.OrderService;
import jakarta.validation.Valid; import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus; import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/v1/orders")
public class OrderController {
 private final OrderService service; public OrderController(OrderService service){this.service=service;}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public OrderResponse create(@Valid @RequestBody CreateOrderRequest r){return service.create(r);}
 @GetMapping public List<OrderResponse> all(){return service.getAll();}
 @GetMapping("/{id}") public OrderResponse one(@PathVariable @Positive Long id){return service.getById(id);}
 @GetMapping("/customer/{customerId}") public List<OrderResponse> customer(@PathVariable @Positive Long customerId){return service.getByCustomer(customerId);}
 @PostMapping("/{id}/cancel") public OrderResponse cancel(@PathVariable @Positive Long id){return service.cancel(id);}
}
