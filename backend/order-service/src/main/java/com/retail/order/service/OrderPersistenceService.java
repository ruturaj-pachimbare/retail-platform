package com.retail.order.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.order.dto.*;
import com.retail.order.entity.*;
import com.retail.order.exception.OrderOperationException;
import com.retail.order.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.UUID;
@Service
public class OrderPersistenceService {
 private final OrderRepository orders; private final OutboxEventRepository outbox; private final ObjectMapper mapper;
 public OrderPersistenceService(OrderRepository orders,OutboxEventRepository outbox,ObjectMapper mapper){this.orders=orders;this.outbox=outbox;this.mapper=mapper;}
 @Transactional public OrderResponse create(CreateOrderRequest r){
  CustomerOrder o=orders.save(new CustomerOrder(r.customerId(),r.productId(),r.quantity(),r.unitPrice()));
  addEvent(o,"ORDER_CREATED"); return response(o);
 }
 @Transactional public OrderResponse cancel(Long id){
  CustomerOrder o=orders.findById(id).orElseThrow(()->new com.retail.order.exception.OrderNotFoundException(id));
  o.cancel(); orders.save(o); addEvent(o,"ORDER_CANCELLED"); return response(o);
 }
 private void addEvent(CustomerOrder o,String type){
  try { OrderEvent e=new OrderEvent(UUID.randomUUID().toString(),type,o.getId(),o.getCustomerId(),o.getProductId(),o.getQuantity(),o.getTotalAmount(),o.getStatus(),Instant.now());
   outbox.save(new OutboxEvent("ORDER",o.getId(),type,mapper.writeValueAsString(e)));
  } catch(JsonProcessingException e){throw new OrderOperationException("Could not serialize order event");}
 }
 private OrderResponse response(CustomerOrder o){return new OrderResponse(o.getId(),o.getCustomerId(),o.getProductId(),o.getQuantity(),o.getUnitPrice(),o.getTotalAmount(),o.getStatus(),o.getCreatedAt(),o.getUpdatedAt());}
}
