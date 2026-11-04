package com.retail.notification.messaging;
import com.retail.notification.service.OrderEventProcessor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
@Component
public class OrderEventConsumer {
 private final OrderEventProcessor processor;
 public OrderEventConsumer(OrderEventProcessor processor){this.processor=processor;}
 @KafkaListener(topics="${app.kafka.order-events-topic}",groupId="${spring.kafka.consumer.group-id}")
 public void consume(String message){processor.process(message);}
}