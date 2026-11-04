CREATE TABLE products
(
    id          BIGINT IDENTITY(1,1) NOT NULL,
    name        VARCHAR(120) NOT NULL,
    description VARCHAR(1000) NULL,
    category    VARCHAR(80) NOT NULL,
    sku         VARCHAR(50) NOT NULL,
    price       DECIMAL(12,2) NOT NULL,
    active      BIT NOT NULL CONSTRAINT df_products_active DEFAULT 1,
    created_at  DATETIME2 NOT NULL,
    updated_at  DATETIME2 NOT NULL,

    CONSTRAINT pk_products PRIMARY KEY (id),
    CONSTRAINT uk_products_sku UNIQUE (sku)
);
