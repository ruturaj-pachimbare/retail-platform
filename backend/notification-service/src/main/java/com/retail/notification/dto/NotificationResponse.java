package com.retail.notification.dto;
import com.retail.notification.entity.NotificationRecord;
import java.math.BigDecimal;
import java.time.Instant;
public record NotificationResponse(Long id,String eventId,String eventType,Long orderId,Long customerId,Long productId,Integer quantity,BigDecimal totalAmount,String orderStatus,String notificationStatus,String message,Instant receivedAt) {
 public static NotificationResponse from(NotificationRecord n) { return new NotificationResponse(n.getId(),n.getEventId(),n.getEventType(),n.getOrderId(),n.getCustomerId(),n.getProductId(),n.getQuantity(),n.getTotalAmount(),n.getOrderStatus(),n.getNotificationStatus(),n.getMessage(),n.getReceivedAt()); }
}