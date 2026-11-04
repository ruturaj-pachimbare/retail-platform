package com.retail.inventory.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "inventory_items",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_inventory_product_id", columnNames = "product_id"),
           @UniqueConstraint(name = "uk_inventory_sku", columnNames = "sku")
       },
       indexes = @Index(name = "ix_inventory_sku", columnList = "sku"))
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false, unique = true)
    private Long productId;

    @Column(nullable = false, length = 50, unique = true)
    private String sku;

    @Column(name = "quantity_on_hand", nullable = false)
    private int quantityOnHand;

    @Column(name = "quantity_reserved", nullable = false)
    private int quantityReserved;

    @Version
    @Column(nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected InventoryItem() {}

    public InventoryItem(Long productId, String sku, int initialQuantity) {
        this.productId = productId;
        this.sku = sku;
        this.quantityOnHand = initialQuantity;
        this.quantityReserved = 0;
    }

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    public void adjustStock(int delta) {
        long updated = (long) quantityOnHand + delta;
        if (updated < quantityReserved) {
            throw new IllegalArgumentException("Stock cannot be reduced below the reserved quantity");
        }
        if (updated > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Stock quantity exceeds the supported limit");
        }
        quantityOnHand = (int) updated;
    }

    public void reserve(int quantity) {
        if (quantity > getAvailableQuantity()) {
            throw new IllegalArgumentException("Insufficient available stock");
        }
        quantityReserved += quantity;
    }

    public void release(int quantity) {
        if (quantity > quantityReserved) {
            throw new IllegalArgumentException("Cannot release more stock than is reserved");
        }
        quantityReserved -= quantity;
    }

    public int getAvailableQuantity() {
        return quantityOnHand - quantityReserved;
    }

    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public String getSku() { return sku; }
    public int getQuantityOnHand() { return quantityOnHand; }
    public int getQuantityReserved() { return quantityReserved; }
    public Long getVersion() { return version; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
