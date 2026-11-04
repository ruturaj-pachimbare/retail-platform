package com.retail.order.repository;
import com.retail.order.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface OutboxEventRepository extends JpaRepository<OutboxEvent,Long> {
 List<OutboxEvent> findTop50ByPublishedAtIsNullOrderByIdAsc();
}
