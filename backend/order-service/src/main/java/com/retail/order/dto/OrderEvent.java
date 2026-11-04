package com.retail.order.dto;
import com.retail.order.entity.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;
public record OrderEvent(String eventId,String eventType,Long orderId,Long customerId,Long productId,
 Integer quantity,BigDecimal totalAmount,OrderStatus status,Instant occurredAt) {}
