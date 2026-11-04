package com.retail.order.service;
import com.retail.order.client.InventoryClient;
import com.retail.order.dto.*;
import com.retail.order.entity.*;
import com.retail.order.exception.*;
import com.retail.order.repository.OrderRepository;
import feign.FeignException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
public class OrderService {
 private final OrderRepository orders; private final InventoryClient inventory; private final OrderPersistenceService persistence;
 public OrderService(OrderRepository orders,InventoryClient inventory,OrderPersistenceService persistence){this.orders=orders;this.inventory=inventory;this.persistence=persistence;}
 public OrderResponse create(CreateOrderRequest r){
  try{inventory.reserve(r.productId(),new StockQuantityRequest(r.quantity()));}
  catch(FeignException e){throw new OrderOperationException("Inventory reservation failed. Check stock and Inventory Service.");}
  try{return persistence.create(r);}
  catch(RuntimeException e){try{inventory.release(r.productId(),new StockQuantityRequest(r.quantity()));}catch(Exception compensation){e.addSuppressed(compensation);}throw e;}
 }
 @Transactional(readOnly=true) public OrderResponse getById(Long id){return orders.findById(id).map(this::response).orElseThrow(()->new OrderNotFoundException(id));}
 @Transactional(readOnly=true) public List<OrderResponse> getAll(){return orders.findAll().stream().map(this::response).toList();}
 @Transactional(readOnly=true) public List<OrderResponse> getByCustomer(Long id){return orders.findByCustomerIdOrderByCreatedAtDesc(id).stream().map(this::response).toList();}
 public OrderResponse cancel(Long id){
  CustomerOrder o=orders.findById(id).orElseThrow(()->new OrderNotFoundException(id));
  if(o.getStatus()==OrderStatus.CANCELLED)throw new OrderOperationException("Order is already cancelled");
  try{inventory.release(o.getProductId(),new StockQuantityRequest(o.getQuantity()));}
  catch(FeignException e){throw new OrderOperationException("Could not release stock; cancellation was not completed.");}
  return persistence.cancel(id);
 }
 private OrderResponse response(CustomerOrder o){return new OrderResponse(o.getId(),o.getCustomerId(),o.getProductId(),o.getQuantity(),o.getUnitPrice(),o.getTotalAmount(),o.getStatus(),o.getCreatedAt(),o.getUpdatedAt());}
}
