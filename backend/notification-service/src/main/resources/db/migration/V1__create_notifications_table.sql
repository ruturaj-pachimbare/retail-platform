CREATE TABLE notifications (
 id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
 event_id NVARCHAR(36) NOT NULL,
 event_type NVARCHAR(50) NOT NULL,
 order_id BIGINT NOT NULL,
 customer_id BIGINT NOT NULL,
 product_id BIGINT NOT NULL,
 quantity INT NOT NULL,
 total_amount DECIMAL(19,2) NOT NULL,
 order_status NVARCHAR(30) NOT NULL,
 notification_status NVARCHAR(30) NOT NULL,
 message NVARCHAR(1000) NOT NULL,
 event_payload NVARCHAR(MAX) NOT NULL,
 received_at DATETIMEOFFSET(6) NOT NULL,
 CONSTRAINT uk_notifications_event_id UNIQUE(event_id)
);
CREATE INDEX ix_notifications_order_id ON notifications(order_id);
CREATE INDEX ix_notifications_customer_id ON notifications(customer_id);
