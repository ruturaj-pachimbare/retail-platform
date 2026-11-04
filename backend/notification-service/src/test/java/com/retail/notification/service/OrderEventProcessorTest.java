package com.retail.notification.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.notification.entity.NotificationRecord;
import com.retail.notification.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
class OrderEventProcessorTest {
 private final NotificationRepository repo=mock(NotificationRepository.class);
 private final OrderEventProcessor processor=new OrderEventProcessor(new ObjectMapper(),repo);
 private final String event="{\"eventId\":\"evt-001\",\"eventType\":\"ORDER_CREATED\",\"orderId\":101,\"customerId\":1001,\"productId\":1,\"quantity\":2,\"totalAmount\":99998.00,\"status\":\"CONFIRMED\",\"occurredAt\":\"2026-11-04T10:15:30Z\"}";
 @Test void savesNewEvent(){when(repo.findByEventId("evt-001")).thenReturn(Optional.empty());processor.process(event);verify(repo).save(any(NotificationRecord.class));}
 @Test void ignoresDuplicate(){when(repo.findByEventId("evt-001")).thenReturn(Optional.of(mock(NotificationRecord.class)));processor.process(event);verify(repo,never()).save(any());}
 @Test void rejectsMissingEventId(){org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,()->processor.process("{\"eventType\":\"ORDER_CREATED\"}"));verify(repo,never()).save(any());}
}