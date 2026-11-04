CREATE TABLE inventory_items
(
    id                  BIGINT IDENTITY(1,1) NOT NULL,
    product_id          BIGINT NOT NULL,
    sku                 VARCHAR(50) NOT NULL,
    quantity_on_hand    INT NOT NULL,
    quantity_reserved   INT NOT NULL CONSTRAINT df_inventory_reserved DEFAULT 0,
    version             BIGINT NOT NULL CONSTRAINT df_inventory_version DEFAULT 0,
    created_at          DATETIME2 NOT NULL,
    updated_at          DATETIME2 NOT NULL,

    CONSTRAINT pk_inventory_items PRIMARY KEY (id),
    CONSTRAINT uk_inventory_product_id UNIQUE (product_id),
    CONSTRAINT uk_inventory_sku UNIQUE (sku),
    CONSTRAINT ck_inventory_on_hand_nonnegative CHECK (quantity_on_hand >= 0),
    CONSTRAINT ck_inventory_reserved_nonnegative CHECK (quantity_reserved >= 0),
    CONSTRAINT ck_inventory_reserved_not_over_on_hand CHECK (quantity_reserved <= quantity_on_hand)
);

CREATE INDEX ix_inventory_sku ON inventory_items(sku);
