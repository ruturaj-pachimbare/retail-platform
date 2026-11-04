package com.retail.notification.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.notification.entity.NotificationRecord;
import com.retail.notification.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
@Service
public class OrderEventProcessor {
 private static final Logger log=LoggerFactory.getLogger(OrderEventProcessor.class);
 private final ObjectMapper mapper; private final NotificationRepository repository;
 public OrderEventProcessor(ObjectMapper mapper,NotificationRepository repository){this.mapper=mapper;this.repository=repository;}
 @Transactional
 public void process(String raw) {
  try {
   // Exact flat JSON contract from com.retail.order.dto.OrderEvent in Order Service.
   OrderEventMessage e=mapper.readValue(raw,OrderEventMessage.class);
   if(e.eventId()==null||e.eventId().isBlank()||e.eventType()==null||e.orderId()==null||e.customerId()==null||e.productId()==null||e.quantity()==null||e.totalAmount()==null||e.status()==null)
    throw new IllegalArgumentException("Order event is missing required fields");
   if(repository.findByEventId(e.eventId()).isPresent()){log.info("Duplicate event ignored: {}",e.eventId());return;}
   String message=switch(e.eventType()){
    case "ORDER_CREATED" -> "Your order #"+e.orderId()+" has been created successfully.";
    case "ORDER_CANCELLED" -> "Your order #"+e.orderId()+" has been cancelled.";
    default -> "Your order #"+e.orderId()+" was updated. Event: "+e.eventType()+".";
   };
   repository.save(new NotificationRecord(e.eventId(),e.eventType(),e.orderId(),e.customerId(),e.productId(),e.quantity(),e.totalAmount(),e.status(),"SIMULATED",message,raw,e.occurredAt()==null?Instant.now():e.occurredAt()));
   log.info("Notification recorded: eventId={}, orderId={}, type={}",e.eventId(),e.orderId(),e.eventType());
  } catch(Exception ex) { throw new IllegalArgumentException("Could not process Order Service event",ex); }
 }
 public record OrderEventMessage(String eventId,String eventType,Long orderId,Long customerId,Long productId,Integer quantity,java.math.BigDecimal totalAmount,String status,Instant occurredAt){}
}