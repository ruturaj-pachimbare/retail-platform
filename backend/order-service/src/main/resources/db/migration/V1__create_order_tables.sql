CREATE TABLE orders (
 id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
 customer_id BIGINT NOT NULL, product_id BIGINT NOT NULL, quantity INT NOT NULL,
 unit_price DECIMAL(19,2) NOT NULL, total_amount DECIMAL(19,2) NOT NULL,
 status VARCHAR(20) NOT NULL, created_at DATETIMEOFFSET(6) NOT NULL, updated_at DATETIMEOFFSET(6) NOT NULL,
 CONSTRAINT ck_orders_quantity CHECK (quantity > 0),
 CONSTRAINT ck_orders_amounts CHECK (unit_price >= 0 AND total_amount >= 0)
);
CREATE INDEX ix_orders_customer_id ON orders(customer_id);
CREATE INDEX ix_orders_created_at ON orders(created_at);
CREATE TABLE outbox_events (
 id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY, aggregate_type VARCHAR(100) NOT NULL,
 aggregate_id BIGINT NOT NULL, event_type VARCHAR(150) NOT NULL, payload NVARCHAR(MAX) NOT NULL,
 created_at DATETIMEOFFSET(6) NOT NULL, published_at DATETIMEOFFSET(6) NULL,
 attempts INT NOT NULL CONSTRAINT df_outbox_attempts DEFAULT 0
);
CREATE INDEX ix_outbox_unpublished ON outbox_events(published_at, id);
