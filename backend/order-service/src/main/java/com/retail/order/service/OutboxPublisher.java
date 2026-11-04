package com.retail.order.service;
import com.retail.order.entity.OutboxEvent;
import com.retail.order.repository.OutboxEventRepository;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
@Component
public class OutboxPublisher {
 private static final Logger log=LoggerFactory.getLogger(OutboxPublisher.class);
 private final OutboxEventRepository repo; private final KafkaTemplate<String,String> kafka;
 @Value("${app.kafka.order-events-topic}") private String topic;
 public OutboxPublisher(OutboxEventRepository repo,KafkaTemplate<String,String> kafka){this.repo=repo;this.kafka=kafka;}
 @Scheduled(fixedDelayString="${app.outbox.publish-delay-ms:3000}")
 public void publish(){
  for(OutboxEvent e:repo.findTop50ByPublishedAtIsNullOrderByIdAsc()){
   try{kafka.send(topic,String.valueOf(e.getAggregateId()),e.getPayload()).get(); mark(e.getId(),true);}
   catch(Exception ex){log.warn("Outbox event {} will be retried",e.getId(),ex);mark(e.getId(),false);}
  }
 }
 @Transactional public void mark(Long id,boolean success){repo.findById(id).ifPresent(e->{if(success)e.markPublished();else e.incrementAttempts();repo.save(e);});}
}
