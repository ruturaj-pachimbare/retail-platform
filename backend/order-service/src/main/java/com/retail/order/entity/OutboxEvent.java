package com.retail.order.entity;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="outbox_events")
public class OutboxEvent {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="aggregate_type",nullable=false,length=100) private String aggregateType;
 @Column(name="aggregate_id",nullable=false) private Long aggregateId;
 @Column(name="event_type",nullable=false,length=150) private String eventType;
 @Column(nullable = false, columnDefinition = "nvarchar(max)")
 private String payload;
 @Column(name="created_at",nullable=false) private Instant createdAt;
 @Column(name="published_at") private Instant publishedAt;
 @Column(nullable=false) private Integer attempts=0;
 protected OutboxEvent(){}
 public OutboxEvent(String type,Long aggregateId,String eventType,String payload){this.aggregateType=type;this.aggregateId=aggregateId;this.eventType=eventType;this.payload=payload;this.createdAt=Instant.now();}
 public void markPublished(){publishedAt=Instant.now();} public void incrementAttempts(){attempts++;}
 public Long getId(){return id;} public Long getAggregateId(){return aggregateId;} public String getPayload(){return payload;}
}
