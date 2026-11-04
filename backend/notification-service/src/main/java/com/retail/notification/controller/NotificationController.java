package com.retail.notification.controller;
import com.retail.notification.dto.NotificationResponse;
import com.retail.notification.repository.NotificationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
 private final NotificationRepository repository;
 public NotificationController(NotificationRepository repository){this.repository=repository;}
 @GetMapping public List<NotificationResponse> all(){return repository.findAllByOrderByReceivedAtDesc().stream().map(NotificationResponse::from).toList();}
 @GetMapping("/{id}") public ResponseEntity<NotificationResponse> byId(@PathVariable Long id){return repository.findById(id).map(NotificationResponse::from).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());}
 @GetMapping("/order/{orderId}") public List<NotificationResponse> byOrder(@PathVariable Long orderId){return repository.findByOrderIdOrderByReceivedAtDesc(orderId).stream().map(NotificationResponse::from).toList();}
 @GetMapping("/customer/{customerId}") public List<NotificationResponse> byCustomer(@PathVariable Long customerId){return repository.findByCustomerIdOrderByReceivedAtDesc(customerId).stream().map(NotificationResponse::from).toList();}
}