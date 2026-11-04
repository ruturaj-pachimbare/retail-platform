package com.retail.order.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
@Entity @Table(name="orders")
public class CustomerOrder {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="customer_id",nullable=false) private Long customerId;
 @Column(name="product_id",nullable=false) private Long productId;
 @Column(nullable=false) private Integer quantity;
 @Column(name="unit_price",nullable=false,precision=19,scale=2) private BigDecimal unitPrice;
 @Column(name="total_amount",nullable=false,precision=19,scale=2) private BigDecimal totalAmount;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private OrderStatus status;
 @Column(name="created_at",nullable=false) private Instant createdAt;
 @Column(name="updated_at",nullable=false) private Instant updatedAt;
 protected CustomerOrder() {}
 public CustomerOrder(Long customerId,Long productId,Integer quantity,BigDecimal unitPrice) {
  this.customerId=customerId; this.productId=productId; this.quantity=quantity; this.unitPrice=unitPrice;
  this.totalAmount=unitPrice.multiply(BigDecimal.valueOf(quantity)); this.status=OrderStatus.CONFIRMED;
 }
 @PrePersist void create(){createdAt=Instant.now();updatedAt=createdAt;}
 @PreUpdate void update(){updatedAt=Instant.now();}
 public void cancel(){if(status==OrderStatus.CANCELLED) throw new IllegalStateException("Order is already cancelled");status=OrderStatus.CANCELLED;}
 public Long getId(){return id;} public Long getCustomerId(){return customerId;} public Long getProductId(){return productId;}
 public Integer getQuantity(){return quantity;} public BigDecimal getUnitPrice(){return unitPrice;}
 public BigDecimal getTotalAmount(){return totalAmount;} public OrderStatus getStatus(){return status;}
 public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;}
}
