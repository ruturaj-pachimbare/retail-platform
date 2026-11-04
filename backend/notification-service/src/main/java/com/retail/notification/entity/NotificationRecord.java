package com.retail.notification.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
@Entity
@Table(name="notifications", uniqueConstraints=@UniqueConstraint(name="uk_notifications_event_id", columnNames="event_id"))
public class NotificationRecord {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="event_id", nullable=false, length=36) private String eventId;
 @Column(name="event_type", nullable=false, length=50) private String eventType;
 @Column(name="order_id", nullable=false) private Long orderId;
 @Column(name="customer_id", nullable=false) private Long customerId;
 @Column(name="product_id", nullable=false) private Long productId;
 @Column(nullable=false) private Integer quantity;
 @Column(name="total_amount", nullable=false, precision=19, scale=2) private BigDecimal totalAmount;
 @Column(name="order_status", nullable=false, length=30) private String orderStatus;
 @Column(name="notification_status", nullable=false, length=30) private String notificationStatus;
 @Column(nullable=false, length=1000) private String message;
 @Column(name="event_payload", nullable=false, columnDefinition="nvarchar(max)") private String eventPayload;
 @Column(name="received_at", nullable=false, columnDefinition="datetimeoffset(6)") private Instant receivedAt;
 protected NotificationRecord() {}
 public NotificationRecord(String eventId,String eventType,Long orderId,Long customerId,Long productId,Integer quantity,BigDecimal totalAmount,String orderStatus,String notificationStatus,String message,String eventPayload,Instant receivedAt) {
 this.eventId=eventId;this.eventType=eventType;this.orderId=orderId;this.customerId=customerId;this.productId=productId;this.quantity=quantity;this.totalAmount=totalAmount;this.orderStatus=orderStatus;this.notificationStatus=notificationStatus;this.message=message;this.eventPayload=eventPayload;this.receivedAt=receivedAt;
 }
 public Long getId(){return id;} public String getEventId(){return eventId;} public String getEventType(){return eventType;}
 public Long getOrderId(){return orderId;} public Long getCustomerId(){return customerId;} public Long getProductId(){return productId;}
 public Integer getQuantity(){return quantity;} public BigDecimal getTotalAmount(){return totalAmount;} public String getOrderStatus(){return orderStatus;}
 public String getNotificationStatus(){return notificationStatus;} public String getMessage(){return message;} public String getEventPayload(){return eventPayload;} public Instant getReceivedAt(){return receivedAt;}
}