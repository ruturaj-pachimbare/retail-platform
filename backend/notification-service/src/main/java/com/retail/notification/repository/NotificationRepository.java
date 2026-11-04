package com.retail.notification.repository;
import com.retail.notification.entity.NotificationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface NotificationRepository extends JpaRepository<NotificationRecord,Long> {
 Optional<NotificationRecord> findByEventId(String eventId);
 List<NotificationRecord> findAllByOrderByReceivedAtDesc();
 List<NotificationRecord> findByOrderIdOrderByReceivedAtDesc(Long orderId);
 List<NotificationRecord> findByCustomerIdOrderByReceivedAtDesc(Long customerId);
}